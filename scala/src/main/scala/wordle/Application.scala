package wordle

import wordle.internal.{ComputerPlayer, HumanPlayer, TextDisplay, Puzzle}
import io.Source

object Application {

  def main(args: Array[String]): Unit = {
    val display = TextDisplay()

    val wordList = loadWordList()
    val puzzle = Puzzle.newPuzzle(loadWordList(), new ComputerPlayer(wordList))

    val states = Iterator.iterate(puzzle)(_.nextState()).takeWhile(_ != null)
    states.foreach(display.display)
  }

  private def loadWordList(): List[String] = {
    Source.fromResource("wordle/corncob-caps.txt").getLines().toList
  }
}
