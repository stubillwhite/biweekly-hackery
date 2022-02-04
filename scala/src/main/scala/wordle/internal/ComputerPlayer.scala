package wordle.internal

import wordle.internal.ComputerPlayer.*
import wordle.internal.domain.*

object ComputerPlayer {

  private def getValidWords(words: List[String]): List[String] = {
    val hasRepeatedLetters = (s: String) => s.toList.groupBy(identity).values.exists(_.length >= 2)

    words
      .filter(_.length == 5)
      .filterNot(hasRepeatedLetters)
      .map(_.toUpperCase)
  }

  private case class Trie(children: Map[Char, Trie] = Map(), subtreeSize: Int = 0) {
    def add(s: String): Trie = {
      if (s.isEmpty) {
        this
      }
      else {
        val trie = children.getOrElse(s.head, Trie())
        copy(children = children + (s.head -> trie.add(s.tail)), subtreeSize = subtreeSize + 1)
      }
    }
  }

  private def filterWordList(wordList: List[String], guessFeedback: GuessFeedback): List[String] = {
    guessFeedback
      .letterFeedback
      .zipWithIndex
      .foldLeft(wordList) {
        case (wordList, (LetterFeedback(letter, feedback), idx)) => {
          feedback match {
            case Correct =>
              wordList.filter(word => word.charAt(idx) == letter)

            case IncorrectLocation =>
              wordList
                .filter(word => word.contains(letter))
                .filterNot(word => word.charAt(idx) == letter)

            case IncorrectLetter =>
              wordList.filterNot(word => word.contains(letter))
          }
        }
      }
  }

  private def buildTrie(wordList: List[String]): Trie = {
    wordList.foldLeft(Trie())((t, w) => t.add(w))
  }
}

class ComputerPlayer(words: List[String]) extends Player {

  private val wordList = getValidWords(words)

  override def guessWord(guessFeedback: Seq[GuessFeedback]): String = {
    val filteredList = guessFeedback.foldLeft(wordList)((wordList, feedback) => filterWordList(wordList, feedback))
    val trie = buildTrie(filteredList)

    def mostLikelyWordFromTrie(trie: Trie): String = {
      if (trie.subtreeSize == 0) {
        ""
      }
      else {
        val ch = trie.children.maxBy { case (_, trie) => trie.subtreeSize }.head
        ch + mostLikelyWordFromTrie(trie.children(ch))
      }
    }

    mostLikelyWordFromTrie(trie)
  }
}
