package com.example.sportikitochka.presentation.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.ActivityMainBinding
import com.example.sportikitochka.databinding.AuthActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class AuthActivity : AppCompatActivity() {


    private lateinit var navHostFragment: NavHostFragment

    private var _binding: AuthActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = AuthActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun showSnackbar(message: String) {
        Snackbar.make(
            findViewById(R.id.rootView),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

}