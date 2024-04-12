package com.example.sportikitochka.presentation.main

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
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.auth.UserType.Premium.toUserType
import com.example.sportikitochka.databinding.ActivityMainBinding
import com.example.sportikitochka.other.TrackingUtility.USER_TYPE_KEY
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var navHostFragment: NavHostFragment

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val window: Window = getWindow()
//        window.setBackgroundDrawableResource(R.drawable.background_image)

        navigateToTrackingFragmentIfNeeded(intent)

        val value = intent.getStringExtra(USER_TYPE_KEY)
        val userType = value?.toUserType()

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostMainFragment) as NavHostFragment

        var standartBottomNavigationView : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        var adminBottomNavigationView : BottomNavigationView = findViewById(R.id.adminBottomNavigationView)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var bottomNavigationView: BottomNavigationView

        standartBottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        adminBottomNavigationView.visibility = View.GONE
        bottomNavigationView = standartBottomNavigationView

        when(userType){
            UserType.Normal, UserType.Premium  -> {
                standartBottomNavigationView.setupWithNavController(navHostFragment.findNavController())
                adminBottomNavigationView.visibility = View.GONE
                bottomNavigationView = standartBottomNavigationView
            }
            UserType.Admin -> {
                adminBottomNavigationView.setupWithNavController(navHostFragment.findNavController())
                standartBottomNavigationView.visibility = View.GONE
                bottomNavigationView = adminBottomNavigationView
            }

            else -> {}
        }



        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id) {

                    R.id.mainFragment, R.id.ratingFragment, R.id.statisticFragment, R.id.profileFragment ->{
                        bottomNavigationView.visibility = View.VISIBLE
//                        toolbar.visibility = View.VISIBLE
                    }
                    else -> {
                        bottomNavigationView.visibility = View.GONE
//                        toolbar.visibility = View.GONE
                    }
//                    R.id.mainFragment ->
//                        bottomNavigationView.visibility = View.VISIBLE
//                    else -> bottomNavigationView.visibility = View.GONE

                }
            }
    }



    private fun recreateActivity() {
        finish() // Завершите текущую активность
        startActivity(intent) // Запустите активность заново
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
//        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
//            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
//        }
    }

}