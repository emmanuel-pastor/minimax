package com.emmanuel.pastor.simplesmartapps.algorithm

interface GameRules<S, A> {
    fun nextPlayer(state: S): Player

    /**
     * Check if the game is over and return the result
     * @param state The current state of the game
     * @return The result of the game if it is over, otherwise null
     */
    fun gameResult(state: S): GameResult?
    fun possibleActions(state: S): Array<A>
    fun nextState(state: S, action: A): S
    fun valueOf(state: S): Int
}