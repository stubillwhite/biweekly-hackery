import asyncio
import logging
from datetime import datetime
from typing import Optional, Any

import requests
import xmltodict
from pydantic import Field
from pydantic_xml import BaseXmlModel, element
from requests import ReadTimeout

from src.newsfeed.utils import extract

logger = logging.getLogger(__name__)


class RSS2Feed(BaseXmlModel, extra="ignore"):
    class Item(BaseXmlModel, extra="ignore"):
        title: Optional[str]
        link: Optional[str]
        description: Optional[str]

    title: str
    link: str
    description: str
    items: list[Item] = element(alias="item", tag="item")

    @classmethod
    def extract_complex_fields(cls, data: dict[Any, Any]) -> dict[Any, Any]:
        return data


class UnparsableFeed(BaseXmlModel, extra="ignore"):
    pass

    @classmethod
    def extract_complex_fields(cls, data: dict[Any, Any]) -> dict[Any, Any]:
        return data


class UnfetchableFeed(BaseXmlModel, extra="ignore"):
    pass

    @classmethod
    def extract_complex_fields(cls, data: dict[Any, Any]) -> dict[Any, Any]:
        return data


class UnreadableFeed(BaseXmlModel, extra="ignore"):
    pass

    @classmethod
    def extract_complex_fields(cls, data: dict[Any, Any]) -> dict[Any, Any]:
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
    def extract_complex_fields(cls, data: dict[Any, Any]) -> dict[Any, Any]:
        def self_links(xs: Optional[list[dict[str, Any]]]):
            return [link for link in xs if link.get('@rel') == 'self'] if xs is not None else None

        data["extracted_link_self"] = extract(data, ["link", self_links, 0, "@href"])
        return data


async def parse_feed(url: str) -> AtomFeed | RSS2Feed | UnparsableFeed | UnfetchableFeed:
    parsers = [parse_as_atom, parse_as_rss2, parse_as_unparsable]

    def get():
        return requests.get(url, timeout=2)

    try:
        response = await asyncio.to_thread(get)
        data = xmltodict.parse(response.content)
        feed = (parser(url, data) for parser in parsers)
        return next(f for f in feed if f is not None)
    except ReadTimeout as e:
        logger.debug("Failed to retrieve feed before timeout", exc_info=e)
        return parse_as_unreadable(url, {})

    except Exception as e:
        logger.debug("Failed to retrieve feed", exc_info=e)
        return parse_as_unfetchable(url, {})


def parse_as_atom(_: str, data: dict[Any, Any]) -> AtomFeed | None:
    try:
        logger.debug("Attempting to parse as Atom")
        extracted = AtomFeed.extract_complex_fields(data["feed"])
        return AtomFeed.model_validate(extracted)
    except Exception as e:  # TODO: Tighten exception type
        logger.debug(f"Failed to parse as Atom", exc_info=e)
        return None


def parse_as_rss2(_: str, data: dict[Any, Any]) -> RSS2Feed | None:
    try:
        logger.debug("Attempting to parse as RSS 2.0")
        extracted = RSS2Feed.extract_complex_fields(data["rss"]["channel"])
        return RSS2Feed.model_validate(extracted)
    except Exception as e:  # TODO: Tighten exception type
        logger.debug(f"Failed to parse as RSS 2.0", exc_info=e)
        return None


def parse_as_unparsable(url: str, data: dict[Any, Any]) -> UnparsableFeed:
    logger.debug("Treating as unparsable")
    return UnparsableFeed()


def parse_as_unfetchable(url: str, data: dict[Any, Any]) -> UnfetchableFeed:
    logger.debug("Treating as unfetchable")
    return UnfetchableFeed()

def parse_as_unreadable(url: str, data: dict[Any, Any]) -> UnreadableFeed:
    logger.debug("Treating as unreadable")
    return UnreadableFeed()
