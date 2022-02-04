package wordle.internal

import wordle.internal.domain.{GuessFeedback, Player}

object HumanPlayer {
  def apply(): HumanPlayer = {
    new HumanPlayer
  }
}

class HumanPlayer extends Player {

  def guessWord(guessFeedback: Seq[GuessFeedback]): String = {
    print("Enter guess: ")
    Console.in.readLine().trim
  }
}
