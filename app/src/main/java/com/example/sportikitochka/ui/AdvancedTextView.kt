package com.example.sportikitochka.ui

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet

class AdvancedTextView : androidx.appcompat.widget.AppCompatTextView {
    private var mMaxValue = 100

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    fun setMaxValue(maxValue: Int) {
        mMaxValue = maxValue
    }

    @Synchronized
    fun setValue(value: Int) {
        this.text = ""
        val background = this.background as LayerDrawable
        val barValue = background.getDrawable(1) as ClipDrawable
        barValue.setLevel((value * 10000 / mMaxValue))
        drawableStateChanged()
    }
}