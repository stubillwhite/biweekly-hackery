import logging
import logging.config
from os import path


def configure_logging() -> None:
    logging.config.fileConfig(path.normpath('logging.conf'))


def shutdown_logging() -> None:
    logging.shutdown()
