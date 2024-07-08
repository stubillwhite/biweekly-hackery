import logging
import logging.config
from os import path

import click

from newsfeed.app import main, check_subscriptions

def common_options(function):
    function = click.option('--log', default='info')(function)
    return function

def configure_logging():
    logging.config.fileConfig(path.normpath('logging.conf'))
    #
    # myLogger = logging.getLogger(__name__)
    #
    # msg = 'Bite Me'
    # myLogger.debug(msg)
    # myLogger.info(msg)
    # myLogger.warning(msg)
    # myLogger.error(msg)
    # myLogger.critical(msg)

    # Shut down the logger
    # logging.shutdown()

@click.command("check")
@common_options
def check(log: str) -> None:
    configure_logging()
    # logging.basicConfig(level='DEBUG')
    # logging.basicConfig(level=log)
    check_subscriptions()
    logging.shutdown()


if __name__ == "__main__":
    # configure_logging()
    main()
