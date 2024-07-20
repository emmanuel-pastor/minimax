import com.emmanuel.pastor.simplesmartapps.ConnectFourRules
import com.emmanuel.pastor.simplesmartapps.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ConnectFourRulesTest {
    val rules = ConnectFourRules()

    @Nested
    inner class NextPlayerTests {
        @Test
        fun `next player is Min given a basic state`() {
            val initialState = arrayOf(
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("X", "O", "O", "X", "", "", "")
            )

            val nextPlayer = rules.nextPlayer(initialState)

            assertEquals(nextPlayer, Player.Min)
        }

        @Test
        fun `next player is Max given a basic state`() {
            val initialState = arrayOf(
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("O", "", "", "", "", "", ""),
                arrayOf("X", "", "", "", "", "", ""),
                arrayOf("X", "O", "O", "", "", "", "")
            )

            val nexPlayer = rules.nextPlayer(initialState)

            assertEquals(nexPlayer, Player.Max)
        }

        @Test
        fun `next player is Min given an empty grid state`() {
            val initialState = arrayOf(
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", "")
            )

            val nexPlayer = rules.nextPlayer(initialState)

            assertEquals(nexPlayer, Player.Min)
        }

        @Test
        fun `exception thrown when grid is full`() {
            val initialState = arrayOf(
                arrayOf("O", "X", "O", "X", "O", "X", "O"),
                arrayOf("X", "O", "O", "O", "X", "O", "X"),
                arrayOf("O", "X", "X", "X", "O", "X", "O"),
                arrayOf("X", "O", "O", "X", "O", "O", "X"),
                arrayOf("O", "X", "O", "X", "X", "X", "O"),
                arrayOf("X", "O", "X", "O", "X", "O", "X")
            )

            assertThrows(IllegalArgumentException::class.java) {
                rules.nextPlayer(initialState)
            }
        }
    }

    @Nested
    inner class PossibleActionsTests {
        @Test
        fun `return no action when given a full grid state`() {
            val initialState = arrayOf(
                arrayOf("O", "X", "O", "X", "O", "X", "O"),
                arrayOf("X", "O", "O", "O", "X", "O", "X"),
                arrayOf("O", "X", "X", "X", "O", "X", "O"),
                arrayOf("X", "O", "O", "X", "O", "O", "X"),
                arrayOf("O", "X", "O", "X", "X", "X", "O"),
                arrayOf("X", "O", "X", "O", "X", "O", "X")
            )

            val actions = rules.possibleActions(initialState)

            assert(actions.isEmpty())
        }

        @Test
        fun `return every possible action when given an empty grid state`() {
            val initialState = arrayOf(
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", "")
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(
                5 to 0, 5 to 1, 5 to 2, 5 to 3, 5 to 4, 5 to 5, 5 to 6,
            )
            assert(actions.contentDeepEquals(expectedActions))
        }

        @Test
        fun `return one action when given a state with only one action possible`() {
            val initialState = arrayOf(
                arrayOf("O", "", "O", "X", "O", "X", "O"),
                arrayOf("X", "O", "O", "O", "X", "O", "X"),
                arrayOf("O", "X", "X", "X", "O", "X", "O"),
                arrayOf("X", "O", "O", "X", "O", "O", "X"),
                arrayOf("O", "X", "O", "X", "X", "X", "O"),
                arrayOf("X", "O", "X", "O", "X", "O", "X")
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(0 to 1)
            assert(actions.contentDeepEquals(expectedActions))
        }

        @Test
        fun `return all possible actions when given normal grid state with a few empty cells`() {
            val initialState = arrayOf(
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("", "", "", "", "", "", ""),
                arrayOf("O", "", "", "", "", "", ""),
                arrayOf("X", "", "", "", "", "", ""),
                arrayOf("X", "O", "O", "", "", "", "")
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(5 to 3, 5 to 4, 5 to 5, 5 to 6, 4 to 1, 4 to 2, 2 to 0)
            assert(actions.contentEquals(expectedActions))
        }
    }
}