package com.emmanuel.pastor.simplesmartapps

typealias CFAction = Int

class ConnectFourRules : GameRules<State, CFAction> {
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
    }

    private fun isTerminalWithHorizontal(state: State): Boolean {
        for(x in 0..5) {
            val winnerSymbol = state[x].firstOrNull { it.isNotBlank() }
            if(winnerSymbol == null) continue

            for(y in 0..4) {
                var streak = 0
                for (i in 0..3) {
                    if(state[x][y] == winnerSymbol) streak++
                }
                if (streak == 4) return true
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