package com.emmanuel.pastor.simplesmartapps

typealias CFAction = Int

class ConnectFourRules : GameRules<State, CFAction> {
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

        require(turnsPlayed <= 41) {
            "The grid is full. Cannot determine the next player."
        }

        return if (turnsPlayed.mod(2) == 0) {
            Player.Min
        } else {
            Player.Max
        }
    }

    override fun isTerminal(state: State): Boolean {
        return isTerminalWithHorizontal(state)
                || isTerminalWithVertical(state)
                || isTerminalWithDiagonalTopLeftToBottomRight(state)
                || isTerminalWithDiagonalTopRightToBottomLeft(state)
                || isTerminalWithFullGrid(state)
    }

    private fun isTerminalWithFullGrid(state: State): Boolean {
        return state.sumOf { line ->
            line.count { value ->
                value.isBlank()
            }
        } == 0
    }

    private fun isTerminalWithHorizontal(state: State): Boolean {
        for (x in 0..5) {
            for (y in 0..3) {
                var streakMinPlayer = 0
                var streakMaxPlayer = 0
                for (i in 0..3) {
                    val cell = state[x][y + i]
                    if (cell == Player.Min.symbol()) {
                        streakMinPlayer++
                        streakMaxPlayer = 0
                    } else if (cell == Player.Max.symbol()) {
                        streakMaxPlayer++
                        streakMinPlayer = 0
                    }
                }
                if (streakMinPlayer == 4 || streakMaxPlayer == 4) return true
            }
        }
        return false
    }

    private fun isTerminalWithVertical(state: State): Boolean {
        for (y in 0..6) {
            for (x in 0..2) {
                var streakMinPlayer = 0
                var streakMaxPlayer = 0
                for (i in 0..3) {
                    val cell = state[x + i][y]
                    if (cell == Player.Min.symbol()) {
                        streakMinPlayer++
                        streakMaxPlayer = 0
                    } else if (cell == Player.Max.symbol()) {
                        streakMaxPlayer++
                        streakMinPlayer = 0
                    }
                }
                if (streakMinPlayer == 4 || streakMaxPlayer == 4) return true
            }
        }
        return false
    }

    private fun isTerminalWithDiagonalTopLeftToBottomRight(state: State): Boolean {
        for (x in 0..2) {
            for (y in 0..3) {
                var streakMinPlayer = 0
                var streakMaxPlayer = 0
                for (i in 0..3) {
                    val cell = state[x + i][y + i]
                    if (cell == Player.Min.symbol()) {
                        streakMinPlayer++
                        streakMaxPlayer = 0
                    } else if (cell == Player.Max.symbol()) {
                        streakMaxPlayer++
                        streakMinPlayer = 0
                    }
                }
                if (streakMinPlayer == 4 || streakMaxPlayer == 4) return true
            }
        }
        return false
    }

    private fun isTerminalWithDiagonalTopRightToBottomLeft(state: State): Boolean {
        for (x in 3..5) {
            for (y in 0..3) {
                var streakMinPlayer = 0
                var streakMaxPlayer = 0
                for (i in 0..3) {
                    val cell = state[x - i][y + i]
                    if (cell == Player.Min.symbol()) {
                        streakMinPlayer++
                        streakMaxPlayer = 0
                    } else if (cell == Player.Max.symbol()) {
                        streakMaxPlayer++
                        streakMinPlayer = 0
                    }
                }
                if (streakMinPlayer == 4 || streakMaxPlayer == 4) return true
            }
        }
        return false
    }

    override fun possibleActions(state: State): Array<CFAction> {
        val actions = mutableListOf<CFAction>()

        var x = 0
        while (actions.size != 7 && x < state.size) {
            val line = state[x]
            for (y in line.indices) {
                if (line[y].isBlank()) {
                    actions.add(y)
                }

                if (actions.size == 7) break
            }

            x++
        }

        return actions.toTypedArray()
    }

    override fun nextState(state: State, action: CFAction): State {
        TODO("Not yet implemented")
    }

    override fun valueOf(state: State): Int {
        TODO("Not yet implemented")
    }

    override fun printState(state: State) {
        println("   0  1  2  3  4  5  6")
        state.forEachIndexed { index, line ->
            println("$index ${line.contentToString()}")
        }
        println()
    }
}