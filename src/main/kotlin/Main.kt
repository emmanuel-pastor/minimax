package com.emmanuel.pastor.simplesmartapps

import kotlin.random.Random

fun isInputValid(input: String): Boolean {
    val regex = Regex("^\\s*[0-2]\\s*,\\s*[0-2]\\s*\$")
    return regex.matches(input)
}

fun extractNumbers(input: String): Action {
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
        arrayOf(" ", " ", " "),
        arrayOf(" ", " ", " "),
        arrayOf(" ", " ", " ")
    )
    val difficulty = 0.7

    val game = Game(initialState, Int.MAX_VALUE, TicTacToeRules())

    while (!game.isOver) {
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
                    println("Coordinates of your next action: ")
                    input = readln()
                } while (!isInputValid(input))

                game.play(extractNumbers(input))
            }
        }
    }
    game.printState()
}