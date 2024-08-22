package com.emmanuel.pastor.simplesmartapps.algorithm

sealed class Player {
    data object Max : Player()
    data object Min : Player()
}