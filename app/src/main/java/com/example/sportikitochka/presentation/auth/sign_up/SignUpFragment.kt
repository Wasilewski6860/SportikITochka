package com.example.sportikitochka.presentation.auth.sign_up

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.sportactivityapp.other.DateFormatTextWatcher
import com.example.sportactivityapp.other.PhoneFormatTextWatcher
import com.example.sportikitochka.R
import com.example.data.network.error.AlreadyUsedException
import com.example.data.network.error.IncorrectInputException
import com.example.domain.models.UserType
import com.example.sportikitochka.common.State
import com.example.sportikitochka.databinding.FragmentSignUpBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import com.example.sportikitochka.other.WeightTextWatcher
import com.example.sportikitochka.presentation.auth.AuthActivity
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private val viewModel: SignUpViewModel by viewModel()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    protected lateinit var connectionLiveData: ConnectionLiveData
    private var isOnline : Boolean = false

    private var imageString: String? = null
    private var imageBitmap: Bitmap? = null

    private var isEmailPassed = false

    private var isAdmin: Boolean = false
    private var name: String? = null
    private var email: String? = null
    private var password: String? = null
    private var birthday: String? = null
    private var phone: String? = null
    private var weight: String? = null

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        viewModel.loadImage(uriToString(requireContext(), it))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this.requireContext())
        connectionLiveData.observe(viewLifecycleOwner) {
            isOnline = it
        }

        initListeners()
        setupObservers()
    }

    private fun signUpFirstScreen() = with(binding) {

        name = signUpName.text.toString()
        birthday = signUpBirthday.text.toString()
        phone = signUpPhone.text.toString()
        weight = signUpCalories.text.toString()
        isAdmin = isAdminCheckBox.isChecked()

        if (imageString==null){
            showSnackbar("Выберите фото профиля", requireActivity().findViewById(R.id.rootView))
        }
        else
            if (!viewModel.isInputNameValid(name)) showSnackbar("Неверно введено имя", requireActivity().findViewById(R.id.rootView))
            else if (!viewModel.isInputDateValid(birthday)) showSnackbar("Неверно введена дата рождения", requireActivity().findViewById(R.id.rootView))
            else if (!viewModel.isInputPhoneValid(phone)) showSnackbar("Неверно введен телефонный номер", requireActivity().findViewById(R.id.rootView))
            else if (!viewModel.isInputWeightValid(weight)) showSnackbar("Неверно введен вес", requireActivity().findViewById(R.id.rootView))
            else {
                viewModel.navigateToScreen(SignUpNavigationState.SecondScreen)
            }

    }

    private fun signUpSecondScreen() = with(binding) {
        email = emailEt.text.toString()

        if (!viewModel.isInputEmailValid(email)) {
            showSnackbar("Неверно введен email", requireActivity().findViewById(R.id.rootView))
        }
        else {
            viewModel.validateEmail(email!!)
        }
    }

    private fun signUpThirdScreen() = with(binding) {
        password = passwordEt.text.toString()

        if (!viewModel.isInputPasswordValid(password)) showSnackbar("Неверно введен пароль, минимальная длина равна 4", requireActivity().findViewById(R.id.rootView))
        else viewModel.signUp(
                name!!, email!!, password!!, birthday!!, phone!!, weight!!, TrackingUtility.bitmapToFile(
                    name!!,
                    requireContext(),
                    imageBitmap!!
                ), isAdmin
            )
    }

    fun uriToString(context: Context, imageUri: Uri?): String? {
        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null
        var string: String? =null

        try {
            inputStream = imageUri?.let { context.contentResolver.openInputStream(it) }
            bitmap = BitmapFactory.decodeStream(inputStream)
            string = bitmapToString(bitmap)
        } catch (e: FileNotFoundException) {
            showSnackbar("Файл не найден", requireActivity().findViewById(R.id.rootView))
        }
        catch (e: Exception) {
            showSnackbar("Что-то пошло не так", requireActivity().findViewById(R.id.rootView))
        }finally {
            inputStream?.close()
        }

        return string
    }

    fun bitmapToString(bitmap: Bitmap): String = runBlocking {
        return@runBlocking withContext(Dispatchers.Default) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val encodedString: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            return@withContext encodedString
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                    with(binding) {
                        if (it.image!=null){
                            imageString = it.image
                            val decodedString: ByteArray? = Base64.decode(it.image, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size ?: 0)
                            imageBitmap = bitmap
                            binding.profileImage.setImageBitmap(bitmap)
                        }
                        when(it.registrationState) {
                            is State.Error -> {
                                when(it.registrationState.error) {
                                    is IncorrectInputException -> (requireActivity() as AuthActivity).showSnackbar("Неверные данные. Связаться с нами: sportikitocka@gmail.com")
                                    is AlreadyUsedException -> (requireActivity() as AuthActivity).showSnackbar("Данный email уже используется")
                                    else -> (requireActivity() as AuthActivity).showSnackbar("Что-то пошло не так. Проверьте корректность введенного")
                                }
                            }
                            State.Loading -> Unit
                            State.NotStarted -> Unit
                            is State.Success -> {
                                val eventParameters: MutableMap<String, Any> = HashMap()
                                eventParameters["name"] = name.toString()
                                eventParameters["role"] = if (isAdmin) UserType.Admin.toString() else UserType.Normal.toString()
                                AppMetrica.reportEvent("New user", eventParameters)
                                showSnackbar("Регистрация прошла успешно", requireActivity().findViewById(R.id.rootView))
                                findNavController().navigate(
                                    R.id.action_signUpFragment_to_signInFragment
                                )
                            }
                        }
                        when(it.validationState) {
                            is State.Error -> {
                                (requireActivity() as AuthActivity).showSnackbar("Что-то пошло не так. Проверьте корректность ввода")
                            }
                            State.Loading -> Unit
                            State.NotStarted -> Unit
                            is State.Success -> viewModel.navigateToScreen(SignUpNavigationState.ThirdScreen)
                        }
                        when(it.navigationState) {
                            is SignUpNavigationState.FirstScreen -> {
                                createAccountLayout.visibility = View.VISIBLE
                                emailAndPasswordLayout.visibility = View.GONE
                                emailLayout.visibility = View.GONE
                                passwordLayout.visibility = View.GONE
                                val advancedTextView = progressBarFirst
                                advancedTextView.setValue(33)
                            }
                            is SignUpNavigationState.SecondScreen -> {
                                createAccountLayout.visibility = View.GONE
                                emailAndPasswordLayout.visibility = View.VISIBLE
                                emailLayout.visibility = View.VISIBLE
                                passwordLayout.visibility = View.GONE
                                val advancedTextView = progressBarSignUp
                                advancedTextView.setValue(66)
                                stepTv.text = "2 из 3"
                                binding.nextButtonSignUp.setOnClickListener {
                                    signUpSecondScreen()
                                }
                            }
                            is SignUpNavigationState.ThirdScreen -> {
                                createAccountLayout.visibility = View.GONE
                                emailAndPasswordLayout.visibility = View.VISIBLE
                                emailLayout.visibility = View.GONE
                                passwordLayout.visibility = View.VISIBLE
                                val advancedTextView = progressBarSignUp
                                advancedTextView.setValue(100)
                                stepTv.text = "3 из 3"
                                binding.nextButtonSignUp.setOnClickListener {
                                    signUpThirdScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private fun initListeners() {
        binding.signUpBirthday.addTextChangedListener(DateFormatTextWatcher(binding.signUpBirthday))
        binding.signUpPhone.addTextChangedListener(PhoneFormatTextWatcher(binding.signUpPhone))
        binding.signUpCalories.addTextChangedListener(WeightTextWatcher(binding.signUpCalories))
        binding.switchPasswordVisibility.setOnClickListener {
            if (binding.switchPasswordVisibility.isChecked) {
                binding.passwordEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.passwordEt.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        binding.editProfileNewImage.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.nextBtnSignUp.setOnClickListener {
            if (isOnline){
                signUpFirstScreen()
            }
            else{
                showSnackbar("No Internet Connection", requireActivity().findViewById(R.id.rootView))
            }
        }
    }
}



sealed class SignUpNavigationState {
    object FirstScreen: SignUpNavigationState()
    object SecondScreen: SignUpNavigationState()
    object ThirdScreen: SignUpNavigationState()
}