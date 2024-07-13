import com.emmanuel.pastor.simplesmartapps.Player
import com.emmanuel.pastor.simplesmartapps.isTerminal
import com.emmanuel.pastor.simplesmartapps.nextPlayer
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested

class MainKtTest {

    @Nested
    inner class NextPlayerTests {
        @Test
        fun `next player is Min given a basic state`() {
            val initialState = arrayOf(
                arrayOf("X", "X", ""),
                arrayOf("O", "O", ""),
                arrayOf("X", "O", "")
            )

            val nextPlayer = nextPlayer(initialState)

            assertEquals(nextPlayer, Player.Min)
        }

        @Test
        fun `next player is Max given a basic state`() {
            val initialState = arrayOf(
                arrayOf("X", "X", ""),
                arrayOf("O", "O", ""),
                arrayOf("O", "", "")
            )

            val nexPlayer = nextPlayer(initialState)

            assertEquals(nexPlayer, Player.Max)
        }

        @Test
        fun `next player is Min given an empty grid state`() {
            val initialState = arrayOf(
                arrayOf("", "", ""),
                arrayOf("", "", ""),
                arrayOf("", "", "")
            )

            val nexPlayer = nextPlayer(initialState)

            assertEquals(nexPlayer, Player.Min)
        }

        @Test
        fun `exception thrown when grid is full`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "O", "X"),
                arrayOf("X", "O", "X")
            )

            assertThrows(IllegalArgumentException::class.java) {
                nextPlayer(initialState)
            }
        }
    }

    @Nested
    inner class IsTerminalTests {
        @Test
        fun `return false given empty grid state`() {
            val initialState = arrayOf(
                arrayOf("", "", ""),
                arrayOf("", "", ""),
                arrayOf("", "", "")
            )

            val isTerminal = isTerminal(initialState)

            assertEquals(isTerminal, false)
        }

        @Test
        fun `return true given full grid state with a draw`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "O", "X"),
                arrayOf("X", "O", "X")
            )

            val isTerminal = isTerminal(initialState)

            assertEquals(isTerminal, true)
        }

        @Test
        fun `return true given Min won with a diagonal`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "O", "X"),
                arrayOf("O", "X", "X")
            )

            val isTerminal = isTerminal(initialState)

            assertEquals(isTerminal, true)
        }

        @Test
        fun `return true given Max won with a line before full grid`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "X"),
                arrayOf("O", "O", "X"),
                arrayOf("O", "", "")
            )

            val isTerminal = isTerminal(initialState)

            assertEquals(isTerminal, true)
        }

        @Test
        fun `return true given Max won with a Column`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "X", "X"),
                arrayOf("O", "X", "O")
            )

            val isTerminal = isTerminal(initialState)

            assertEquals(isTerminal, true)
        }
    }
}