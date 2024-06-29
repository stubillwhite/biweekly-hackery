from typing import Optional, Any

import requests
import asyncio
from dataclasses import dataclass
from datetime import datetime
import xmltodict

from pydantic import Field, AliasPath, AliasChoices
from pydantic_xml import BaseXmlModel, RootXmlModel, attr, element, wrapped

from src.newsfeed.utils import extract


class AtomFeed(BaseXmlModel, extra="ignore"):
    class Entry(BaseXmlModel, extra="ignore"):
        id: str
        title: str
        updated: datetime
        summary: Optional[str] = None

    id: str
    title: str
    updated: datetime
    entries: list[Entry] = element(alias="entry", tag="entry")
    link: Optional[str] = Field(alias="link_self")

    @classmethod
    def extract_nested_fields(cls, data: dict[Any, Any]) -> dict[Any, Any]:
        def self_links(xs: Optional[list[dict[str, Any]]]):
            return [link for link in xs if link.get('@rel') == 'self'] if xs is not None else None

        data["link_self"] = extract(data, ["link", self_links, 0, "@href"])
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
        summary: Optional[str]

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
        entries = [cls.Entry(e.id, e.title, e.updated, e.summary) for e in feed.entries]
        return LiveFeed(url, feed.title, feed.id, feed.updated, entries)


async def get_feed(url: str) -> Feed:
    try:
        response = await asyncio.to_thread(requests.get, url)
        data = xmltodict.parse(response.content)
        extracted = AtomFeed.extract_nested_fields(data["feed"])
        feed = AtomFeed.model_validate(extracted)
        return LiveFeed.from_atom_feed(url, feed)
    except Exception as e:
        print(e)
        return DeadFeed(url)
