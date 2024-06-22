from typing import cast
from unittest.mock import patch, Mock

from newsfeed.app import get_feed
from newsfeed.feed import LiveFeed


def strip_margin(s: str) -> str:
    lines = s.splitlines()
    indent = lines[1].index("|")
    stripped = [lines[0]] + [line[indent + 1:] for line in lines[1:]]
    return "\n".join(stripped)


async def test_get_feed():
    # Given
    url = "stub-url"
    content = strip_margin(
        f"""<?xml version="1.0" encoding="utf-8"?>
           |<feed xmlns="http://www.w3.org/2005/Atom">
           |
           |  <title>feed-title</title>
           |  <link href="feed-link"/>
           |  <updated>2001-02-03T04:05:06Z</updated>
           |  <author>
           |    <name>author-name</name>
           |  </author>
           |  <id>urn:uuid:aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa</id>
           |
           |  <entry>
           |    <title>entry-1-title</title>
           |    <link href="entry-1-link"/>
           |    <id>urn:uuid:11111111-1111-1111-1111-111111111111</id>
           |    <updated>2001-01-01T00:00:00Z</updated>
           |    <summary>entry-1-summary</summary>
           |  </entry>
           |
           |  <entry>
           |    <title>entry-2-title</title>
           |    <link href="entry-2-link"/>
           |    <id>urn:uuid:22222222-2222-2222-2222-222222222222</id>
           |    <updated>2002-02-02T00:00:00Z</updated>
           |    <summary>entry-2-summary</summary>
           |  </entry>
           |
           |</feed>
           |"""
    )

    stub_response = Mock()
    stub_response.content = content

    with (
        patch("requests.get", return_value=stub_response)
    ):
        # When
        actual = cast(LiveFeed, await get_feed(url))

        # Then
        assert actual.title == "feed-title"
        assert actual.url == url

