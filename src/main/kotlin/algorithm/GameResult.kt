package algorithm

sealed class GameResult {
    data object Min : GameResult()
    data object Max : GameResult()
    data object Draw : GameResult()
}