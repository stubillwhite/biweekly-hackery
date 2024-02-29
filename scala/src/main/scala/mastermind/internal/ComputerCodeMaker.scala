package mastermind.internal

import mastermind.internal.domain.*
import mastermind.internal.domain.CodePegs.*

import scala.util.Random

class ComputerCodeMaker extends CodeMaker {

  private val codePegs = List(Red, Blue, Yellow, Green, White, Black)

  override def makeCode(): Code = {
    Array.fill[CodePeg](4)(randomCodePeg)
  }

  private def randomCodePeg: CodePeg = {
    codePegs(Random.nextInt(codePegs.length))
  }
}
