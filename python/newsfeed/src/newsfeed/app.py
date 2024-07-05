import asyncio
from textwrap import wrap
from typing import cast

from newsfeed.blog import get_blog, LiveBlog, DeadBlog


async def app() -> None:
    for url in [
        "http://www.martinfowler.com/bliki/bliki.atom",
        "https://scala.libhunt.com/newsletter/feed",
        "https://blog.softwaremill.com/feed",
        "http://googlescholar.blogspot.com/feeds/posts/default",
        "http://multithreaded.stitchfix.com/feed.xml",
        "http://techblog.netflix.com/feeds/posts/default",
        "http://labs.spotify.com/feed/",
        "https://blog.beachgeek.co.uk/index.xml",
    ]:
        blog = await get_blog(url)

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


def main() -> None:
    asyncio.run(app())


if __name__ == "__main__":
    main()
