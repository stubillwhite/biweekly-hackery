from newsfeed.utils import extract


def test_extract_given_value_exists_in_dict_then_returns_value() -> None:
    assert extract({"a": {"b": {"c": 23}}}, ["a", "b", "c"]) == 23


def test_extract_given_value_does_not_exist_in_dict_then_returns_none() -> None:
    assert extract({"a": {"b": {"c": 23}}}, ["a", "d", "e"]) is None


def test_extract_given_value_exists_in_list_then_returns_value() -> None:
    assert extract({"a": {"b": [23, 42]}}, ["a", "b", 1]) == 42


def test_extract_given_value_out_of_range_in_list_then_returns_value() -> None:
    assert extract({"a": {"b": [23, 42]}}, ["a", "b", 2]) == None


def test_extract_given_value_exists_with_callable_then_returns_value() -> None:
    def last(xs):
        return xs[-1]

    assert extract({"a": {"b": [23, 42]}}, ["a", "b", last]) == 42
