package com.example.sportikitochka.presentation.auth.sign_up

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.example.sportikitochka.databinding.FragmentSignUpBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import com.example.sportikitochka.other.WeightTextWatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private val viewModel: SignUpViewModel by viewModel()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    protected lateinit var connectionLiveData: ConnectionLiveData
    private var isOnline : Boolean = false

    private var currentScreenState: MutableLiveData<SignUpNavigationState> = MutableLiveData<SignUpNavigationState>().apply {
        value = SignUpNavigationState.FirstScreen
    }
    private var image: MutableLiveData<String?> = MutableLiveData<String?>(null)
    private var imageString: String? = null

    private var isEmailPassed = false

    private var name: String? = null
    private var email: String? = null
    private var password: String? = null
    private var birthday: String? = null
    private var phone: String? = null
    private var weight: String? = null

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        image.postValue(uriToString(requireContext(), it))
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

        image.observe(viewLifecycleOwner) {
            if (it!=null){
                imageString = it
                val decodedString: ByteArray? = Base64.decode(it, Base64.DEFAULT)

                val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size ?: 0)

// Установка Bitmap в ImageView
                binding.profileImage.setImageBitmap(bitmap)
            }
        }

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

        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                SignUpScreenState.Loading -> Unit
                SignUpScreenState.AnyError -> showSnackbar("Что-то пошло не так", requireActivity().findViewById(R.id.rootView))
                SignUpScreenState.ValidationError -> showSnackbar("Данный email уже используется", requireActivity().findViewById(R.id.rootView))
                SignUpScreenState.ValidationSuccess -> currentScreenState.postValue(
                    SignUpNavigationState.ThirdScreen
                )
                SignUpScreenState.Success -> {
                    showSnackbar("Регистрация прошла успешно", requireActivity().findViewById(R.id.rootView))
                    findNavController().navigate(
                        R.id.action_signUpFragment_to_signInFragment,
                        savedInstanceState
                    )
                }
                else -> Unit
            }
        }
        currentScreenState.observe(viewLifecycleOwner) {
            with(binding) {
                when(it) {
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

                    else -> {}
                }
            }
        }
    }

    private fun signUpFirstScreen() = with(binding) {

        name = signUpName.text.toString()
        birthday = signUpBirthday.text.toString()
        phone = signUpPhone.text.toString()
        weight = signUpCalories.text.toString()

        if (imageString==null){
            showSnackbar("Выберите фото профиля", requireActivity().findViewById(R.id.rootView))
        }
        else
            if (!viewModel.isInputNameValid(name)){
                showSnackbar("Неверно введено имя", requireActivity().findViewById(R.id.rootView))
            }
            else if (!viewModel.isInputDateValid(birthday)) {
                showSnackbar("Неверно введена дата рождения", requireActivity().findViewById(R.id.rootView))
            }
            else if (!viewModel.isInputPhoneValid(phone)) {
                showSnackbar("Неверно введен телефонный номер", requireActivity().findViewById(R.id.rootView))
            }
            else if (!viewModel.isInputWeightValid(weight)) {
                showSnackbar("Неверно введен вес", requireActivity().findViewById(R.id.rootView))
            }
            else {
                currentScreenState.postValue(SignUpNavigationState.SecondScreen)
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


        if (!viewModel.isInputPasswordValid(password)){
            showSnackbar("Неверно введен пароль, минимальная длина равна 4", requireActivity().findViewById(R.id.rootView))
        }
        else {
            viewModel.signUp(
                name!!, email!!, password!!, birthday!!, phone!!, weight!!, imageString!!
            )
        }
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

}



private sealed class SignUpNavigationState {
    object FirstScreen: SignUpNavigationState()
    object SecondScreen: SignUpNavigationState()
    object ThirdScreen: SignUpNavigationState()
}