package frontends.cli.connect_four

import algorithm.Player
import frontends.cli.CliFrontend
import games.Game
import games.connect_four.C4Action
import games.connect_four.C4State
import games.connect_four.ConnectFourRules

class ConnectFourCli : CliFrontend<C4State, C4Action>(
    Game(
        state = Array(6) { Array(7) { null } },
        depth = 1,
        ConnectFourRules()
    )
) {
    override fun isInputValid(input: String): Boolean {
        return input.trim().toIntOrNull() in 0..6
    }

    override fun extractInput(input: String): C4Action {
        return input.trim().toInt()
    }

    override fun printInstruction() {
        println("Column number of your next action: ")
    }

    override fun printState(state: C4State) {
        println("  0   1   2   3   4   5   6")
        state.forEachIndexed { _, line ->
            println("${line.map { it?.let { playerToChar(it) } ?: "  " }}")
        }
        println()
    }

    override fun playerToChar(player: Player): String {
        return when (player) {
            Player.Min -> "\uD83D\uDFE1"
            Player.Max -> "\uD83D\uDD34"
        }
    }
}
