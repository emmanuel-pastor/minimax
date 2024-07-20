package com.emmanuel.pastor.simplesmartapps

class ConnectFourRules: GameRules<State, Action> {
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
        TODO("Not yet implemented")
    }

    override fun possibleActions(state: State): Array<Action> {
        val actions = mutableListOf<Action>()
        val excludedColumns = hashSetOf<Int>()
        for (x in state.indices.reversed()) {
            state[x].forEachIndexed { y, cell ->
                if (!excludedColumns.contains(y) && cell.isBlank()) {
                    actions.add(x to y)
                    excludedColumns.add(y)
                }
            }

            if(excludedColumns.size == state.first().size) break
        }

        return actions.toTypedArray()
    }

    override fun nextState(state: State, action: Action): State {
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