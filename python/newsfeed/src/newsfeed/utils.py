from functools import reduce
from typing import Any, Union, Mapping, Sequence


def extract(d: Union[Mapping, Sequence], ks: list[Any]) -> Any:
    def get_value(data, k):
        if callable(k):
            return k(data)
        else:
            match data:
                case None:
                    return None
                case dict():
                    return data.get(k)
                case list():
                    return data[k] if k < len(data) else None

    return reduce(get_value, ks, d)