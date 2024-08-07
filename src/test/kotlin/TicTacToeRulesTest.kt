import com.emmanuel.pastor.simplesmartapps.GameResult
import com.emmanuel.pastor.simplesmartapps.Player
import com.emmanuel.pastor.simplesmartapps.TicTacToeRules
import com.emmanuel.pastor.simplesmartapps.minimax
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TicTacToeRulesTest {

    val rules = TicTacToeRules()

    @Nested
    inner class NextPlayerTests {
        @Test
        fun `next player is Min given a basic state`() {
            val initialState = arrayOf(
                arrayOf("X", "X", ""),
                arrayOf("O", "O", ""),
                arrayOf("X", "O", "")
            )

            val nextPlayer = rules.nextPlayer(initialState)

            assertEquals(nextPlayer, Player.Min)
        }

        @Test
        fun `next player is Max given a basic state`() {
            val initialState = arrayOf(
                arrayOf("X", "X", ""),
                arrayOf("O", "O", ""),
                arrayOf("O", "", "")
            )

            val nexPlayer = rules.nextPlayer(initialState)

            assertEquals(nexPlayer, Player.Max)
        }

        @Test
        fun `next player is Min given an empty grid state`() {
            val initialState = arrayOf(
                arrayOf("", "", ""),
                arrayOf("", "", ""),
                arrayOf("", "", "")
            )

            val nexPlayer = rules.nextPlayer(initialState)

            assertEquals(nexPlayer, Player.Min)
        }

        @Test
        fun `exception thrown when grid is full`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "O", "X"),
                arrayOf("X", "O", "O")
            )

            assertThrows(IllegalArgumentException::class.java) {
                rules.nextPlayer(initialState)
            }
        }
    }

    @Nested
    inner class GameResultTests {
        @Test
        fun `return null given empty grid state`() {
            val initialState = arrayOf(
                arrayOf("", "", ""),
                arrayOf("", "", ""),
                arrayOf("", "", "")
            )

            val gameResult = rules.getGameResult(initialState)

            assertEquals(gameResult, null)
        }

        @Test
        fun `return Draw given full grid state with a draw`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "O", "X"),
                arrayOf("X", "O", "O")
            )

            val gameResult = rules.getGameResult(initialState)

            assertEquals(gameResult, GameResult.Draw)
        }

        @Test
        fun `return Min given Min won with a diagonal`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "O", "X"),
                arrayOf("O", "X", "X")
            )

            val gameResult = rules.getGameResult(initialState)

            assertEquals(gameResult, GameResult.Min)
        }

        @Test
        fun `return Max given Max won with a line before full grid`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "X"),
                arrayOf("O", "O", "X"),
                arrayOf("O", "", "")
            )

            val gameResult = rules.getGameResult(initialState)

            assertEquals(gameResult, GameResult.Max)
        }

        @Test
        fun `return Max given Max won with a Column`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "X", "X"),
                arrayOf("O", "X", "O")
            )

            val gameResult = rules.getGameResult(initialState)

            assertEquals(gameResult, GameResult.Max)
        }
    }

    @Nested
    inner class PossibleActionsTests {
        @Test
        fun `return no action when given a full grid state`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "X", "X"),
                arrayOf("O", "X", "O")
            )

            val actions = rules.possibleActions(initialState)

            assert(actions.isEmpty())
        }

        @Test
        fun `return every possible action when given an empty grid state`() {
            val initialState = arrayOf(
                arrayOf("", "", ""),
                arrayOf("", "", ""),
                arrayOf("", "", "")
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(
                0 to 0, 0 to 1, 0 to 2,
                1 to 0, 1 to 1, 1 to 2,
                2 to 0, 2 to 1, 2 to 2
            )
            assert(actions.contentEquals(expectedActions))
        }

        @Test
        fun `return one action when given a state with only one action possible`() {
            val initialState = arrayOf(
                arrayOf("", "X", "O"),
                arrayOf("O", "X", "X"),
                arrayOf("O", "X", "O")
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(0 to 0)
            assert(actions.contentEquals(expectedActions))
        }

        @Test
        fun `return all possible actions when given normal grid state with a few empty cells`() {
            val initialState = arrayOf(
                arrayOf("O", "X", ""),
                arrayOf("O", "", "X"),
                arrayOf("O", "X", "O")
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(0 to 2, 1 to 1)
            assert(actions.contentEquals(expectedActions))
        }
    }

    @Nested
    inner class NextStateTests {
        @Test
        fun `throws an exception when given coordinates out of the grid`() {
            val initialState = arrayOf(
                arrayOf("O", "X", ""),
                arrayOf("X", "O", ""),
                arrayOf("O", "X", "")
            )

            assertThrows(IllegalArgumentException::class.java) {
                rules.nextState(initialState, (-1 to 3))
            }
        }

        @Test
        fun `throws an exception when given coordinates for an non empty cell`() {
            val initialState = arrayOf(
                arrayOf("O", "X", ""),
                arrayOf("X", "O", ""),
                arrayOf("O", "X", "")
            )

            assertThrows(IllegalArgumentException::class.java) {
                rules.nextState(initialState, (0 to 0))
            }
        }

        @Test
        fun `return the right state given a normal initial state and action and playing with Min`() {
            val initialState = arrayOf(
                arrayOf("O", "X", ""),
                arrayOf("X", "O", ""),
                arrayOf("O", "X", "")
            )


            val nextState = rules.nextState(initialState, (0 to 2))

            val expectedState = arrayOf(
                arrayOf("O", "X", "O"),
                arrayOf("X", "O", ""),
                arrayOf("O", "X", "")
            )
            assert(nextState.contentDeepEquals(expectedState))
        }

        @Test
        fun `return the right state given a normal initial state and action and playing with Max`() {
            val initialState = arrayOf(
                arrayOf("O", "X", ""),
                arrayOf("X", "O", ""),
                arrayOf("O", "", "")
            )


            val nextState = rules.nextState(initialState, (2 to 1))

            val expectedState = arrayOf(
                arrayOf("O", "X", ""),
                arrayOf("X", "O", ""),
                arrayOf("O", "X", "")
            )
            assert(nextState.contentDeepEquals(expectedState))
        }

        @Test
        fun `check that a deep copy is done`() {
            val initialState = arrayOf(
                arrayOf("O", "X", ""),
                arrayOf("X", "O", ""),
                arrayOf("O", "", "")
            )


            val nextState = rules.nextState(initialState, (2 to 1))

            assertNotEquals(nextState.toString(), initialState.toString())
            for ((x, line) in initialState.withIndex()) {
                assertNotEquals(nextState[x].toString(), line.toString())
            }
        }
    }

    @Nested
    inner class ValueOfTests {
        @Test
        fun `throws an error when given a non terminal state`() {
            val initialState = arrayOf(
                arrayOf("O", "X", ""),
                arrayOf("X", "O", ""),
                arrayOf("O", "X", "")
            )

            assertThrows(IllegalArgumentException::class.java) {
                rules.valueOf(initialState)
            }
        }

        @Test
        fun `return 1 when Max won`() {
            val initialState = arrayOf(
                arrayOf("O", "", "X"),
                arrayOf("O", "X", ""),
                arrayOf("X", "O", "")
            )

            val value = rules.valueOf(initialState)

            assertEquals(value, 1)
        }

        @Test
        fun `return -1 when Min won`() {
            val initialState = arrayOf(
                arrayOf("", "", "O"),
                arrayOf("X", "O", ""),
                arrayOf("O", "X", "")
            )

            val value = rules.valueOf(initialState)

            assertEquals(value, -1)
        }

        @Test
        fun `return 0 when it ended in a draw`() {
            val initialState = arrayOf(
                arrayOf("X", "X", "O"),
                arrayOf("O", "O", "X"),
                arrayOf("X", "O", "O")
            )

            val value = rules.valueOf(initialState)

            assertEquals(value, 0)
        }
    }

    @Nested
    inner class MinimaxTests {
        @Test
        fun `return -1 when Min will win`() {
            val initialState = arrayOf(
                arrayOf("X", "", ""),
                arrayOf("O", "O", ""),
                arrayOf("X", "O", "")
            )

            val value = minimax(initialState, Int.MAX_VALUE, Int.MIN_VALUE, Int.MAX_VALUE, rules)

            assertEquals(-1, value)
        }

        @Test
        fun `return 1 when Max will win`() {
            val initialState = arrayOf(
                arrayOf("O", "", ""),
                arrayOf("X", "X", ""),
                arrayOf("O", "X", "")
            )

            val value = minimax(initialState, Int.MAX_VALUE, Int.MIN_VALUE, Int.MAX_VALUE, rules)

            assertEquals(1, value)
        }

        @Test
        fun `return 0 when only a draw is possible`() {
            val initialState = arrayOf(
                arrayOf("O", "O", "X"),
                arrayOf("X", "", ""),
                arrayOf("O", "X", "O")
            )

            val value = minimax(initialState, Int.MAX_VALUE, Int.MIN_VALUE, Int.MAX_VALUE, rules)

            assertEquals(0, value)
        }

        @Test
        fun `return 0 when given an empty grid state`() {
            val initialState = arrayOf(
                arrayOf("", "", ""),
                arrayOf("", "", ""),
                arrayOf("", "", "")
            )

            val value = minimax(initialState, Int.MAX_VALUE, Int.MIN_VALUE, Int.MAX_VALUE, rules)

            assertEquals(0, value)
        }

        @Test
        fun `return 1 when given a grid state where Max won`() {
            val initialState = arrayOf(
                arrayOf("O", "", "X"),
                arrayOf("O", "X", ""),
                arrayOf("X", "O", "")
            )

            val value = minimax(initialState, Int.MAX_VALUE, Int.MIN_VALUE, Int.MAX_VALUE, rules)

            assertEquals(1, value)
        }
    }
}