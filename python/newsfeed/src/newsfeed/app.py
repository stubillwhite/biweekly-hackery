import asyncio
import logging
from pathlib import Path
from typing import cast

import xmltodict
from colored import colored, Fore, Style

from newsfeed.blog import get_blog, LiveBlog, DeadBlog
from newsfeed.utils import flatten

subscriptions = [
    "http://www.martinfowler.com/bliki/bliki.atom",
    "https://scala.libhunt.com/newsletter/feed",
    "https://blog.softwaremill.com/feed",
    "http://googlescholar.blogspot.com/feeds/posts/default",
    "http://multithreaded.stitchfix.com/feed.xml",
    "http://techblog.netflix.com/feeds/posts/default",
    "https://medium.com/feed/netflix-techblog",
    "http://labs.spotify.com/feed/",
    "https://blog.beachgeek.co.uk/index.xml",
]


async def app() -> None:
    blogs = [await get_blog(url) for url in subscriptions]

    for blog in blogs:

        match blog:
            case LiveBlog():
                live_blog = cast(LiveBlog, blog)
                print(f"{live_blog.url}")
                for item in live_blog.items[:3]:
                    # print(f"    {item.title}\n    {item.link}\n{'\n'.join(wrap(description, width=120, initial_indent='        ', subsequent_indent="        "))}")
                    print(item.title)

            case DeadBlog() as e:
                print(e)

        print()

# https://superfastpython.com/asyncio-gather-timeout/
async def run_check_subscriptions() -> None:
    subscriptions = read_blogs()[:300]
    blogs = [get_blog(url) for url in subscriptions]

    blogs = await asyncio.gather(*blogs)
    count = {}

    for blog in blogs:
        match blog:
            case LiveBlog() as live_blog:
                count['live'] = count.get('live', 0) + 1
                print(f"{Fore.green}Live{Style.reset}: {live_blog.format} {live_blog.url}")

            case DeadBlog() as dead_blog:
                count[dead_blog.reason] = count.get(dead_blog.reason, 0) + 1
                print(f"{Fore.red}Dead ({dead_blog.reason}){Style.reset}: {dead_blog.url}")

    print(count)


def check_subscriptions() -> None:
    asyncio.run(run_check_subscriptions())
    # run_check_subscriptions()


def main() -> None:
    logging.basicConfig(level='DEBUG')
    asyncio.run(app())


def read_blogs() -> list[str]:
    return Path(
        '/Users/stubillwhite/Dev/my-stuff/biweekly-hackery/python/newsfeed/feedly.opml').read_text().splitlines()


if __name__ == "__main__":
    main()
