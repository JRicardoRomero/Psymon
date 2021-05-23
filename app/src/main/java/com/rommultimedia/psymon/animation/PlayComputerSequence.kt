package com.rommultimedia.psymon.animation

import android.animation.Animator
import android.animation.AnimatorSet

class PlayComputerSequence(playList:MutableList<Animator>) {

    init {
        val compuPlaylist = AnimatorSet()
        compuPlaylist.playSequentially(playList)
        compuPlaylist.start()

    }


}