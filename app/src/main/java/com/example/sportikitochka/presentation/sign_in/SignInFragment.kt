package com.example.sportikitochka.presentation.sign_in

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by viewModel()

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val isAdmin: Boolean = true
    private val isPremium: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel.isLogged()){
            viewModel.getUserType()
            findNavController().navigate(
                R.id.action_signInFragment_to_raitingFragment,
                savedInstanceState
            )
        }

        binding.buttonLogin.setOnClickListener {
            signIn()
        }

        binding.createAccountTv.setOnClickListener {
            findNavController().navigate(
                R.id.action_signInFragment_to_signUpFragment,
                savedInstanceState
            )
        }

        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it){
                ScreenState.Loading -> Unit
                is ScreenState.Error -> showSnackbar(it.message.toString())
                ScreenState.Success -> {
                    viewModel.getUserType()
                    findNavController().navigate(
                        R.id.action_signInFragment_to_raitingFragment,
                        savedInstanceState
                    )
                }
                else -> Unit
            }
        }

    }


    private fun signIn() = with(binding) {
        val email = emailEdittext.text.toString()
        val password = passwordEdittext.text.toString()

        if (!validateEmail(email = email)){
            showSnackbar("Введите email корректно")
        }
        else if (!validatePassword(password = password)){
            showSnackbar("Пароль недостаточно сильный")
        }
        else viewModel.login(email, password)

    }

    fun showSnackbar(text : String){
        Snackbar.make(
            requireActivity().findViewById(R.id.rootView),
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }

}