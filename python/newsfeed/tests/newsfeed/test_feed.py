from typing import cast
from unittest import mock
from unittest.mock import patch, Mock, AsyncMock, MagicMock

import aiohttp
import pytest

from newsfeed.feed import parse_feed, AtomFeed, RSS2Feed, UnparsableFeed, hackery


def strip_margin(s: str) -> str:
    lines = s.splitlines()
    indent = lines[1].index("|")
    stripped = [lines[0]] + [line[indent + 1:] for line in lines[1:]]
    return "\n".join(stripped)


async def create_resp(status_code=200, resp_data=None):
    resp = AsyncMock(status_code=status_code)
    resp.text = resp_data
    return resp


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

    # stub_response = Mock()
    # stub_response.content = content

    mock_session = MagicMock()
    mock_response = AsyncMock()

    mock_session.__aenter__.return_value = mock_session
    mock_session.get.return_value = mock_response
    mock_response.text = AsyncMock(return_value=content)

    # foo = create_resp(200, content)

    with patch("newsfeed.feed.aiohttp.ClientSession", return_value=mock_session):
        # When
        # actual = await parse_feed(url)
        actual = await hackery()

        # Then
        print(actual)
        # assert isinstance(actual, AtomFeed)
        # atom_feed = cast(AtomFeed, actual)
        # assert atom_feed.title == "feed-title"


async def test_parse_feed_given_rss2_feed_then_returns_rss2():
    # Given
    content = strip_margin(
        f"""<rss version="2.0">
           |    <channel>
           |        <title>feed-title</title>
           |        <link>feed-link</link>
           |        <description>feed-description</description>
           |    </channel>
           |</rss>
           |"""
    )

    stub_response = Mock()
    stub_response.content = content

    with patch("requests.get", return_value=stub_response):
        # When
        actual = await parse_feed("stub-url")

        # Then
        assert isinstance(actual, RSS2Feed)
        rss2_feed = cast(RSS2Feed, actual)
        assert rss2_feed.title == "feed-title"
        assert rss2_feed.link == "feed-link"


async def test_parse_feed_given_invalid_feed_content_then_returns_unparsable_feed():
    # Given
    content = strip_margin(
        f"""<html>This is not a feed</html>
           |"""
    )

    stub_response = Mock()
    stub_response.content = content

    with patch("requests.get", return_value=stub_response):
        # When
        actual = await parse_feed("stub-url")

        # Then
        assert isinstance(actual, UnparsableFeed)
