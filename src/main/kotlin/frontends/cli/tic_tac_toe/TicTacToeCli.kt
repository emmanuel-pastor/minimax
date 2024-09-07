package frontends.cli.tic_tac_toe

import algorithm.Player
import frontends.cli.CliFrontend
import games.Game
import games.tic_tac_toe.T3Action
import games.tic_tac_toe.T3State
import games.tic_tac_toe.TicTacToeRules

object TicTacToeCli : CliFrontend<T3State, T3Action>(Game(
    state = Array(3) { Array(3) { null } },
    depth = Int.MAX_VALUE,
    rules = TicTacToeRules()
)) {
    private val inputRegex = "^\\s*([a-c])\\s*,?\\s*([0-2])\\s*\$"

    override fun isInputValid(input: String): Boolean {
        val regex = Regex(inputRegex)
        return regex.matches(input)
    }

    override fun extractInput(input: String): T3Action {
        val regex = Regex(inputRegex)
        val matchResult = regex.find(input)

        requireNotNull(matchResult)
        return matchResult.let {
            val (firstNumber, secondNumber) = it.destructured
            firstNumber.first().minus('a'.code).code to secondNumber.toInt()
        }
    }

    override fun printState(state: T3State) {
        println("   0   1   2")
        state.forEachIndexed { index, line ->
            println("${'a'.plus(index)} ${line.map { it?.let { playerToChar(it) } ?: "  " }}")
        }
        println()
    }

    override fun printInstruction() {
        println("Coordinates of your next action [row,column]: ")
    }

    override fun playerToChar(player: Player): String {
        return when (player) {
            Player.Min -> "⭕"
            Player.Max -> "❌"
        }
    }
}
