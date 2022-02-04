package wordle.internal

import wordle.internal.domain.*

import scala.annotation.tailrec
import scala.util.Random

object Puzzle {

  def newPuzzle(wordList: List[String], player: Player): Puzzle = {
    val validWords = getValidWords(wordList)
    val word = Random.shuffle(validWords).head

    Puzzle(validWords, player, word, InProgress, List())
  }

  private def getValidWords(wordList: List[String]): List[String] = {
    val hasRepeatedLetters = (s: String) => s.toList.groupBy(identity).values.exists(_.length >= 2)

    wordList
      .filter(_.length == 5)
      .filterNot(hasRepeatedLetters)
      .map(_.toUpperCase)
  }
}

case class Puzzle(wordList: List[String],
                  player: Player,
                  word: String,
                  puzzleStatus: PuzzleStatus,
                  guessFeedback: Seq[GuessFeedback]) {

  def nextState(): Puzzle = {
    puzzleStatus match {
      case InProgress =>
        val guess = nextValidGuessFromPlayer()

        val newGuessFeedback = guessFeedback ++ List(getFeedback(word, guess))
        val newPuzzleStatus =
          if (word == guess) Won
          else if (newGuessFeedback.length == 6) Lost
          else InProgress

        copy(puzzleStatus = newPuzzleStatus, guessFeedback = newGuessFeedback)

      case _ => null
    }
  }

  @tailrec
  private def nextValidGuessFromPlayer(): String = {
    val guess = player.guessWord(guessFeedback).toUpperCase

    if (wordList.contains(guess)) guess
    else nextValidGuessFromPlayer()
  }

  private def getFeedback(word: String, guess: String): GuessFeedback = {
    val letterFeedback = word.zip(guess).map {
      case (wordLetter, guessLetter) =>
        val letterStatus =
          if (wordLetter == guessLetter) Correct
          else if (word.contains(guessLetter)) IncorrectLocation
          else IncorrectLetter

        LetterFeedback(guessLetter, letterStatus)
    }

    GuessFeedback(letterFeedback)
  }
}

