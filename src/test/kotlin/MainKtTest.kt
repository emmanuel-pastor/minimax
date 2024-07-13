import com.emmanuel.pastor.simplesmartapps.Player
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
}