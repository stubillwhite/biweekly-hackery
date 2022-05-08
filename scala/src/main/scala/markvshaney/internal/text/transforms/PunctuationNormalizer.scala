package markvshaney.internal.text.transforms

import markvshaney.internal.domain.{TextTransform, TokenStream}

/** Transforms the token stream by normalising fancy punctuation into simple ASCII */
object PunctuationNormalizer extends TextTransform {
  val transform: TokenStream => TokenStream = (tokens: Iterator[String]) => {
    tokens.map { token =>
      token
        .replaceAll("[“”]", "\"")
        .replaceAll("[‘’]", "'")
    }
  }
}
