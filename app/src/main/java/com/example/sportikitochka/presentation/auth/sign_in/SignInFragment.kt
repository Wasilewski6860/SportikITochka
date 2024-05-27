package com.example.sportikitochka.presentation.auth.sign_in

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.databinding.FragmentSignInBinding
import com.example.sportikitochka.other.TrackingUtility.USER_TYPE_KEY
import com.example.sportikitochka.other.Validator.validateEmail
import com.example.sportikitochka.other.Validator.validatePassword
import com.example.sportikitochka.presentation.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import io.appmetrica.analytics.AppMetrica
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
            val data = viewModel.getUserRole()
            if (data!=null) {
                AppMetrica.reportEvent("Sign in", data.toString())
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra(USER_TYPE_KEY, data.toString())
                startActivity(intent) // запускаем новую активность
            }
            else {
                showSnackbar("Что-то пошло не так. Связаться с нами: kakoi-to_email@gmail.com")
            }
        }

        binding.buttonLogin.setOnClickListener {
            signIn()
        }

        binding.createAccountTv.setOnClickListener {
            showSnackbar("Попытка регистрации")
            findNavController().navigate(
                R.id.action_signInFragment_to_signUpFragment,
                savedInstanceState
            )
        }

        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it){
                SignInScreenState.Loading -> Unit
                SignInScreenState.UserBlockedError -> showSnackbar("Вы заблокированы. Связаться с нами: kakoi-to_email@gmail.com")
                SignInScreenState.UserNotFoundError -> showSnackbar("Пользователь не найден. Связаться с нами: kakoi-to_email@gmail.com")
                SignInScreenState.IncorrectPasswordError -> showSnackbar("Неверный пароль. Связаться с нами: kakoi-to_email@gmail.com")
                SignInScreenState.Success -> {
                    showSnackbar("Успешный вход")

                    val data = viewModel.getUserRole()
                    if (data!=null) {
//                        AppMetrica.reportEvent("Sign in", data.toString())

                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra(USER_TYPE_KEY, data.toString())
                        startActivity(intent) // запускаем новую активность
                    }
                    else {
                        showSnackbar("Что-то пошло не так. Связаться с нами: kakoi-to_email@gmail.com")
                    }

//                    viewModel.getUserType()
//                    findNavController().navigate(
//                        R.id.action_signInFragment_to_raitingFragment,
//                        savedInstanceState
//                    )
                }
                SignInScreenState.AnyError -> showSnackbar("Что-то пошло не так. Связаться с нами: kakoi-to_email@gmail.com")
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