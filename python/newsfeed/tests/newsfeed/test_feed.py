import logging
from pathlib import Path
from typing import cast
from unittest.mock import patch, Mock, AsyncMock, MagicMock

import aiohttp
import pytest

from newsfeed.feed import parse_feed, AtomFeed, RSS2Feed, UnparsableFeed
from newsfeed.logging_utils import configure_logging, shutdown_logging


def strip_margin(s: str) -> str:
    lines = s.splitlines()
    indent = lines[1].index("|")
    stripped = [lines[0]] + [line[indent + 1 :] for line in lines[1:]]
    return "\n".join(stripped)


def mocked_response(content: str) -> aiohttp.ClientSession:
    mock_session = MagicMock()
    mock_response = AsyncMock()

    mock_session.__aenter__.return_value = mock_session
    mock_session.get.return_value = mock_response
    mock_response.__aenter__.return_value = mock_response
    mock_response.text = AsyncMock(return_value=content)

    return mock_session


@pytest.mark.asyncio
async def test_parse_feed_given_atom_feed_then_returns_atom():
    # Given
    url = "stub-url"
    content = strip_margin(
        f"""<?xml version="1.0" encoding="utf-8"?>
           |<feed xmlns="http://www.w3.org/2005/Atom">
           |
           |  <id>urn:uuid:aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa</id>
           |  <title>feed-title</title>
           |  <updated>2001-02-03T04:05:06Z</updated>
           |
           |  <entry>
           |    <id>urn:uuid:11111111-1111-1111-1111-111111111111</id>
           |    <title>entry-1-title</title>
           |    <updated>2001-01-01T00:00:00Z</updated>
           |  </entry>
           |
           |  <entry>
           |    <id>urn:uuid:22222222-2222-2222-2222-222222222222</id>
           |    <title>entry-2-title</title>
           |    <updated>2002-02-02T00:00:00Z</updated>
           |  </entry>
           |
           |</feed>
           |"""
    )

    with patch("newsfeed.feed.aiohttp.ClientSession", return_value=mocked_response(content)):
        # When
        actual = await parse_feed(url)

        # Then
        assert isinstance(actual, AtomFeed)
        atom_feed = cast(AtomFeed, actual)
        assert atom_feed.title == "feed-title"
        assert [entry.title for entry in atom_feed.entries] == ["entry-1-title", "entry-2-title"]


@pytest.mark.asyncio
async def test_parse_feed_given_rss2_feed_then_returns_rss2():
    # Given
    content = strip_margin(
        f"""<rss version="2.0">
           |    <channel>
           |        <title>feed-title</title>
           |        <link>feed-link</link>
           |        <description>feed-description</description>
           |        <item>
           |            <title>item-1-title</title>
           |            <link>item-1-link</link>
           |            <description>item-1-description</description>
           |        </item>
           |        <item>
           |            <title>item-2-title</title>
           |            <link>item-2-link</link>
           |            <description>item-2-description</description>
           |        </item>
           |    </channel>
           |</rss>
           |"""
    )

    with patch("newsfeed.feed.aiohttp.ClientSession", return_value=mocked_response(content)):
        # When
        actual = await parse_feed("stub-url")

        # Then
        assert isinstance(actual, RSS2Feed)
        rss2_feed = cast(RSS2Feed, actual)
        assert rss2_feed.title == "feed-title"
        assert rss2_feed.link == "feed-link"
        assert rss2_feed.description == "feed-description"
        assert [item.title for item in rss2_feed.items] == ["item-1-title", "item-2-title"]


@pytest.mark.asyncio
async def test_parse_feed_given_invalid_feed_content_then_returns_unparsable_feed():
    # Given
    content = strip_margin(
        f"""<html>This is not a feed</html>
           |"""
    )

    with patch("newsfeed.feed.aiohttp.ClientSession", return_value=mocked_response(content)):
        # When
        actual = await parse_feed("stub-url")

        # Then
        assert isinstance(actual, UnparsableFeed)

@pytest.mark.asyncio
async def test_parse_feed_given_failing_feed_then_parses():

    logging.basicConfig(level='DEBUG')
    # Given
    url = "stub-url"
    content = Path("../test-data/failing-file-1.xml").read_text()

    with patch("newsfeed.feed.aiohttp.ClientSession", return_value=mocked_response(content)):
        # When
        actual = await parse_feed(url)

        # Then
        print(actual)
        assert isinstance(actual, RSS2Feed)
        rss2_feed = cast(RSS2Feed, actual)

