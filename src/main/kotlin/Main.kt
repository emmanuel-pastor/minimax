package com.emmanuel.pastor.simplesmartapps

import com.emmanuel.pastor.simplesmartapps.Minimax.isTerminal
import com.emmanuel.pastor.simplesmartapps.Minimax.minimax
import com.emmanuel.pastor.simplesmartapps.Minimax.nextPlayer
import com.emmanuel.pastor.simplesmartapps.Minimax.nextState
import com.emmanuel.pastor.simplesmartapps.Minimax.possibleActions

fun State.print() {
    this.forEach { line ->
        println(line.contentToString())
    }
    println()
}

class TicTacToe(private val state: State) {
    val isOver get() = isTerminal(state)

    fun nextOptimalPlay(): Coords {
        val actions = possibleActions(state)
        val plays = mutableListOf<Pair<Coords, Int>>()
        val nextPlayer = nextPlayer(state)
        actions.forEach { action ->
            val value = minimax(nextState(state, action, nextPlayer))
            plays.add(action to value)
        }

        return when (nextPlayer) {
            Player.Max -> {
                plays.maxBy { (_, value) -> value }.first
            }

            Player.Min -> {
                plays.minBy { (_, value) -> value }.first
            }
        }
    }

    fun play(action: Coords) {
        val (line, column) = action
        require(state[line][column].isBlank()) {
            "Cannot play on cell ($line, $column). This cell is not empty."
        }
        require(!isTerminal(state)) {
            "Cannot play. The game is already over."
        }

        state[line][column] = nextPlayer(state).symbol
    }

    fun printState() = state.print()
}

fun isInputValid(input: String): Boolean {
    val regex = Regex("^\\s*[0-2]\\s*,\\s*[0-2]\\s*\$")
    return regex.matches(input)
}

fun extractNumbers(input: String): Coords {
    val regex = Regex("^\\s*([0-2])\\s*,\\s*([0-2])\\s*$")
    val matchResult = regex.find(input)

    requireNotNull(matchResult)
    return matchResult.let {
        val (firstNumber, secondNumber) = it.destructured
        firstNumber.toInt() to secondNumber.toInt()
    }
}

fun main() {
    val initialState = arrayOf(
        arrayOf("", "", ""),
        arrayOf("", "", ""),
        arrayOf("", "", "")
    )

    val game = TicTacToe(initialState)

    var nextPlayer: Player = Player.Min
    while (!game.isOver) {
        game.printState()
        when(nextPlayer) {
            Player.Min -> {
                game.play(game.nextOptimalPlay())
                nextPlayer = Player.Max
            }
            Player.Max -> {
                var input: String
                do {
                    println("Coordinates of your next action: ")
                    input = readln()
                } while (!isInputValid(input))

                game.play(extractNumbers(input))
                nextPlayer = Player.Min
            }
        }
    }
    game.printState()
}