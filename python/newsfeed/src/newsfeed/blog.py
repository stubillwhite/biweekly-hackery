from abc import ABC
from dataclasses import dataclass
from typing import cast

from newsfeed.feed import RSS2Feed, AtomFeed, parse_feed, UnparsableFeed, UnfetchableFeed, UnreadableFeed


@dataclass
class Blog(ABC):
    @dataclass
    class Item:
        title: str

    url: str
    items: list[Item]


@dataclass
class DeadBlog(Blog):
    def __init__(self, url: str, reason: str):
        self.url = url
        self.items = []
        self.reason = reason


@dataclass
class LiveBlog(Blog):
    def __init__(self, url: str, format: str, items: list[Blog.Item]):
        self.url = url
        self.format = format
        self.items = items


def from_atom_feed(url: str, feed: AtomFeed) -> LiveBlog:
    items = [Blog.Item(title=e.title) for e in feed.entries]
    return LiveBlog(url, "atom", items)


def from_rss2_feed(url: str, feed: RSS2Feed) -> LiveBlog:
    items = [Blog.Item(e.title) for e in feed.items]
    return LiveBlog(url, "rss-2.0", items)


async def get_blog(url: str) -> LiveBlog | DeadBlog:
    feed = await parse_feed(url)
    match feed:
        case AtomFeed():
            return from_atom_feed(url, cast(AtomFeed, feed))
        case RSS2Feed():
            return from_rss2_feed(url, cast(RSS2Feed, feed))
        case UnparsableFeed():
            return DeadBlog(url, "unparseable")
        case UnfetchableFeed():
            return DeadBlog(url, "unfetchable")
        case UnreadableFeed():
            return DeadBlog(url, "timeout")
