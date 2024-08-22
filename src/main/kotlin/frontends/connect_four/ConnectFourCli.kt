package com.emmanuel.pastor.simplesmartapps.frontends.connect_four

import com.emmanuel.pastor.simplesmartapps.algorithm.GameResult
import com.emmanuel.pastor.simplesmartapps.algorithm.Player
import com.emmanuel.pastor.simplesmartapps.games.Game
import com.emmanuel.pastor.simplesmartapps.games.connect_four.C4Action
import com.emmanuel.pastor.simplesmartapps.games.connect_four.C4State
import com.emmanuel.pastor.simplesmartapps.games.connect_four.ConnectFourRules
import kotlin.random.Random

private fun isInputValidC4(input: String): Boolean {
    return input.trim().toIntOrNull() in 0..6
}

private fun extractInputC4(input: String): C4Action {
    return input.trim().toInt()
}

private fun printWinner(result: GameResult) {
    when (result) {
        GameResult.Draw -> println("The game ended in a DRAW")
        GameResult.Max -> println("MAX player won")
        GameResult.Min -> println("MIN player won")
    }
}

fun printC4State(state: C4State) {
    println("   0  1  2  3  4  5  6")
    state.forEachIndexed { index, line ->
        println("${'a'.plus(index)} ${line.map { it?.let { playerToChar(it) } ?: " " }}")
    }
    println()
}

fun playerToChar(player: Player): String {
    return when (player) {
        Player.Min -> "O"
        Player.Max -> "X"
    }
}

fun main() {
    val numberOfColumns = 7
    val numberOfRows = 6
    val initialState: C4State = Array(numberOfRows) { Array(numberOfColumns) { null } }
    val difficulty = 0.4

    val game = Game(initialState, 1, ConnectFourRules())

    while (game.result == null) {
        printC4State(game.state)
        when (game.nextPlayer) {
            Player.Min -> {
                val random = Random.nextFloat()
                if (random < difficulty) {
                    println("OPTIMAL")
                    game.playOptimal()
                } else {
                    println("RANDOM")
                    game.playRandom()
                }
            }

            Player.Max -> {
                var input: String
                do {
                    println("Column number of your next action: ")
                    input = readln()
                } while (!isInputValidC4(input))

                game.play(extractInputC4(input))
            }
        }
    }
    printC4State(game.state)
    printWinner(game.result!!)
}