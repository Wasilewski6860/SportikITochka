package com.example.sportikitochka.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.example.sportikitochka.R
import world.mappable.mapkit.mapview.MapView

class PlatformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.my_map, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlatformView)
        val movable = typedArray.getBoolean(R.styleable.PlatformView_movable, false)
        typedArray.recycle()

    }

}