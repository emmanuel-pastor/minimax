package frontends.cli

import com.github.ajalt.mordant.input.interactiveSelectList
import com.github.ajalt.mordant.terminal.Terminal
import frontends.cli.connect_four.ConnectFourCli
import frontends.cli.tic_tac_toe.TicTacToeCli

fun main() {
    val terminal = Terminal()
    val menuOptions = listOf("❌⭕ Tic Tac Toe", "🔴🟡 Connect Four", "👋 Quit")

    var quit = false
    while (!quit) {
        val choice = terminal.interactiveSelectList(
            menuOptions,
            title = "🎮 Game Menu 🎮"
        )

        when (choice) {
            menuOptions[0] -> {
                TicTacToeCli().run()
            }

            menuOptions[1] -> {
                ConnectFourCli().run()
            }

            menuOptions[2] -> {
                quit = true
                terminal.println("👋 Bye!")
            }
        }
        println("-------------------")
    }
}
