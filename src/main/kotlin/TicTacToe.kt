package com.emmanuel.pastor.simplesmartapps

typealias State = Array<Array<String>>
typealias T3Action = Pair<Int, Int>

class TicTacToeRules : GameRules<State, T3Action> {
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

    private fun Player.symbol(): String = when (this) {
        Player.Max -> "X"
        Player.Min -> "O"
    }

    override fun nextPlayer(state: State): Player {
        val turnsPlayed = state.sumOf { row ->
            row.count { cell ->
                cell.isNotBlank()
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

    override fun gameResult(state: State): GameResult? {
        terminalStates.forEach { terminalState ->
            var minCounter = 0
            var maxCounter = 0
            terminalState.forEach { (row, column) ->
                if (state[row][column] == Player.Min.symbol()) {
                    minCounter++
                } else if (state[row][column] == Player.Max.symbol()) {
                    maxCounter++
                }
            }

            if (minCounter == 3) return GameResult.Min
            if (maxCounter == 3) return GameResult.Max
        }

        val turnsPlayed = state.sumOf { row ->
            row.count { cell ->
                cell.isNotBlank()
            }
        }

        return if (turnsPlayed > 8) GameResult.Draw else null
    }

    override fun possibleActions(state: State): Array<T3Action> {
        val actions = mutableListOf<T3Action>()
        state.forEachIndexed { x, row ->
            row.forEachIndexed { y, cell ->
                if (cell.isBlank()) {
                    actions.add(x to y)
                }
            }
        }

        return actions.toTypedArray()
    }

    override fun nextState(state: State, action: T3Action): State {
        val (row, column) = action
        require(row in 0..2 && column in 0..2) {
            "Coordinates ($row, $column) are out of the grid"
        }
        require(state[row][column].isBlank()) {
            "Cannot perform this action. The cell ($row, $column) is not empty."
        }

        return Array(state.size) { x ->
            Array(state[x].size) { y ->
                state[x][y]
            }
        }.also { it[row][column] = nextPlayer(state).symbol() }
    }

    override fun valueOf(state: State): Int {
        terminalStates.forEach { terminalState ->
            var minCounter = 0
            var maxCounter = 0
            terminalState.forEach { (row, column) ->
                if (state[row][column] == Player.Min.symbol()) {
                    minCounter++
                } else if (state[row][column] == Player.Max.symbol()) {
                    maxCounter++
                }
            }

            if (minCounter == 3) return -1
            if (maxCounter == 3) return 1
        }

        val turnsPlayed = state.sumOf { row ->
            row.count { cell ->
                cell.isNotBlank()
            }
        }

        return if (turnsPlayed == 9) 0 else throw IllegalArgumentException("Cannot determine the value of a non terminal state.")
    }
}

