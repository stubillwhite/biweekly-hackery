package markvshaney.internal.model

import scala.util.Random

trait RandomNumberProvider {
  
  /**
   * Returns a pseudorandom int value between 0 (inclusive) and the specified value (exclusive).
   *
   * @param n The specified (exclusive) maximum value.
   * @return The random number.
   */
  def nextInt(n: Int): Int
}
