package com.example.sportikitochka.presentation.auth.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by viewModel()

    private var isOnboardingViewed: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isOnboardingViewed = viewModel.isOnboardingViewed()
        Handler().postDelayed({
            findNavController().navigate(
                if(isOnboardingViewed) R.id.action_splashFragment_to_signInFragment else
                    R.id.action_splashFragment_to_onboardingFragment,
                savedInstanceState
            )
        }, 3000)
    }

}