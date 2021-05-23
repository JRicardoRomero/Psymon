package com.rommultimedia.psymon.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.media.SoundPool
import android.view.View
import android.widget.ImageButton
import androidx.core.animation.doOnStart

class SquaresAnimator(){


    fun scaler(square: ImageButton, soundPool: SoundPool, rSound:Int): Animator {

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(
            square, scaleX, scaleY)
        animator.duration=300
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.doOnStart {
            soundPool?.play(rSound, 1F,1F,2,0,1F)
        }
        return animator
    }


}