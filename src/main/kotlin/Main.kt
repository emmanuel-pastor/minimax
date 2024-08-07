package com.emmanuel.pastor.simplesmartapps

import kotlin.random.Random

fun isInputValidT3(input: String): Boolean {
    val regex = Regex("^\\s*[0-2]\\s*,\\s*[0-2]\\s*\$")
    return regex.matches(input)
}

fun extractNumbersT3(input: String): T3Action {
    val regex = Regex("^\\s*([0-2])\\s*,\\s*([0-2])\\s*$")
    val matchResult = regex.find(input)

    requireNotNull(matchResult)
    return matchResult.let {
        val (firstNumber, secondNumber) = it.destructured
        firstNumber.toInt() to secondNumber.toInt()
    }
}

fun isInputValidC4(input: String): Boolean {
    return input.trim().toIntOrNull() in 0..6
}

fun extractInputC4(input: String): C4Action {
    return input.trim().toInt()
}

fun printWinner(result: GameResult) {
    when(result) {
        GameResult.Draw -> println("The game ended in a DRAW")
        GameResult.Max -> println("MAX player won")
        GameResult.Min -> println("MIN player won")
    }
}

fun main() {
    val initialState = arrayOf(
        arrayOf(" ", " ", " ", " ", " ", " ", " "),
        arrayOf(" ", " ", " ", " ", " ", " ", " "),
        arrayOf(" ", " ", " ", " ", " ", " ", " "),
        arrayOf(" ", " ", " ", " ", " ", " ", " "),
        arrayOf(" ", " ", " ", " ", " ", " ", " "),
        arrayOf(" ", " ", " ", " ", " ", " ", " ")
    )
    val difficulty = 0.4

    val game = Game(initialState, 1, ConnectFourRules())

    while (game.result == null) {
        game.printState()
        when(game.nextPlayer) {
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
    game.printState()
    printWinner(game.result!!)
}