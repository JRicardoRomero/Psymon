package com.rommultimedia.psymon.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.widget.ImageButton

class RightSquare {

    fun rightBtn(square: ImageButton): Animator {

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f)
        val alphaVal = PropertyValuesHolder.ofFloat(View.ALPHA,.75f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(
                square, scaleX, scaleY)
        animator.duration=300
        animator.repeatCount = -1
        animator.repeatMode = ObjectAnimator.REVERSE
        return animator
    }
}