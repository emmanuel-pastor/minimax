package com.emmanuel.pastor.simplesmartapps

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

sealed class Player {
    data object Max : Player()
    data object Min : Player()
}

class Game<S, A>(private var state: S, private val rules: GameRules<S, A>) {

    val nextPlayer get() = rules.nextPlayer(state)
    val isOver get() = rules.isTerminal(state)

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
            val value = minimax(rules.nextState(state, action), rules)
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

interface GameRules<S, A> {
    fun nextPlayer(state: S): Player
    fun isTerminal(state: S): Boolean
    fun possibleActions(state: S): Array<A>
    fun nextState(state: S, action: A): S
    fun valueOf(state: S): Int
    fun printState(state: S)
}

fun <S, A> minimax(state: S, rules: GameRules<S, A>): Int {
    if (rules.isTerminal(state)) return rules.valueOf(state)

    val possibleActions = rules.possibleActions(state)
    return when (rules.nextPlayer(state)) {
        Player.Max -> {
            var value = Int.MIN_VALUE
            possibleActions.forEach { action ->
                value = max(value, minimax(rules.nextState(state, action), rules))
            }
            value
        }

        Player.Min -> {
            var value = Int.MAX_VALUE
            possibleActions.forEach { action ->
                value = min(value, minimax(rules.nextState(state, action), rules))
            }
            value
        }
    }
}