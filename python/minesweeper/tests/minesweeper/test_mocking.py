from unittest.mock import patch, PropertyMock


# Playing with the mock framework


class Foo:
    def __init__(self):
        self.my_attr = "my_attr value"

    def my_func(self) -> str:
        return "my_func value"


class Bar:

    def __init__(self, foo: Foo):
        self._foo = foo

    def get_foo_attr(self) -> str:
        return self._foo.my_attr

    def get_foo_func(self) -> str:
        return self._foo.my_func()


def test_func_with_single_value():
    with (
        patch("tests.minesweeper.test_mocking.Foo") as mock_foo,
        patch("tests.minesweeper.test_mocking.Foo.my_func", return_value="a"),
    ):
        bar = Bar(mock_foo)
        assert bar.get_foo_func() == "a"


def test_func_with_multiple_values():
    with (
        patch("tests.minesweeper.test_mocking.Foo") as mock_foo,
        patch.object(mock_foo, "my_func", side_effect=["a", "b", "c"]),
    ):
        bar = Bar(mock_foo)
        assert bar.get_foo_func() == "a"
        assert bar.get_foo_func() == "b"
        assert bar.get_foo_func() == "c"


def test_attr_with_single_value():
    with (
        patch("tests.minesweeper.test_mocking.Foo") as mock_foo,
        patch("tests.minesweeper.test_mocking.Foo.my_attr", "mocked value"),
    ):
        bar = Bar(mock_foo)
        assert bar.get_foo_attr() == "mocked value"


def test_attr_with_multiple_values():
    with (
        patch("tests.minesweeper.test_mocking.Foo") as mock_foo  # fmt: skip
    ):
        # Patching the side effect of an attribute must be done on the type of the mocked
        # class, not the type of the original class
        type(mock_foo).my_attr = PropertyMock(side_effect=["a", "b", "c"])
        bar = Bar(mock_foo)
        assert bar.get_foo_attr() == "a"
        assert bar.get_foo_attr() == "b"
        assert bar.get_foo_attr() == "c"
