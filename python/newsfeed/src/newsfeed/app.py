import requests
import asyncio
from dataclasses import dataclass
from datetime import datetime
import xmltodict

import requests
from pydantic import BaseModel, Field
from requests import Response
from requests.auth import HTTPBasicAuth
from pydantic_xml import BaseXmlModel, RootXmlModel, attr, element, wrapped

from newsfeed.feed import get_feed


async def app() -> None:
    print(await get_feed("https://diogocastro.com/blog/atom.xml"))
    # print(await get_feed("http://snooogle.com"))
    # print(await get_feed("http://snooogle.com"))
    print("Hello")


def main() -> None:
    asyncio.run(app())

if __name__ == '__main__':
    main()