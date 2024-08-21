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
    when (result) {
        GameResult.Draw -> println("The game ended in a DRAW")
        GameResult.Max -> println("MAX player won")
        GameResult.Min -> println("MIN player won")
    }
}

fun printT3State(state: State) {
    println("   0  1  2")
    state.forEachIndexed { index, line ->
        println("$index ${line.map { it?.let { playerToChar(it) } ?: " " }}")
    }
    println()
}

fun printC4State(state: State) {
    println("   0  1  2  3  4  5  6")
    state.forEachIndexed { index, line ->
        println("$index ${line.map { it?.let { playerToChar(it) } ?: " " }}")
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
    val initialState: State = arrayOf(
        arrayOf(null, null, null, null, null, null, null),
        arrayOf(null, null, null, null, null, null, null),
        arrayOf(null, null, null, null, null, null, null),
        arrayOf(null, null, null, null, null, null, null),
        arrayOf(null, null, null, null, null, null, null),
        arrayOf(null, null, null, null, null, null, null)
    )
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