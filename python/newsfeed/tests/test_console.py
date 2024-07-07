from click.testing import CliRunner

from newsfeed.__main__ import greet


def test_greet_cli():
    runner = CliRunner()
    result = runner.invoke(greet, ["Europe/Istanbul"])
    assert result.exit_code == 0
    assert "Hello, Istanbul!" in result.output
