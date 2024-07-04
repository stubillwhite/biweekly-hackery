from typing import Optional, Any, cast

import requests
import asyncio
from dataclasses import dataclass
from datetime import datetime
import xmltodict

from pydantic import Field, AliasPath, AliasChoices
from pydantic_xml import BaseXmlModel, RootXmlModel, attr, element, wrapped

from src.newsfeed.utils import extract


class RSS2Feed(BaseXmlModel, extra="ignore"):
    title: str
    link: str
    description: str

    @classmethod
    def extract_nested_fields(cls, data: dict[Any, Any]) -> dict[Any, Any]:
        return data

class AtomFeed(BaseXmlModel, extra="ignore"):
    class Entry(BaseXmlModel, extra="ignore"):
        id: str
        title: str
        updated: datetime

    id: str
    title: str
    updated: datetime
    entries: list[Entry] = element(alias="entry", tag="entry")
    link: Optional[str] = Field(alias="extracted_link_self")

    @classmethod
    def extract_nested_fields(cls, data: dict[Any, Any]) -> dict[Any, Any]:
        def self_links(xs: Optional[list[dict[str, Any]]]):
            return [link for link in xs if link.get('@rel') == 'self'] if xs is not None else None

        data["extracted_link_self"] = extract(data, ["link", self_links, 0, "@href"])
        return data


@dataclass
class Feed:
    _is_abstract = True
    url: str

    def __init__(self, url: str):
        if self._is_abstract:
            raise RuntimeError("Abstract class instantiation.")
        self.url = url

    def __init_subclass__(cls):
        cls._is_abstract = False


@dataclass
class DeadFeed(Feed):
    def __init__(self, url: str):
        super().__init__(url)


@dataclass
class LiveFeed(Feed):
    @dataclass
    class Entry:
        id: str
        title: str
        updated: datetime

    id: str
    title: str
    updated: datetime
    entries: list[Entry]

    def __init__(self, url: str, title: str, id: str, updated: datetime, entries: list[Entry]):
        super().__init__(url)
        self.title = title
        self.id = id
        self.updated = updated
        self.entries = entries

    @classmethod
    def from_atom_feed(cls, url: str, feed: AtomFeed) -> Feed:
        entries = [cls.Entry(e.id, e.title, e.updated) for e in feed.entries]
        return LiveFeed(url, feed.title, feed.id, feed.updated, entries)

    @classmethod
    def from_rss2_feed(cls, url: str, feed: RSS2Feed) -> Feed:
        entries = [cls.Entry(e.id, e.title, e.updated) for e in feed.entries]
        return LiveFeed(url, feed.title, feed.id, feed.updated, entries)


async def get_feed(url: str) -> Feed:
    try:
        feed = parse_url(url)
        match feed:
            case AtomFeed():
                return LiveFeed.from_atom_feed(url, cast(AtomFeed, feed))
            case RSS2Feed():
                return LiveFeed.from_rss2_feed(url, cast(RSS2Feed, feed))
            case None:
                return await parse_as_dead(url, )
    except Exception as e:
        print(e)
        return DeadFeed(url)


async def parse_url(url: str) -> AtomFeed | RSS2Feed | None:
    parsers = [parse_as_atom, parse_as_rss2]
    try:
        response = await asyncio.to_thread(requests.get, url, params={})
        data = xmltodict.parse(response.content)
        feed = (parser(url, data) for parser in parsers)
        return next((f for f in feed if f is not None), None)
    except Exception as e:
        print(e)
        return None


def parse_as_atom(_: str, data: dict[Any, Any]) -> AtomFeed | None:
    try:
        print("Attempting to parse as Atom")
        extracted = AtomFeed.extract_nested_fields(data["feed"])
        return AtomFeed.model_validate(extracted)
    except Exception as e:  # TODO: Tighten exception type
        print(e)
        return None

def parse_as_rss2(_: str, data: dict[Any, Any]) -> RSS2Feed | None:
    try:
        print("Attempting to parse as RSS 2.0")
        extracted = RSS2Feed.extract_nested_fields(data["rss"]["channel"])
        return RSS2Feed.model_validate(extracted)
    except Exception as e:  # TODO: Tighten exception type
        print(e)
        return None


async def parse_as_dead(url: str, _: dict[Any, Any]) -> DeadFeed:
    print("Attempting to parse as Dead")
    return DeadFeed(url)
