package com.example.sportikitochka.ui

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet

class AdvancedTextView : androidx.appcompat.widget.AppCompatTextView {
    // Максимальное значение шкалы
    private var mMaxValue = 100

    // Конструкторы
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    // Установка максимального значения
    fun setMaxValue(maxValue: Int) {
        mMaxValue = maxValue
    }

    // Установка значения
    @Synchronized
    fun setValue(value: Int) {
        // Установка новой надписи
        this.text = ""

        // Drawable, отвечающий за фон
        val background = this.background as LayerDrawable

        // Достаём Clip, отвечающий за шкалу, по индексу 1
        val barValue = background.getDrawable(1) as ClipDrawable

        // Устанавливаем уровень шкалы
        barValue.setLevel((value * 10000 / mMaxValue))

        // Уведомляем об изменении Drawable
        drawableStateChanged()
    }
}