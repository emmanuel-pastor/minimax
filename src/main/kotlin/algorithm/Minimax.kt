package algorithm

import kotlin.math.max
import kotlin.math.min

/**
 * Minimax algorithm with alpha-beta pruning
 * @param state The current state of the game
 * @param depth The depth of the search tree
 * @param alpha The best value that the maximizer currently can guarantee at that level or above
 * @param beta The best value that the minimizer currently can guarantee at that level or above
 * @param rules The rules of the game
 * @return The value of the state for the current player
 */
fun <S, A> minimax(state: S, depth: Int, alpha: Int, beta: Int, rules: GameRules<S, A>): Int {
    if (rules.gameResult(state) != null || depth == 0) return rules.valueOf(state)

    val possibleActions = rules.possibleActions(state)
    var a = alpha
    var b = beta
    return when (rules.nextPlayer(state)) {
        Player.Max -> {
            var value = Int.MIN_VALUE
            for (action in possibleActions) {
                value = max(value, minimax(rules.nextState(state, action), depth - 1, a, b, rules))
                a = max(a, value)
                if (value >= b) break
            }
            value
        }

        Player.Min -> {
            var value = Int.MAX_VALUE
            for (action in possibleActions) {
                value = min(value, minimax(rules.nextState(state, action), depth - 1, a, b, rules))
                b = min(b, value)
                if (value <= a) break
            }
            value
        }
    }
}