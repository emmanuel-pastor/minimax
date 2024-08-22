package com.emmanuel.pastor.simplesmartapps.games

import com.emmanuel.pastor.simplesmartapps.algorithm.GameRules
import com.emmanuel.pastor.simplesmartapps.algorithm.Player
import com.emmanuel.pastor.simplesmartapps.algorithm.minimax
import kotlin.random.Random

/**
 * @param S The type of the state of the game
 * @param A The type of an action in the game
 * @property state The current state of the game
 * @property depth The depth of the search tree
 * @property rules The rules of the game
 */
class Game<S, A>(state: S, private val depth: Int, private val rules: GameRules<S, A>) {

    var state = state
        private set
    val nextPlayer get() = rules.nextPlayer(state)
    val result get() = rules.gameResult(state)

    fun play(action: A) {
        state = rules.nextState(state, action)
    }

    fun playOptimal() {
        play(nextOptimalAction())
    }

    private fun nextOptimalAction(): A {
        val actions = rules.possibleActions(state)
        val plays = mutableListOf<Pair<A, Int>>()
        val nextPlayer = rules.nextPlayer(state)
        actions.forEach { action ->
            val value = minimax(rules.nextState(state, action), depth, Int.MIN_VALUE, Int.MAX_VALUE, rules)
            plays.add(action to value)
        }

        return when (nextPlayer) {
            Player.Max -> {
                plays.maxBy { (_, value) -> value }.first
            }

            Player.Min -> {
                plays.minBy { (_, value) -> value }.first
            }
        }
    }

    fun playRandom() {
        val actions = rules.possibleActions(state)
        val randomAction = actions[Random.nextInt(actions.size)]
        play(randomAction)
    }
}
