package com.emmanuel.pastor.simplesmartapps

import kotlin.math.max
import kotlin.math.min

typealias State = Array<Array<String>>
typealias Coords = Pair<Int, Int>

sealed class Player(val symbol: String) {
    data object Max : Player("X")
    data object Min : Player("O")
}

fun nextPlayer(state: State): Player {
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

val terminalStates = arrayOf(
    arrayOf(0 to 0, 0 to 1, 0 to 2), //first row
    arrayOf(1 to 0, 1 to 1, 1 to 2), //second row
    arrayOf(2 to 0, 2 to 1, 2 to 2), //third row
    arrayOf(0 to 0, 1 to 0, 2 to 0), //first column
    arrayOf(0 to 1, 1 to 1, 2 to 1), //second column
    arrayOf(0 to 2, 1 to 2, 2 to 2), //third column
    arrayOf(0 to 0, 1 to 1, 2 to 2), //diagonal top-left to bottom-right
    arrayOf(0 to 2, 1 to 1, 2 to 0) //diagonal top-right to bottom-left
)

fun isTerminal(state: State): Boolean {
    terminalStates.forEach { terminalState ->
        var minCounter = 0
        var maxCounter = 0
        terminalState.forEach { (row, column) ->
            if (state[row][column] == Player.Min.symbol) {
                minCounter++
            } else if (state[row][column] == Player.Max.symbol) {
                maxCounter++
            }
        }

        if (max(minCounter, maxCounter) == 3) return true
    }

    val turnsPlayed = state.sumOf { row ->
        row.count { cell ->
            cell.isNotBlank()
        }
    }

    return turnsPlayed > 8
}

fun possibleActions(state: State): Array<Coords> {
    val actions = mutableListOf<Coords>()
    state.forEachIndexed { x, row ->
        row.forEachIndexed { y, cell ->
            if (cell.isBlank()) {
                actions.add(x to y)
            }
        }
    }

    return actions.toTypedArray()
}

fun nextState(state: State, action: Coords, player: Player): State {
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
    }.also { it[row][column] = player.symbol }
}

fun valueOf(state: State): Int {
    terminalStates.forEach { terminalState ->
        var minCounter = 0
        var maxCounter = 0
        terminalState.forEach { (row, column) ->
            if (state[row][column] == Player.Min.symbol) {
                minCounter++
            } else if (state[row][column] == Player.Max.symbol) {
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

/*
    X will be the MAX player
    O is the starting player (playing on odd turns, first turn being 1)
    @return the best reachable value from the given state
 */
fun minimax(state: State): Int {
    if (isTerminal(state)) return valueOf(state)

    val possibleActions = possibleActions(state)
    return when (nextPlayer(state)) {
        Player.Max -> {
            var value = Int.MIN_VALUE
            possibleActions.forEach { action ->
                value = max(value, minimax(nextState(state, action, Player.Max)))
            }
            value
        }

        Player.Min -> {
            var value = Int.MAX_VALUE
            possibleActions.forEach { action ->
                value = min(value, minimax(nextState(state, action, Player.Min)))
            }
            value
        }
    }
}

fun main() {
    /*
        Next turn: player O
        Expected next action: (1,2)
        Expected next state:
            XX_
            OOO
            XO_
     */
    val initialState = arrayOf(
        arrayOf("O", "X", ""),
        arrayOf("O", "X", ""),
        arrayOf("X", "O", "O")
    )

    val actions = possibleActions(initialState)
    val plays = mutableListOf<Pair<Coords, Int>>()
    actions.forEach { action ->
        val value = minimax(nextState(initialState, action, Player.Max))
        plays.add(action to value)
    }

    println("Best play: ${plays.maxBy { (_, value) -> value }}")
}