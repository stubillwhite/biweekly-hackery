package cheatinghangman.internal

import cheatinghangman.internal.domain.*
import cheatinghangman.internal.TextDisplay.*

object TextDisplay {
  private val States = List(
    """







    """,
    """






      ━━━━━━━━━━━━
    """,
    """
       ┃
       ┃
       ┃
       ┃
       ┃
       ┃
      ━┻━━━━━━━━━━
    """,
    """
       ┏━━━━━━━
       ┃
       ┃
       ┃
       ┃
       ┃
      ━┻━━━━━━━━━━
    """,
    """
       ┏━━━━━━━┑
       ┃       │
       ┃
       ┃
       ┃
       ┃
      ━┻━━━━━━━━━━
    """,
    """
       ┏━━━━━━━┑
       ┃       │
       ┃       O
       ┃
       ┃
       ┃
      ━┻━━━━━━━━━━
    """,
    """
       ┏━━━━━━━┑
       ┃       │
       ┃       O
       ┃       │
       ┃
       ┃
      ━┻━━━━━━━━━━
    """,
    """
       ┏━━━━━━━┑
       ┃       │
       ┃       O
       ┃      /│
       ┃
       ┃
      ━┻━━━━━━━━━━
    """,
    """
       ┏━━━━━━━┑
       ┃       │
       ┃       O
       ┃      /│\
       ┃
       ┃
      ━┻━━━━━━━━━━
    """,
    """
       ┏━━━━━━━┑
       ┃       │
       ┃       O
       ┃      /│\
       ┃      /
       ┃
      ━┻━━━━━━━━━━
    """,
    """
       ┏━━━━━━━┑
       ┃       │
       ┃       O
       ┃      /│\
       ┃      / \
       ┃
      ━┻━━━━━━━━━━
    """
  )
}

class TextDisplay extends Display {
  def display(game: Game): Unit = {
    println(s"Word: ${game.word}, number of alternatives: ${game.candidateWords.length}")

    val guessedCharacters = game.knownLetters.map(_.getOrElse("-")).mkString(" ")

    println
    println(States(10 - game.remainingLives))
    println(s"Word:    ${guessedCharacters}")
    println(s"Guesses: ${game.guessedLetters.toList.sorted.mkString(", ")}")

    if (game.isWon) {
      println("You win!")
    }
    else if (game.isLost) {
      println(s"You lose! I was thinking of ${game.word}")
    }
  }
}
