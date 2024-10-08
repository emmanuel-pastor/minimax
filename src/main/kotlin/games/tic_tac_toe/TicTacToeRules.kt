package games.tic_tac_toe

import algorithm.GameResult
import algorithm.GameRules
import algorithm.Player

typealias T3State = Array<Array<Player?>>
typealias T3Action = Pair<Int, Int>

class TicTacToeRules : GameRules<T3State, T3Action> {
    private companion object {
        const val ROWS_COUNT = 3
        const val COLUMNS_COUNT = 3
    }

    private val terminalStates = arrayOf(
        arrayOf(0 to 0, 0 to 1, 0 to 2), //first row
        arrayOf(1 to 0, 1 to 1, 1 to 2), //second row
        arrayOf(2 to 0, 2 to 1, 2 to 2), //third row
        arrayOf(0 to 0, 1 to 0, 2 to 0), //first column
        arrayOf(0 to 1, 1 to 1, 2 to 1), //second column
        arrayOf(0 to 2, 1 to 2, 2 to 2), //third column
        arrayOf(0 to 0, 1 to 1, 2 to 2), //diagonal top-left to bottom-right
        arrayOf(0 to 2, 1 to 1, 2 to 0) //diagonal top-right to bottom-left
    )

    override fun nextPlayer(state: T3State): Player {
        val turnsPlayed = state.sumOf { row ->
            row.count { cell: Player? ->
                cell != null
            }
        }

        require(turnsPlayed <= 8) {
            "The grid is full. Cannot determine the next player."
        }

        return if (turnsPlayed.mod(2) == 0) {
            Player.Min
        } else {
            Player.Max
        }
    }

    override fun gameResult(state: T3State): GameResult? {
        terminalStates.forEach { terminalState ->
            var minCounter = 0
            var maxCounter = 0
            terminalState.forEach { (row, column) ->
                if (state[row][column] == Player.Min) {
                    minCounter++
                } else if (state[row][column] == Player.Max) {
                    maxCounter++
                }
            }

            if (minCounter == 3) return GameResult.Min
            if (maxCounter == 3) return GameResult.Max
        }

        val turnsPlayed = state.sumOf { row ->
            row.count { cell: Player? ->
                cell != null
            }
        }

        return if (turnsPlayed > COLUMNS_COUNT * ROWS_COUNT - 1) GameResult.Draw else null
    }

    override fun possibleActions(state: T3State): Array<T3Action> {
        val actions = mutableListOf<T3Action>()
        state.forEachIndexed { x, row ->
            row.forEachIndexed { y, cell ->
                if (cell == null) {
                    actions.add(x to y)
                }
            }
        }

        return actions.toTypedArray()
    }

    override fun nextState(state: T3State, action: T3Action): T3State {
        val (row, column) = action
        require(row in 0 until ROWS_COUNT && column in 0 until COLUMNS_COUNT) {
            "Coordinates ($row, $column) are out of the grid"
        }
        require(state[row][column] == null) {
            "Cannot perform this action. The cell ($row, $column) is not empty."
        }

        return Array(state.size) { x ->
            Array(state[x].size) { y ->
                state[x][y]
            }
        }.also { it[row][column] = nextPlayer(state) }
    }

    override fun valueOf(state: T3State): Int {
        terminalStates.forEach { terminalState ->
            var minCounter = 0
            var maxCounter = 0
            terminalState.forEach { (row, column) ->
                if (state[row][column] == Player.Min) {
                    minCounter++
                } else if (state[row][column] == Player.Max) {
                    maxCounter++
                }
            }

            if (minCounter == 3) return -1
            if (maxCounter == 3) return 1
        }

        val turnsPlayed = state.sumOf { row ->
            row.count { cell: Player? ->
                cell != null
            }
        }

        return if (turnsPlayed == ROWS_COUNT * COLUMNS_COUNT) 0 else throw IllegalArgumentException("Cannot determine the value of a non terminal state.")
    }
}