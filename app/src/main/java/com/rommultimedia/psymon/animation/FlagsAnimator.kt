package com.rommultimedia.psymon.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

class FlagsAnimator{
    fun flagShow(flag:View):Animator{

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f)
        flag.visibility= View.VISIBLE
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            flag, scaleX, scaleY
        )

        animator.duration=400
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        return animator
    }
}