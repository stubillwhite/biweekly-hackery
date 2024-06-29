import asyncio
from typing import cast

from newsfeed.feed import get_feed, LiveFeed, DeadFeed


async def app() -> None:
    for url in ['https://martinfowler.com/feed.atom', "https://diogocastro.com/blog/atom.xml"]:
        feed = await get_feed(url)

        match feed:
            case LiveFeed():
                live_feed: LiveFeed = cast(LiveFeed, feed)
                print(live_feed.title)
                print(live_feed.url)
                for entry in live_feed.entries:
                    print(f"    {entry.updated}")
                    print(f"    {entry.title}")
                    print(f"    {entry.summary}")
                    print()

            case DeadFeed():
                print(feed)


def main() -> None:
    asyncio.run(app())


if __name__ == "__main__":
    main()
