package hexed

import assertk.assert
import assertk.assertions.isEqualTo
import hexed.HexEd.solutionPartOne
import hexed.HexEd.solutionPartTwo
import kotlin.test.Test
import kotlin.test.assertNotNull

class HexEdTest {

    @Test
    fun solutionPartOneGivenExampleInputThenReturnsEndDistance() {
        assert(solutionPartOne("ne,ne,ne")).isEqualTo(3)
        assert(solutionPartOne("ne,ne,sw,sw")).isEqualTo(0)
        assert(solutionPartOne("ne,ne,s,s")).isEqualTo(2)
        assert(solutionPartOne("se,sw,se,sw,sw")).isEqualTo(3)
    }

    @Test
    fun solutionPartOneGivenProblemInputThenReturnsEndDistance() {
        assert(solutionPartOne(readInput("hexed/input.txt"))).isEqualTo(664)
    }

    @Test
    fun solutionPartTwoGivenProblemInputThenReturnsEndDistance() {
        assert(solutionPartTwo(readInput("hexed/input.txt"))).isEqualTo(1447)
    }

    private fun readInput(path: String): String {
        return Thread.currentThread().contextClassLoader.getResource(path)!!.readText()
    }
}
