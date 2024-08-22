package algorithm

sealed class Player {
    data object Max : Player()
    data object Min : Player()
}