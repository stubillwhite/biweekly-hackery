from unittest import skip

from click.testing import CliRunner

from newsfeed.__main__ import check


@skip("Test not working yet")
def test_greet_cli():
    runner = CliRunner()
    result = runner.invoke(check, ["Europe/Istanbul"])
    assert result.exit_code == 0
    assert "Hello, Istanbul!" in result.output
