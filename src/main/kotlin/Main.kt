package com.emmanuel.pastor.simplesmartapps

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