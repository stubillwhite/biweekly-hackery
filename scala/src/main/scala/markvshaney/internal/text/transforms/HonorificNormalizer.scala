package markvshaney.internal.text.transforms

import markvshaney.internal.domain.{TextTransform, TokenStream}

/** Transforms the token stream by removing punctuation from honorifics */
object HonorificNormalizer extends TextTransform {
  val transform: TokenStream => TokenStream = (tokens: Iterator[String]) => {
    tokens.map { token =>
      token
        .replaceAll("Mr\\.", "Mr")
        .replaceAll("Mrs\\.", "Mrs")
        .replaceAll("Dr\\.", "Dr")
    }
  }
}
