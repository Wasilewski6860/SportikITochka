package com.example.sportikitochka.presentation.auth.reset_password

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.sportactivityapp.other.DateFormatTextWatcher
import com.example.sportactivityapp.other.PhoneFormatTextWatcher
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.databinding.FragmentResetPasswordBinding
import com.example.sportikitochka.databinding.FragmentSignUpBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.WeightTextWatcher
import com.example.sportikitochka.presentation.auth.sign_up.SignUpScreenState
import com.example.sportikitochka.presentation.auth.sign_up.SignUpViewModel
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

class ResetPasswordFragment : Fragment() {

    companion object {
        fun newInstance() = ResetPasswordFragment()
    }

    private val viewModel: ResetPasswordViewModel by viewModel()
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    protected lateinit var connectionLiveData: ConnectionLiveData
    private var isOnline : Boolean = false

    private var currentScreenState: MutableLiveData<ResetNavigationState> = MutableLiveData<ResetNavigationState>().apply {
        value = ResetNavigationState.FirstScreen
    }

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

        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ResetPasswordScreenState.AnyError -> TrackingUtility.showSnackbar(
                    "Что-то пошло не так",
                    requireActivity().findViewById(R.id.rootView)
                )
                ResetPasswordScreenState.ConfirmCodeError -> TrackingUtility.showSnackbar(
                    "Код неверный",
                    requireActivity().findViewById(R.id.rootView)
                )
                ResetPasswordScreenState.ConfirmCodeSuccess -> currentScreenState.postValue(ResetNavigationState.ThirdScreen)
                ResetPasswordScreenState.Loading -> Unit
                ResetPasswordScreenState.ResetPasswordError -> TrackingUtility.showSnackbar(
                    "Не удалось сменить пароль",
                    requireActivity().findViewById(R.id.rootView)
                )
                ResetPasswordScreenState.ResetPasswordSuccess -> findNavController().navigate(
                    R.id.action_resetPasswordFragment_to_signInFragment,
                    savedInstanceState
                )
                ResetPasswordScreenState.SendToEmailError -> TrackingUtility.showSnackbar(
                    "Не удалось отправить сообщение на введенный адрес. Проверьте корректность введенного",
                    requireActivity().findViewById(R.id.rootView)
                )
                is ResetPasswordScreenState.SendToEmailSuccess -> {
                    code = it.code
                    currentScreenState.postValue(ResetNavigationState.SecondScreen)
                }

            }
        }
        currentScreenState.observe(viewLifecycleOwner) {
            with(binding) {
                when(it) {
                    is ResetNavigationState.FirstScreen -> {
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
                    is ResetNavigationState.SecondScreen -> {
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
                    is ResetNavigationState.ThirdScreen -> {
                        emailAndPasswordLayout.visibility = View.VISIBLE
                        emailLayout.visibility = View.GONE
                        passwordLayout.visibility = View.VISIBLE
                        codeLayout.visibility = View.GONE

                        val advancedTextView = progressBarSignUp
                        advancedTextView.setValue(100)
                        binding.nextButtonResetPassword.setOnClickListener {
                            if (binding.passwordEt.text.isNullOrBlank()){
                                TrackingUtility.showSnackbar(
                                    "Введите пароль",
                                    requireActivity().findViewById(R.id.rootView)
                                )
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

                    else -> {}
                }
            }
        }
    }

}



private sealed class ResetNavigationState {
    object FirstScreen: ResetNavigationState()
    object SecondScreen: ResetNavigationState()
    object ThirdScreen: ResetNavigationState()
}