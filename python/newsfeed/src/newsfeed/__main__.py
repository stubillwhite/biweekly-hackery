import sys

import click

from newsfeed.app import main, check_subscriptions
from newsfeed.logging_utils import configure_logging, shutdown_logging
from typing import Callable


def common_options(function: Callable) -> Callable:
    function = click.option("--log", default="info")(function)
    return function


@click.command("check")
@common_options
def check() -> None:
    configure_logging()
    check_subscriptions()
    shutdown_logging()
    sys.exit(0)


if __name__ == "__main__":
    main()
