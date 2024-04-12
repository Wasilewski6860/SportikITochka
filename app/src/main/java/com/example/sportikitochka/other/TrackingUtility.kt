package com.example.sportikitochka.other

import android.view.View
import com.google.android.material.snackbar.Snackbar

object TrackingUtility {

    fun showSnackbar(text : String, view: View){
        Snackbar.make(
            view,
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }
}