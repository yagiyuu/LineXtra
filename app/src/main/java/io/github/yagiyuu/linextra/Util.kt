package io.github.yagiyuu.linextra

import android.view.View
import android.view.ViewGroup

object Util {
    fun View.hideView() {
        visibility = View.GONE
        layoutParams = layoutParams?.apply {
            height = 0
            width = 0
        } ?: ViewGroup.LayoutParams(0, 0)
    }
}