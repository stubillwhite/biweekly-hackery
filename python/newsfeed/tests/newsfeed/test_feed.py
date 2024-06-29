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

    stub_response = Mock()
    stub_response.content = content

    with patch("requests.get", return_value=stub_response):
        # When
        actual = await get_feed(url)

        # Then
        assert isinstance(actual, LiveFeed)
        assert actual.title == "feed-title"
        assert actual.url == url
