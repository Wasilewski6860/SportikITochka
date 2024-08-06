package com.example.sportikitochka.presentation.auth.sign_in

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.data.network.error.DoesntMatchException
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectPasswordException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UserBlockedException
import com.example.domain.coroutines.Response
import com.example.sportikitochka.R
import com.example.sportikitochka.common.State
import com.example.sportikitochka.databinding.FragmentSignInBinding
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.USER_TYPE_KEY
import com.example.sportikitochka.other.Validator.validateEmail
import com.example.sportikitochka.other.Validator.validatePassword
import com.example.sportikitochka.presentation.auth.AuthActivity
import com.example.sportikitochka.presentation.auth.reset_password.ResetNavigationState
import com.example.sportikitochka.presentation.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch
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

    private var isPasswordVisible = false


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
                showSnackbar("Что-то пошло не так. Связаться с нами: sportikitocka@gmail.com")
            }
        }

        initListeners()
        setupObservers()
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

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                   with(binding) {
                       when(it.signInState) {
                           is State.Error -> {
                               when(it.signInState.error) {
                                   is IncorrectInputException -> showSnackbar("Неверные данные. Связаться с нами: sportikitocka@gmail.com")
                                   is IncorrectPasswordException -> showSnackbar("Неверный пароль. Связаться с нами: sportikitocka@gmail.com")
                                   is UserBlockedException -> showSnackbar("Вы заблокированы. Связаться с нами: sportikitocka@gmail.com")
                                   is NotFoundException -> showSnackbar("Пользователь не найден. Связаться с нами: sportikitocka@gmail.com")
                                   else -> showSnackbar("Что-то пошло не так. Проверьте корректность введенного")
                               }
                           }
                           State.Loading -> Unit
                           State.NotStarted -> Unit
                           is State.Success -> {
                               showSnackbar("Успешный вход")
                               val data = viewModel.getUserRole()
                               if (data!=null) {
                                   val intent = Intent(activity, MainActivity::class.java)
                                   intent.putExtra(USER_TYPE_KEY, data.toString())
                                   startActivity(intent) // запускаем новую активность
                               }
                               else {
                                   showSnackbar("Что-то пошло не так. Связаться с нами: sportikitocka@gmail.com")
                               }
                           }
                       }
                   }
                }
            }
        }
    }
    private fun initListeners() {
        binding.buttonLogin.setOnClickListener {
            signIn()
        }
        binding.forgotPassTv.setOnClickListener {
            findNavController().navigate(
                R.id.action_signInFragment_to_resetPasswordFragment
            )
        }
        binding.createAccountTv.setOnClickListener {
            showSnackbar("Попытка регистрации")
            findNavController().navigate(
                R.id.action_signInFragment_to_signUpFragment
            )
        }
        binding.togglePasswordVisibility.setOnClickListener {
            if (isPasswordVisible) {
                // Скрыть пароль
                binding.passwordEdittext.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.togglePasswordVisibility.setImageResource(R.drawable.ic_hide_password)
            } else {
                // Показать пароль
                binding.passwordEdittext.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.togglePasswordVisibility.setImageResource(R.drawable.ic_show_password)
            }
            // Переместить курсор в конец текста
            binding.passwordEdittext.text?.let { it1 -> binding.passwordEdittext.setSelection(it1.length) }
            isPasswordVisible = !isPasswordVisible
        }
    }

    fun showSnackbar(text : String){
        Snackbar.make(
            requireActivity().findViewById(R.id.rootView),
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }

}