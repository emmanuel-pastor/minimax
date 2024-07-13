package com.emmanuel.pastor.simplesmartapps

import kotlin.math.max

typealias State = Array<Array<String>>
typealias Coords = Pair<Int, Int>

sealed class Player(val symbol: String) {
    data object Max : Player("X")
    data object Min : Player("O")
}

fun nextPlayer(state: State): Player {
    val turnsPlayed = state.sumOf { line ->
        line.count { cell ->
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

fun isTerminal(state: State): Boolean {
    val terminalStates = arrayOf(
        arrayOf(0 to 0, 0 to 1, 0 to 2), //first line
        arrayOf(1 to 0, 1 to 1, 1 to 2), //second line
        arrayOf(2 to 0, 2 to 1, 2 to 2), //third line
        arrayOf(0 to 0, 1 to 0, 2 to 0), //first column
        arrayOf(0 to 1, 1 to 1, 2 to 1), //second column
        arrayOf(0 to 2, 1 to 2, 2 to 2), //third column
        arrayOf(0 to 0, 1 to 1, 2 to 2), //diagonal top-left to bottom-right
        arrayOf(0 to 2, 1 to 1, 2 to 0) //diagonal top-right to bottom-left
    )

    terminalStates.forEach { terminalState ->
        var minCounter = 0
        var maxCounter = 0
        terminalState.forEach { (line, column) ->
            if (state[line][column] == Player.Min.symbol) {
                minCounter++
            } else if (state[line][column] == Player.Max.symbol) {
                maxCounter++
            }
        }

        if (max(minCounter, maxCounter) == 3) return true
    }

    val turnsPlayed = state.sumOf { line ->
        line.count { cell ->
            cell.isNotBlank()
        }
    }

    return turnsPlayed > 8
}

fun possibleActions(state: State): Array<Coords> {
    val actions = mutableListOf<Coords>()
    state.forEachIndexed { x, line ->
        line.forEachIndexed { y, cell ->
            if (cell.isBlank()) {
                actions.add(x to y)
            }
        }
    }

    return actions.toTypedArray()
}

/*
    X will be the MAX player
    O is the starting player (playing on odd turns, first turn being 1)
    @return the coordinates of the next player's most optimal action
 */
fun minimax(state: State): Coords {
    return 1 to 2
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
        arrayOf("X", "X", ""),
        arrayOf("O", "O", ""),
        arrayOf("X", "O", "")
    )

    val nextAction = minimax(initialState)

    println(nextAction)
}