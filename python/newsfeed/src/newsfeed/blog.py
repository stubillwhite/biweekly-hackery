from dataclasses import dataclass
from datetime import datetime
from typing import cast

from newsfeed.feed import RSS2Feed, AtomFeed, parse_feed
from abc import ABC, abstractmethod


@dataclass
class Blog(ABC):
    @dataclass
    class Item:
        title: str

    url: str
    items: list[Item]


@dataclass
class DeadBlog(Blog):
    def __init__(self, url: str):
        self.url = url
        self.items = []


@dataclass
class LiveBlog(Blog):
    def __init__(self, url: str, items: list[Blog.Item]):
        self.url = url
        self.items = items


def from_atom_feed(url: str, feed: AtomFeed) -> LiveBlog:
    items = [Blog.Item(e.title) for e in feed.entries]
    return LiveBlog(url, items)


def from_rss2_feed(url: str, feed: RSS2Feed) -> LiveBlog:
    items = [Blog.Item(e.title) for e in feed.entries]
    return LiveBlog(url, items)


async def get_blog(url: str) -> LiveBlog | DeadBlog:
    try:
        feed = await parse_feed(url)
        print(feed)
        match feed:
            case AtomFeed():
                return from_atom_feed(url, cast(AtomFeed, feed))
            case RSS2Feed():
                return from_rss2_feed(url, cast(RSS2Feed, feed))
            case None:
                return DeadBlog(url)

    except Exception as e:
        return DeadBlog(url)
