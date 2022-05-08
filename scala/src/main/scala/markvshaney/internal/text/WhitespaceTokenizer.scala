package markvshaney.internal.text

import markvshaney.internal.domain.Tokenizer

object WhitespaceTokenizer extends Tokenizer {

  override def tokenize(lines: Iterator[String]): Iterator[String] = {
    lines.mkString(" ")
      .replaceAll("\n", " ")
      .split("\\s+")
      .iterator
      .filter(_.matches("\\S+"))
  }
}
