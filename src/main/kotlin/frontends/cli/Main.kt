package frontends.cli

import frontends.cli.connect_four.ConnectFourCli
import frontends.cli.tic_tac_toe.TicTacToeCli

fun main() {
    do {
        println("1: Tic Tac Toe")
        println("2: Connect Four")
        print("Choose a game: ")
        val input = readlnOrNull()
        when (input) {
            "1" -> TicTacToeCli().run()
            "2" -> ConnectFourCli().run()
            else -> println("Invalid input")
        }
    } while (true)
}