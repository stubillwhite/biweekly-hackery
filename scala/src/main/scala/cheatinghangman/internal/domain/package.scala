package cheatinghangman.internal

package object domain {

  trait Player {
    def guessCharacter(word: List[Option[Char]],
                       guessedLetters: Set[Char],
                       remainingLives: Int): Char
  }

  trait Display {
    def display(game: Game): Unit
  }

}
