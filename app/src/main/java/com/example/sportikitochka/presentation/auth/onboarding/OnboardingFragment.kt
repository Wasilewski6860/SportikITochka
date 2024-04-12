package com.example.sportikitochka.presentation.auth.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.FragmentOnboardingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.nextOnboardingButton.setOnClickListener{
            viewModel.setOnboardingViewed()
            findNavController().navigate(
                R.id.action_onboardingFragment_to_signInFragment,
                savedInstanceState
            )
        }
    }

}