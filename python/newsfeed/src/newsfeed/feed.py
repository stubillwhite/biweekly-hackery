import requests
import asyncio
from dataclasses import dataclass
from datetime import datetime
import xmltodict

import requests
from pydantic import BaseModel, Field, AliasPath
from requests import Response
from requests.auth import HTTPBasicAuth
from pydantic_xml import BaseXmlModel, RootXmlModel, attr, element, wrapped


class AtomFeedEntry(BaseXmlModel, extra="ignore"):
    title: str
    link: str
    id: str
    updated: datetime
    summary: str


class AtomFeed(BaseXmlModel, extra="ignore"):
    title: str
    link: str = Field(alias=AliasPath("link", 0, "@href")) # TODO: Should be self
    updated: datetime
    id: str
    # entry: list[AtomFeedEntry] = element(tag="entry")


@dataclass
class Feed:
    _is_abstract = True
    url: str

    # TODO: Take URL parameter here
    def __init__(self):
        if self._is_abstract:
            raise RuntimeError("Abstract class instantiation.")

    def __init_subclass__(cls):
        cls._is_abstract = False


@dataclass
class DeadFeed(Feed):
    def __init__(self, url: str):
        super().__init__()
        self.url = url


@dataclass
class LiveFeed(Feed):
    title: str
    id: str
    updated: datetime

    def __init__(self,
                 url: str,
                 title: str,
                 id: str,
                 updated: datetime):
        super().__init__()
        self.url = url
        self.title = title
        self.id = id
        self.updated = updated

    @classmethod
    def from_atom_feed(cls, feed: AtomFeed) -> Feed:
        return LiveFeed("feed.link", feed.title, feed.id, feed.updated)


async def get_feed(url: str) -> Feed:
    try:
        response = await asyncio.to_thread(requests.get, url)
        data = xmltodict.parse(response.content)
        print(data["feed"]["link"])
        feed = AtomFeed.model_validate(data["feed"])
        return LiveFeed.from_atom_feed(feed)
    except Exception as e:
        print(e)
        return DeadFeed(url)
