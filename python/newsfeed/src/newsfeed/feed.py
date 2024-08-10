import logging
import ssl
from datetime import datetime
from typing import Optional, Any

import aiohttp
import certifi
import xmltodict
from aiohttp import TCPConnector
from pydantic import Field
from pydantic_xml import BaseXmlModel, element

from src.newsfeed.utils import extract

logger = logging.getLogger(__name__)


class RSS2Feed(BaseXmlModel, extra="ignore"):
    class Item(BaseXmlModel, extra="ignore"):
        title: Optional[str] = None
        link: Optional[str] = None
        description: Optional[str] = None

    title: str
    link: str
    description: Optional[str] = None # Should be mandatory, but many blogs do not include
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
            return [link for link in xs if link.get("@rel") == "self"] if xs is not None else None

        data["extracted_link_self"] = extract(data, ["link", self_links, 0, "@href"])
        return data


async def hackery():
    ssl_context = ssl.create_default_context(cafile=certifi.where())

    async with aiohttp.ClientSession(connector=TCPConnector(ssl=ssl_context), trust_env=True) as session:
        print(session)
        async with session.get("http://url.com", timeout=5) as response:
            print(response)
            content = await response.text()
            print(content)



async def parse_feed(url: str) -> AtomFeed | RSS2Feed | UnparsableFeed | UnfetchableFeed:
    parsers = [parse_as_atom, parse_as_rss2, parse_as_unparsable]

    ssl_context = ssl.create_default_context(cafile=certifi.where())

    try:
        logger.info(f"Attempting to retrieve '{url}'")
        async with aiohttp.ClientSession(connector=TCPConnector(ssl=ssl_context), trust_env=True) as session:
            print(session)
            async with session.get(url, timeout=5) as response:
                logger.info(f"Status code: {response.status}")

                content = await response.text()
                data = xmltodict.parse(content)
                feed = (parser(url, data) for parser in parsers)
                return next(f for f in feed if f is not None)
    # except ReadTimeout as e:
    #     logger.debug("Failed to retrieve feed before timeout", exc_info=e)
    #     return parse_as_unreadable(url, {})

    except Exception as e:
        print(e)
        logger.info("Failed to retrieve feed", exc_info=e)
        return parse_as_unfetchable(url, {})


def parse_as_atom(_: str, data: dict[Any, Any]) -> AtomFeed | None:
    try:
        extracted = AtomFeed.extract_complex_fields(data["feed"])
        return AtomFeed.model_validate(extracted)
    except Exception as e:  # TODO: Tighten exception type
        logger.debug(f"Failed to parse as Atom with errors:", exc_info=e)
        logger.info(f"Failed to parse as Atom")
        return None


def parse_as_rss2(_: str, data: dict[Any, Any]) -> RSS2Feed | None:
    try:
        extracted = RSS2Feed.extract_complex_fields(data["rss"]["channel"])
        return RSS2Feed.model_validate(extracted)
    except Exception as e:  # TODO: Tighten exception type
        print(extracted['description'])
        logger.info(f"Failed to parse as RSS 2.0 with errors:", exc_info=e)
        logger.info(f"Failed to parse as RSS 2.0")
        return None


def parse_as_unparsable(url: str, data: dict[Any, Any]) -> UnparsableFeed:
    logger.info("Treating as unparsable")
    return UnparsableFeed()


def parse_as_unfetchable(url: str, data: dict[Any, Any]) -> UnfetchableFeed:
    logger.info("Treating as unfetchable")
    return UnfetchableFeed()


def parse_as_unreadable(url: str, data: dict[Any, Any]) -> UnreadableFeed:
    logger.info("Treating as unreadable")
    return UnreadableFeed()
