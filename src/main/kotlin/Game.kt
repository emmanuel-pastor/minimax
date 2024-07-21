package com.emmanuel.pastor.simplesmartapps

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

sealed class Player {
    data object Max : Player()
    data object Min : Player()
}

class Game<S, A>(private var state: S, private val depth: Int, private val rules: GameRules<S, A>) {

    val nextPlayer get() = rules.nextPlayer(state)
    val result get() = rules.getGameResult(state)

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

    fun printState() = rules.printState(state)
}

sealed class GameResult {
    data object Min: GameResult()
    data object Max: GameResult()
    data object Draw: GameResult()
}

interface GameRules<S, A> {
    fun nextPlayer(state: S): Player
    fun getGameResult(state: S): GameResult?
    fun possibleActions(state: S): Array<A>
    fun nextState(state: S, action: A): S
    fun valueOf(state: S): Int
    fun printState(state: S)
}

fun <S, A> minimax(state: S, depth: Int, alpha: Int, beta: Int, rules: GameRules<S, A>): Int {
    if (rules.getGameResult(state) != null || depth == 0) return rules.valueOf(state)

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