package com.example.sportikitochka.presentation.auth.reset_password

import android.os.Bundle
import android.os.Parcelable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.data.network.error.DoesntMatchException
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.NotFoundException
import com.example.sportikitochka.R
import com.example.sportikitochka.common.State
import com.example.sportikitochka.databinding.FragmentResetPasswordBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.presentation.auth.AuthActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordFragment : Fragment() {


    private val viewModel: ResetPasswordViewModel by viewModel()
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    protected lateinit var connectionLiveData: ConnectionLiveData
    private var isOnline : Boolean = false

    private var email: String? = null
    private var password: String? = null
    private var code: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this.requireContext())
        connectionLiveData.observe(viewLifecycleOwner) {
            isOnline = it
        }

        binding.switchPasswordVisibility.setOnClickListener {
            if (binding.switchPasswordVisibility.isChecked) {
                binding.passwordEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.passwordConfirmEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.passwordEt.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.passwordConfirmEt.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        initListeners()
        setupObserver()
    }

    private fun initListeners() = with(binding){
        switchPasswordVisibility.setOnClickListener {
            if (binding.switchPasswordVisibility.isChecked) {
                binding.passwordEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.passwordConfirmEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.passwordEt.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.passwordConfirmEt.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                    with(binding) {
                        when (it.currentScreen) {
                            ResetNavigationState.FirstScreen -> {
                                emailAndPasswordLayout.visibility = View.VISIBLE
                                emailLayout.visibility = View.VISIBLE
                                passwordLayout.visibility = View.GONE
                                codeLayout.visibility = View.GONE

                                val advancedTextView = progressBarSignUp
                                advancedTextView.setValue(33)

                                binding.nextButtonResetPassword.setOnClickListener {
                                    if (binding.emailEt.text.isNullOrBlank()){
                                        TrackingUtility.showSnackbar(
                                            "Введите email",
                                            requireActivity().findViewById(R.id.rootView)
                                        )
                                    }
                                    else {
                                        email = binding.emailEt.text.toString()
                                        viewModel.sendEmail(binding.emailEt.text.toString())
                                    }
                                }
                            }
                            ResetNavigationState.SecondScreen -> {
                                emailAndPasswordLayout.visibility = View.VISIBLE
                                emailLayout.visibility = View.GONE
                                passwordLayout.visibility = View.GONE
                                codeLayout.visibility = View.VISIBLE

                                val advancedTextView = progressBarSignUp
                                advancedTextView.setValue(66)
                                binding.nextButtonResetPassword.setOnClickListener {
                                    if (binding.codeEt.text.isNullOrBlank()){
                                        TrackingUtility.showSnackbar(
                                            "Введите код",
                                            requireActivity().findViewById(R.id.rootView)
                                        )
                                    }
                                    else {
                                        viewModel.confirmCode(code!!,binding.codeEt.text.toString())
                                    }
                                }
                            }
                            ResetNavigationState.ThirdScreen -> {
                                emailAndPasswordLayout.visibility = View.VISIBLE
                                emailLayout.visibility = View.GONE
                                passwordLayout.visibility = View.VISIBLE
                                codeLayout.visibility = View.GONE

                                val advancedTextView = progressBarSignUp
                                advancedTextView.setValue(100)

                                binding.nextButtonResetPassword.setOnClickListener {
                                    if (binding.passwordEt.text.isNullOrBlank()){
                                        (requireActivity() as AuthActivity).showSnackbar("Введите пароль")
                                    }
                                    else if (binding.passwordEt.text.toString() != binding.passwordConfirmEt.text.toString()){
                                        TrackingUtility.showSnackbar(
                                            "Пароли должны совпадать",
                                            requireActivity().findViewById(R.id.rootView)
                                        )
                                    }
                                    else {
                                        viewModel.resetPassword(email!!,binding.passwordEt.text.toString(), binding.passwordConfirmEt.text.toString())
                                    }
                                }
                            }
                        }
                        when(it.sendEmailState) {
                            is State.Error -> {
                                when(it.sendEmailState.error) {
                                    is NotFoundException -> {
                                        (requireActivity() as AuthActivity).showSnackbar("Не удалось найти пользователя с данным адресом")
                                    }
                                    else -> {
                                        (requireActivity() as AuthActivity).showSnackbar("Не удалось отправить сообщение на введенный адрес. Проверьте корректность введенного")
                                    }
                                }
                            }
                            State.Loading -> Unit
                            State.NotStarted -> Unit
                            is State.Success -> {
                                code = it.sendEmailState.value
                                viewModel.navigateToScreen(ResetNavigationState.SecondScreen)
                            }
                        }
                        when(it.resetPasswordState) {
                            is State.Error -> {
                                when(it.resetPasswordState.error) {
                                    is DoesntMatchException -> {
                                        (requireActivity() as AuthActivity).showSnackbar("Введенные пароли не совпадают")
                                    }
                                    else -> {
                                        (requireActivity() as AuthActivity).showSnackbar("Не удалось сменить пароль")
                                    }
                                }
                            }
                            State.Loading -> Unit
                            State.NotStarted -> Unit
                            is State.Success -> findNavController().navigate(
                                R.id.action_resetPasswordFragment_to_signInFragment
                            )
                        }
                        when(it.confirmPasswordState) {
                            is State.Error -> {
                                when(it.confirmPasswordState.error) {
                                    is ForbiddenException -> {
                                        (requireActivity() as AuthActivity).showSnackbar("Доступ запрещен")
                                    }
                                    else -> {
                                        (requireActivity() as AuthActivity).showSnackbar("Код неверный")
                                    }
                                }
                            }
                            State.Loading -> Unit
                            State.NotStarted -> Unit
                            is State.Success -> viewModel.navigateToScreen(ResetNavigationState.ThirdScreen)
                        }
                    }
                }
            }
        }
    }


}


sealed class ResetNavigationState: Parcelable {
    @Parcelize
    object FirstScreen: ResetNavigationState()
    @Parcelize
    object SecondScreen: ResetNavigationState()
    @Parcelize
    object ThirdScreen: ResetNavigationState()
}