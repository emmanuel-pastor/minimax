package frontends.cli

import algorithm.GameResult
import algorithm.Player
import games.Game
import kotlin.random.Random

abstract class CliFrontend<S, A>(private val game: Game<S, A>) {
    abstract fun isInputValid(input: String): Boolean
    abstract fun extractInput(input: String): A
    abstract fun printState(state: S)
    abstract fun printInstruction()

    open fun playerToChar(player: Player): String {
        return when (player) {
            Player.Min -> "O"
            Player.Max -> "X"
        }
    }

    fun run() {
        while (game.result == null) {
            this.printState(game.state)
            when (game.nextPlayer) {
                Player.Min -> {
                    val random = Random.nextFloat()
                    if (random < 0.4) {
                        game.playOptimal()
                    } else {
                        game.playRandom()
                    }
                }

                Player.Max -> {
                    var input: String
                    do {
                        this.printInstruction()
                        input = readln()
                    } while (!this.isInputValid(input))

                    game.play(this.extractInput(input))
                }
            }
        }
        this.printState(game.state)
        this.printWinner(game.result!!)
    }

    private fun printWinner(result: GameResult) {
        when (result) {
            GameResult.Draw -> println("The game ended in a DRAW")
            GameResult.Max -> println("${playerToChar(Player.Max)} player won")
            GameResult.Min -> println("${playerToChar(Player.Min)} player won")
        }
    }
}