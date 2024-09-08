package frontends.cli.tic_tac_toe

import algorithm.Player
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.table.Borders
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import frontends.cli.CliFrontend
import games.Game
import games.tic_tac_toe.T3Action
import games.tic_tac_toe.T3State
import games.tic_tac_toe.TicTacToeRules

class TicTacToeCli : CliFrontend<T3State, T3Action>(
    Game(
        state = Array(3) { Array(3) { null } },
        depth = Int.MAX_VALUE,
        rules = TicTacToeRules()
    )
) {
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
        val terminal = Terminal()
        terminal.println(table {
            cellBorders = Borders.NONE
            header {
                row {
                    style = TextColors.cyan
                    cells("", "0", "1", "2")
                }
            }
            body {
                state.forEachIndexed { index, line ->
                    row {
                        cell('a'.plus(index)) {
                            cellBorders = Borders.NONE
                        }
                        line.forEachIndexed { columnNumber, player ->
                            cell(player?.let { playerToChar(player) } ?: "") {
                                if (columnNumber == 1 && index == 1) {
                                    cellBorders = Borders.ALL
                                }
                            }
                        }
                        if (index == 1) {
                            cellBorders = Borders.TOP_BOTTOM
                        }
                    }
                }
                column(0) {
                    style = TextColors.cyan
                }
                column(2) {
                    cellBorders = Borders.LEFT_RIGHT
                }
            }
        })
        terminal.println()
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
