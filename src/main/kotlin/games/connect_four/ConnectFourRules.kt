package games.connect_four

import algorithm.GameResult
import algorithm.GameRules
import algorithm.Player

typealias C4State = Array<Array<Player?>>
typealias C4Action = Int

class ConnectFourRules : GameRules<C4State, C4Action> {
    private companion object {
        const val COLUMNS_COUNT = 7
        const val ROWS_COUNT = 6
    }

    override fun nextPlayer(state: C4State): Player {
        val turnsPlayed = state.sumOf { row ->
            row.count { cell: Player? ->
                cell != null
            }
        }

        require(turnsPlayed < COLUMNS_COUNT * ROWS_COUNT) {
            "The grid is full. Cannot determine the next player."
        }

        return if (turnsPlayed.mod(2) == 0) {
            Player.Min
        } else {
            Player.Max
        }
    }

    override fun gameResult(state: C4State): GameResult? {
        val horizontalResult = isTerminalWithHorizontal(state)
        if (horizontalResult != null) return horizontalResult

        val verticalResult = isTerminalWithVertical(state)
        if (verticalResult != null) return verticalResult

        val diagonalNegativeSlopeResult = isTerminalWithNegativeSlopeDiagonal(state)
        if (diagonalNegativeSlopeResult != null) return diagonalNegativeSlopeResult

        val diagonalPositiveSlopeResult = isTerminalWithPositiveSlopeDiagonal(state)
        if (diagonalPositiveSlopeResult != null) return diagonalPositiveSlopeResult

        return isTerminalWithFullGrid(state)
    }

    private fun isTerminalWithFullGrid(state: C4State): GameResult? {
        return if (state.sumOf { line ->
                line.count { cell: Player? ->
                    cell == null
                }
            } == 0) GameResult.Draw else null
    }

    private fun isTerminalWithHorizontal(state: C4State): GameResult? {
        for (x in 0 until ROWS_COUNT) {
            for (y in 0..3) {
                var streakMinPlayer = 0
                var streakMaxPlayer = 0
                for (i in 0..3) {
                    val cell = state[x][y + i]
                    if (cell == Player.Min) {
                        streakMinPlayer++
                        streakMaxPlayer = 0
                    } else if (cell == Player.Max) {
                        streakMaxPlayer++
                        streakMinPlayer = 0
                    }
                }
                if (streakMinPlayer == 4) return GameResult.Min
                if (streakMaxPlayer == 4) return GameResult.Max
            }
        }
        return null
    }

    private fun isTerminalWithVertical(state: C4State): GameResult? {
        for (y in 0 until COLUMNS_COUNT) {
            for (x in 0..2) {
                var streakMinPlayer = 0
                var streakMaxPlayer = 0
                for (i in 0..3) {
                    val cell = state[x + i][y]
                    if (cell == Player.Min) {
                        streakMinPlayer++
                        streakMaxPlayer = 0
                    } else if (cell == Player.Max) {
                        streakMaxPlayer++
                        streakMinPlayer = 0
                    }
                }
                if (streakMinPlayer == 4) return GameResult.Min
                if (streakMaxPlayer == 4) return GameResult.Max
            }
        }
        return null
    }

    private fun isTerminalWithNegativeSlopeDiagonal(state: C4State): GameResult? {
        for (x in 0..2) {
            for (y in 0..3) {
                var streakMinPlayer = 0
                var streakMaxPlayer = 0
                for (i in 0..3) {
                    val cell = state[x + i][y + i]
                    if (cell == Player.Min) {
                        streakMinPlayer++
                        streakMaxPlayer = 0
                    } else if (cell == Player.Max) {
                        streakMaxPlayer++
                        streakMinPlayer = 0
                    }
                }
                if (streakMinPlayer == 4) return GameResult.Min
                if (streakMaxPlayer == 4) return GameResult.Max
            }
        }
        return null
    }

    private fun isTerminalWithPositiveSlopeDiagonal(state: C4State): GameResult? {
        for (x in 3 until ROWS_COUNT) {
            for (y in 0..3) {
                var streakMinPlayer = 0
                var streakMaxPlayer = 0
                for (i in 0..3) {
                    val cell = state[x - i][y + i]
                    if (cell == Player.Min) {
                        streakMinPlayer++
                        streakMaxPlayer = 0
                    } else if (cell == Player.Max) {
                        streakMaxPlayer++
                        streakMinPlayer = 0
                    }
                }
                if (streakMinPlayer == 4) return GameResult.Min
                if (streakMaxPlayer == 4) return GameResult.Max
            }
        }
        return null
    }

    override fun possibleActions(state: C4State): Array<C4Action> {
        val actions = mutableListOf<C4Action>()

        for (y in 0 until COLUMNS_COUNT) {
            for (x in (ROWS_COUNT - 1) downTo 0) {
                if (state[x][y] == null) {
                    actions.add(y)
                    break
                }
            }
        }

        return actions.toTypedArray()
    }

    override fun nextState(state: C4State, action: C4Action): C4State {
        require(action in 0 until COLUMNS_COUNT) {
            "The column number $action is out of the grid. Valid column numbers is included in [0,6]."
        }
        require(state.first()[action] == null) {
            "Column $action is full. You cannot add any token in this column."
        }

        val nextState = Array(state.size) { x ->
            Array(state[x].size) { y ->
                state[x][y]
            }
        }
        for (x in (ROWS_COUNT - 1) downTo 0) {
            if (nextState[x][action] == null) {
                nextState[x][action] = nextPlayer(state)
                break
            }
        }

        return nextState
    }

    override fun valueOf(state: C4State): Int {
        val nextPlayer = nextPlayer(state)

        val isOverButNotDraw = with(gameResult(state)) {
            this != null && this != GameResult.Draw
        }
        return when (nextPlayer) {
            Player.Max -> {
                if (isOverButNotDraw) {
                    //Min won the previous Turn
                    Int.MIN_VALUE
                } else {
                    scorePosition(state, nextPlayer) - scorePosition(state, Player.Min)
                }
            }

            Player.Min -> {
                if (isOverButNotDraw) {
                    //Max won the previous turn
                    Int.MAX_VALUE
                } else {
                    -1 * (scorePosition(state, nextPlayer) - scorePosition(state, Player.Max))
                }
            }
        }
    }

    private fun scorePosition(state: C4State, nextPlayer: Player): Int {
        var score = 0

        //Score center column
        val centerArray = Array(state.size) { x ->
            state[x][3]
        }
        val centerCount = centerArray.count { it == nextPlayer }
        score += centerCount * 6

        //Score horizontal
        for (row in state) {
            for (y in 0..3) {
                val window = row.sliceArray(y..y + 3)
                score += evaluateWindow(window, nextPlayer)
            }
        }

        //Score vertical
        for (y in 0 until ROWS_COUNT) {
            val column = Array(state.size) { x ->
                state[x][y]
            }
            for (x in 0..2) {
                val window = column.sliceArray(x..x + 3)
                score += evaluateWindow(window, nextPlayer)
            }
        }

        //Score positive slope diagonal
        for (x in 0..2) {
            for (y in 0..3) {
                val window = Array(4) { i ->
                    state[x + i][y + i]
                }
                score += evaluateWindow(window, nextPlayer)
            }
        }

        //Score negative slope diagonal
        for (x in 3 until ROWS_COUNT) {
            for (y in 0..3) {
                val window = Array(4) { i ->
                    state[x - i][y + i]
                }
                score += evaluateWindow(window, nextPlayer)
            }
        }

        return score
    }

    private fun evaluateWindow(window: Array<Player?>, nextPlayer: Player): Int {
        var score = 0
        val opponent = when (nextPlayer) {
            Player.Max -> Player.Min
            Player.Min -> Player.Max
        }

        when {
            window.count { it == nextPlayer } == 4 -> {
                score += 100
            }

            window.count { it == nextPlayer } == 3 && window.count { it == null } == 1 -> {
                score += 10
            }

            window.count { it == nextPlayer } == 2 && window.count { it == null } == 2 -> {
                score += 5
            }
        }

        if (window.count { it == opponent } == 3 && window.count { it == null } == 1) {
            score -= 80
        }

        return score
    }
}