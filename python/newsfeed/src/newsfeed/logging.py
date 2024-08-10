import logging
import logging.config
from os import path

# https://www.zopatista.com/python/2019/05/11/asyncio-logging/
def configure_logging() -> None:
    logging.config.fileConfig(path.normpath("logging.conf"))


def shutdown_logging() -> None:
    logging.shutdown()
