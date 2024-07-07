import click

from newsfeed.app import main, check_subscriptions


@click.command("check")
def check() -> None:
    check_subscriptions()


if __name__ == "__main__":
    main()
