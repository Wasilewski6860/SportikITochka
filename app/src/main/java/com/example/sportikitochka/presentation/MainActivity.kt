package com.example.sportikitochka.presentation

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var navHostFragment: NavHostFragment

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment

        var standartBottomNavigationView : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        var adminBottomNavigationView : BottomNavigationView = findViewById(R.id.adminBottomNavigationView)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var bottomNavigationView: BottomNavigationView

        standartBottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        adminBottomNavigationView.visibility = View.GONE
        bottomNavigationView = standartBottomNavigationView

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id) {

                    R.id.mainFragment, R.id.raitingFragment, R.id.statisticFragment, R.id.profileFragment ->{
                        bottomNavigationView.visibility = View.VISIBLE
//                        toolbar.visibility = View.VISIBLE
                    }
                    else -> {
                        bottomNavigationView.visibility = View.GONE
//                        toolbar.visibility = View.GONE
                    }
                }
            }
    }


}