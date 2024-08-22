package com.emmanuel.pastor.simplesmartapps.frontends.tic_tac_toe

import com.emmanuel.pastor.simplesmartapps.algorithm.GameResult
import com.emmanuel.pastor.simplesmartapps.algorithm.Player
import com.emmanuel.pastor.simplesmartapps.games.Game
import com.emmanuel.pastor.simplesmartapps.games.tic_tac_toe.T3Action
import com.emmanuel.pastor.simplesmartapps.games.tic_tac_toe.T3State
import com.emmanuel.pastor.simplesmartapps.games.tic_tac_toe.TicTacToeRules
import kotlin.random.Random

private fun isInputValidT3(input: String): Boolean {
    val regex = Regex("^\\s*[a-c]\\s*,?\\s*[0-2]\\s*\$")
    return regex.matches(input)
}

private fun extractNumbersT3(input: String): T3Action {
    val regex = Regex("^\\s*([a-c])\\s*,?\\s*([0-2])\\s*$")
    val matchResult = regex.find(input)

    requireNotNull(matchResult)
    return matchResult.let {
        val (firstNumber, secondNumber) = it.destructured
        firstNumber.first().minus('a'.code).code to secondNumber.toInt()
    }
}

private fun printWinner(result: GameResult) {
    when (result) {
        GameResult.Draw -> println("The game ended in a DRAW")
        GameResult.Max -> println("MAX player won")
        GameResult.Min -> println("MIN player won")
    }
}

private fun printT3State(state: T3State) {
    println("   0  1  2")
    state.forEachIndexed { index, line ->
        println("${'a'.plus(index)} ${line.map { it?.let { playerToChar(it) } ?: " " }}")
    }
    println()
}

private fun playerToChar(player: Player): String {
    return when (player) {
        Player.Min -> "O"
        Player.Max -> "X"
    }
}

fun main() {
    val numberOfColumns = 3
    val numberOfRows = 3
    val initialState: T3State = Array(numberOfRows) { Array(numberOfColumns) { null } }
    val difficulty = 0.4

    val game = Game(initialState, Int.MAX_VALUE, TicTacToeRules())

    while (game.result == null) {
        printT3State(game.state)
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
                    println("Coordinates of your next action [row,column]: ")
                    input = readln()
                } while (!isInputValidT3(input))

                game.play(extractNumbersT3(input))
            }
        }
    }
    printT3State(game.state)
    printWinner(game.result!!)
}