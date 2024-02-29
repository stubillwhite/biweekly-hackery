package mastermind.internal

import mastermind.internal.domain.*

class HumanCodeBreaker extends CodeBreaker {

  override def guess(previousGuesses: Seq[Round]): Code = {
    print("Enter guess : ")

    val guessString = Console.in.readLine().trim

    val codePegs = guessString
      .split(" ")
      .map(x => CodePeg(Color.withName(x)))

    Code(codePegs: _*)
  }
}
