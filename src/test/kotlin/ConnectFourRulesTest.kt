import com.emmanuel.pastor.simplesmartapps.ConnectFourRules
import com.emmanuel.pastor.simplesmartapps.GameResult
import com.emmanuel.pastor.simplesmartapps.Player
import com.emmanuel.pastor.simplesmartapps.State
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ConnectFourRulesTest {
    val rules = ConnectFourRules()

    private fun matrixToState(matrix: Array<Array<String>>): State {
        return matrix.map { row ->
            row.map { cell ->
                when (cell) {
                    "X" -> Player.Max
                    "O" -> Player.Min
                    else -> null
                }
            }.toTypedArray()
        }.toTypedArray()
    }

    @Nested
    inner class NextPlayerTests {
        @Test
        fun `next player is Min given a basic state`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("X", "O", "O", "X", "", "", "")
                )
            )

            val nextPlayer = rules.nextPlayer(initialState)

            assertEquals(Player.Min, nextPlayer)
        }

        @Test
        fun `next player is Max given a basic state`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("O", "", "", "", "", "", ""),
                    arrayOf("X", "", "", "", "", "", ""),
                    arrayOf("X", "O", "O", "", "", "", "")
                )
            )

            val nexPlayer = rules.nextPlayer(initialState)

            assertEquals(Player.Max, nexPlayer)
        }

        @Test
        fun `next player is Min given an empty grid state`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", "")
                )
            )

            val nexPlayer = rules.nextPlayer(initialState)

            assertEquals(Player.Min, nexPlayer)
        }

        @Test
        fun `exception thrown when grid is full`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("O", "X", "O", "X", "O", "X", "O"),
                    arrayOf("X", "O", "O", "O", "X", "O", "X"),
                    arrayOf("O", "X", "X", "X", "O", "X", "O"),
                    arrayOf("X", "O", "O", "X", "O", "O", "X"),
                    arrayOf("O", "X", "O", "X", "X", "X", "O"),
                    arrayOf("X", "O", "X", "O", "X", "O", "X")
                )
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
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("O", "X", "O", "X", "O", "X", "O"),
                    arrayOf("X", "O", "O", "O", "X", "O", "X"),
                    arrayOf("O", "X", "X", "X", "O", "X", "O"),
                    arrayOf("X", "O", "O", "X", "O", "O", "X"),
                    arrayOf("O", "X", "O", "X", "X", "X", "O"),
                    arrayOf("X", "O", "X", "O", "X", "O", "X")
                )
            )

            val actions = rules.possibleActions(initialState)

            assert(actions.isEmpty())
        }

        @Test
        fun `return every possible action when given an empty grid state`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", "")
                )
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(0, 1, 2, 3, 4, 5, 6)
            assert(actions.contentDeepEquals(expectedActions))
        }

        @Test
        fun `return one action when given a state with only one action possible`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("O", "", "O", "X", "O", "X", "O"),
                    arrayOf("X", "O", "O", "O", "X", "O", "X"),
                    arrayOf("O", "X", "X", "X", "O", "X", "O"),
                    arrayOf("X", "O", "O", "X", "O", "O", "X"),
                    arrayOf("O", "X", "O", "X", "X", "X", "O"),
                    arrayOf("X", "O", "X", "O", "X", "O", "X")
                )
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(1)
            assert(actions.contentDeepEquals(expectedActions))
        }

        @Test
        fun `return all possible actions when given normal grid state with a few empty cells`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("O", "", "", "", "", "", ""),
                    arrayOf("X", "", "", "", "", "", ""),
                    arrayOf("X", "O", "O", "", "", "", "")
                )
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(0, 1, 2, 3, 4, 5, 6)
            assert(actions.contentEquals(expectedActions))
        }

        @Test
        fun `return all possible solutions when given a grid state with partially filled columns`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("X", "O", " ", " ", " ", " ", " "),
                    arrayOf("X", "X", "X", " ", " ", " ", " "),
                    arrayOf("X", "O", "X", "O", " ", " ", " "),
                    arrayOf("O", "O", "O", "O", "X", "O", "X"),
                    arrayOf("X", "O", "X", "O", "X", "O", "X"),
                    arrayOf("O", "O", "X", "O", "X", "O", "X")
                )
            )

            val actions = rules.possibleActions(initialState)

            val expectedActions = arrayOf(2, 3, 4, 5, 6)
            assert(actions.contentEquals(expectedActions))
        }
    }

    @Nested
    inner class GameResultTests {
        @Test
        fun `return null given empty grid state`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", ""),
                    arrayOf("", "", "", "", "", "", "")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(null, gameResult)
        }

        @Test
        fun `return Draw given full grid state with a draw`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("O", "X", "X", "X", "O", "X", "O"),
                    arrayOf("X", "O", "O", "O", "X", "O", "X"),
                    arrayOf("O", "X", "X", "O", "X", "X", "O"),
                    arrayOf("X", "X", "O", "X", "O", "O", "O"),
                    arrayOf("O", "X", "O", "X", "X", "X", "O"),
                    arrayOf("X", "O", "X", "O", "X", "O", "X")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(GameResult.Draw, gameResult)
        }

        @Test
        fun `return Max given Max won with a line before full grid`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("O", "O", "", "X", "X", "X", "X"),
                    arrayOf("X", "O", "O", "O", "X", "O", "O"),
                    arrayOf("O", "X", "X", "X", "O", "X", "O"),
                    arrayOf("X", "O", "O", "X", "O", "O", "X"),
                    arrayOf("O", "X", "O", "X", "X", "X", "O"),
                    arrayOf("X", "O", "X", "O", "X", "O", "X")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(GameResult.Max, gameResult)
        }

        @Test
        fun `return Min given Min won with a line before full grid`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("X", "X", "", "O", "O", "O", "O"),
                    arrayOf("X", "O", "O", "O", "X", "O", "O"),
                    arrayOf("O", "X", "X", "X", "O", "X", "O"),
                    arrayOf("X", "O", "O", "X", "O", "O", "X"),
                    arrayOf("O", "X", "O", "X", "X", "X", "O"),
                    arrayOf("X", "O", "X", "O", "X", "O", "X")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(GameResult.Min, gameResult)
        }

        @Test
        fun `return Max given Max won with a column before full grid`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("O", "", "O", "X", "X", "X", "O"),
                    arrayOf("O", "X", "O", "X", "O", "O", "O"),
                    arrayOf("X", "X", "O", "O", "X", "O", "X"),
                    arrayOf("O", "O", "X", "X", "O", "X", "X"),
                    arrayOf("X", "O", "X", "X", "X", "O", "X"),
                    arrayOf("O", "X", "O", "X", "O", "X", "X")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(GameResult.Max, gameResult)
        }

        @Test
        fun `return Min given Min won with a column before full grid`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("X", "", "O", "O", "X", "X", "X"),
                    arrayOf("O", "X", "X", "X", "O", "O", "X"),
                    arrayOf("X", "X", "O", "O", "X", "O", "O"),
                    arrayOf("O", "O", "X", "X", "O", "X", "O"),
                    arrayOf("X", "O", "X", "X", "X", "O", "O"),
                    arrayOf("X", "X", "O", "X", "O", "X", "O")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(GameResult.Min, gameResult)
        }

        @Test
        fun `return Min given Min won with a top left to bottom right diagonal before full grid`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("X", "", "O", "O", "X", "X", "X"),
                    arrayOf("O", "X", "X", "X", "O", "O", "X"),
                    arrayOf("X", "X", "O", "O", "X", "O", "X"),
                    arrayOf("O", "O", "X", "X", "O", "X", "O"),
                    arrayOf("X", "O", "X", "X", "X", "O", "O"),
                    arrayOf("X", "X", "O", "X", "O", "X", "O")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(GameResult.Min, gameResult)
        }

        @Test
        fun `return Max given Max won with a top left to bottom right diagonal before full grid`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("X", " ", " ", "O", "X", "X", "X"),
                    arrayOf("O", "X", "X", "X", "O", "O", "X"),
                    arrayOf("X", "O", "O", "O", "X", "O", "O"),
                    arrayOf("O", "X", "X", "O", "O", "X", "X"),
                    arrayOf("O", "O", "X", "X", "O", "X", "X"),
                    arrayOf("X", "O", "O", "X", "X", "X", "O")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(GameResult.Max, gameResult)
        }

        @Test
        fun `return Min given Min won with a top right to bottom left diagonal before full grid`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("X", " ", "O", "O", "X", "X", "O"),
                    arrayOf("O", "X", "X", "X", "O", "O", "X"),
                    arrayOf("X", "X", "O", "X", "O", "O", "X"),
                    arrayOf("O", "O", "X", "O", "X", "X", "X"),
                    arrayOf("X", "O", "X", "X", "X", "O", "O"),
                    arrayOf("X", "X", "O", "X", "O", "X", "O")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(GameResult.Min, gameResult)
        }

        @Test
        fun `return Max given Max won with a top right to bottom left diagonal before full grid`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("X", " ", " ", "O", "X", "X", "O"),
                    arrayOf("O", "X", "X", "X", "O", "O", "O"),
                    arrayOf("O", "X", "O", "O", "X", "X", "X"),
                    arrayOf("O", "X", "X", "O", "O", "X", "X"),
                    arrayOf("X", "O", "X", "O", "X", "X", "O"),
                    arrayOf("X", "O", "O", "X", "X", "O", "O")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(GameResult.Max, gameResult)
        }

        @Test
        fun `return null given no player won yet`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("O", "O", "", "", "X", "X", "O"),
                    arrayOf("X", "O", "O", "O", "X", "O", "X"),
                    arrayOf("O", "X", "X", "X", "O", "X", "O"),
                    arrayOf("X", "X", "O", "X", "O", "X", "X"),
                    arrayOf("O", "X", "O", "X", "X", "O", "O"),
                    arrayOf("X", "O", "X", "O", "O", "O", "X")
                )
            )

            val gameResult = rules.gameResult(initialState)

            assertEquals(null, gameResult)
        }
    }

    @Nested
    inner class NextStateTests {
        @Test
        fun `throws an exception when given coordinates out of the grid`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("O", "O", "", "", "X", "X", "O"),
                    arrayOf("X", "O", "O", "O", "X", "O", "X"),
                    arrayOf("O", "X", "X", "X", "O", "X", "O"),
                    arrayOf("X", "X", "O", "X", "O", "X", "X"),
                    arrayOf("O", "X", "O", "X", "X", "O", "O"),
                    arrayOf("X", "O", "X", "O", "O", "O", "X")
                )
            )

            assertThrows(IllegalArgumentException::class.java) {
                rules.nextState(initialState, (-1))
            }
        }

        @Test
        fun `throws an exception when given coordinates for an non empty cell`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf("O", "O", "", "", "X", "X", "O"),
                    arrayOf("X", "O", "O", "O", "X", "O", "X"),
                    arrayOf("O", "X", "X", "X", "O", "X", "O"),
                    arrayOf("X", "X", "O", "X", "O", "X", "X"),
                    arrayOf("O", "X", "O", "X", "X", "O", "O"),
                    arrayOf("X", "O", "X", "O", "O", "O", "X")
                )
            )

            assertThrows(IllegalArgumentException::class.java) {
                rules.nextState(initialState, (0))
            }
        }

        @Test
        fun `return the right state given a normal initial state and action and playing with Min`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", "", " ", " ", " ", " ")
                )
            )


            val nextState = rules.nextState(initialState, (1))

            val expectedState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", "O", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", "", " ", " ", " ", " ")
                )
            )
            assert(nextState.contentDeepEquals(expectedState))
        }

        @Test
        fun `return the right state given a normal initial state and action and playing with Max`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", "O", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", "", " ", " ", " ", " ")
                )
            )


            val nextState = rules.nextState(initialState, (0))

            val expectedState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", "", " ", " ", " ", " ")
                )
            )
            assert(nextState.contentDeepEquals(expectedState))
        }

        @Test
        fun `check that a deep copy is done`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", "O", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", "", " ", " ", " ", " ")
                )
            )


            val nextState = rules.nextState(initialState, (0))

            assertNotEquals(initialState.toString(), nextState.toString())
            for ((x, line) in initialState.withIndex()) {
                assertNotEquals(line.toString(), nextState[x].toString())
            }
        }
    }

    @Nested
    inner class ValueOfTests {
        @Test
        fun `return MAX Int when Max won`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf("X", " ", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", " ", " ", " ", " ", " "),
                    arrayOf("X", "O", "O", " ", " ", " ", " ")
                )
            )

            val value = rules.valueOf(initialState)

            assertEquals(Int.MAX_VALUE, value)
        }

        @Test
        fun `return MIN Int when Min won`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf("O", " ", " ", " ", " ", " ", " "),
                    arrayOf("O", "X", " ", " ", " ", " ", " "),
                    arrayOf("O", "X", " ", " ", " ", " ", " "),
                    arrayOf("O", "X", " ", " ", " ", " ", " ")
                )
            )

            val value = rules.valueOf(initialState)

            assertEquals(Int.MIN_VALUE, value)
        }

        @Test
        fun `return positive value when Max is one token from winning`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", "X", " ", " ", " ", " ", " "),
                    arrayOf("O", "X", "O", " ", " ", " ", " "),
                    arrayOf("O", "X", "O", " ", " ", " ", " ")
                )
            )

            val value = rules.valueOf(initialState)

            assert(value > 0)
        }

        @Test
        fun `return positive value when Min is one token from winning`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf("O", " ", " ", " ", " ", " ", " "),
                    arrayOf("O", "X", " ", " ", " ", " ", " "),
                    arrayOf("O", "X", "X", " ", " ", " ", " ")
                )
            )

            val value = rules.valueOf(initialState)

            assert(value < 0)
        }

        @Test
        fun `return expected value given a specific state`() {
            val initialState = matrixToState(
                arrayOf(
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", " ", " ", " ", " "),
                    arrayOf(" ", " ", " ", "O", " ", " ", " "),
                    arrayOf("O", "X", "X", "X", " ", " ", " "),
                    arrayOf("O", "X", "O", "O", " ", " ", " ")
                )
            )

            val value = rules.valueOf(initialState)

            assertEquals(84, value)
        }
    }
}