package cheatinghangman

import cheatinghangman.internal.{ComputerPlayer, Game, HumanPlayer, TextDisplay}
import cheatinghangman.internal.domain.*

import scala.annotation.tailrec
import scala.io.Source
import scala.util.Random

object CheatingHangman {

  def main(args: Array[String]): Unit = {

    val wordList = readWordList()
    val word = Random.shuffle(wordList).head

    val game = Game(
      word,
      wordList.filter(x => x.length == word.length),
      Set(),
      ComputerPlayer(wordList)
    )

    playGame(game, new TextDisplay())
  }

  @tailrec
  private def playGame(game: Game, display: Display): Unit = {
    display.display(game)

    if (!game.isWon && !game.isLost) {
      playGame(game.nextState(), display)
    }
  }

  private def readWordList(): List[String] = {
    Source.fromResource("cheatinghangman/corncob-caps.txt").getLines().toList
  }
}
