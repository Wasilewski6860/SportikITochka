package com.example.sportikitochka.presentation.main.edit_profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.example.sportactivityapp.other.DateFormatTextWatcher
import com.example.sportactivityapp.other.PhoneFormatTextWatcher
import com.example.sportikitochka.R
import com.example.data.network.EndPoints
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NotFoundException
import com.example.domain.models.UserType
import com.example.sportikitochka.common.State
import com.example.sportikitochka.databinding.FragmentEditProfileBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.bitmapToFile
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import com.example.sportikitochka.other.TrackingUtility.uriToString
import com.example.sportikitochka.other.WeightTextWatcher
import com.example.sportikitochka.presentation.main.MainActivity
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditProfileViewModel by viewModel()

    private var imageString: String? = null
    private var imageBitmap: Bitmap? = null

    private var name: String? = null
    private var birthday: String? = null
    private var phone: String? = null
    private var weight: String? = null

    protected lateinit var connectionLiveData: ConnectionLiveData
    private var isOnline : Boolean = false

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        viewModel.loadImage(uriToString(requireContext(), it, requireActivity().findViewById(R.id.rootViewMain)))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        connectionLiveData = ConnectionLiveData(this.requireContext())
        connectionLiveData.observe(viewLifecycleOwner) {
            isOnline = it
        }
        AppMetrica.reportEvent("Edit profile screen opened")

        initObservers()
        initListeners()
        viewModel.loadUserData()

        if (viewModel.getUserRole() == UserType.Admin) {
            binding.layoutSignUpCalories.visibility = View.GONE
        }
    }

    private fun initListeners() {
        binding.signUpBirthday.addTextChangedListener(DateFormatTextWatcher(binding.signUpBirthday))
        binding.signUpPhone.addTextChangedListener(PhoneFormatTextWatcher(binding.signUpPhone))
        binding.signUpCalories.addTextChangedListener(WeightTextWatcher(binding.signUpCalories))

        binding.editProfileNewImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.nextBtnSignUp.setOnClickListener {
            editData()
        }
    }


    private fun editData() = with(binding) {

        name = signUpName.text.toString()
        birthday = signUpBirthday.text.toString()
        phone = signUpPhone.text.toString()
        weight = signUpCalories.text.toString()

        if (imageString.isNullOrEmpty()){
            showSnackbar("Выберите фото профиля", requireActivity().findViewById(R.id.rootViewMain))
        }
        else
            if (!viewModel.isInputNameValid(name)){
                showSnackbar("Неверно введено имя, используйте кириллические и латинские буквы и цифры", requireActivity().findViewById(R.id.rootViewMain))
            }
            else if (!viewModel.isInputDateValid(birthday)) {
                showSnackbar("Неверно введена дата рождения, введите в формате дд.мм.гггг", requireActivity().findViewById(R.id.rootViewMain))
            }
            else if (!viewModel.isInputPhoneValid(phone)) {
                showSnackbar("Неверно введен телефонный номер", requireActivity().findViewById(R.id.rootViewMain))
            }
            else if (!viewModel.isInputWeightValid(weight) && viewModel.getUserRole() != UserType.Admin) {
                showSnackbar("Неверно введен вес", requireActivity().findViewById(R.id.rootViewMain))
            }
            else {
                viewModel.changeUserData(
                    name = name!!, image = bitmapToFile(name!!,requireContext(), imageBitmap!!), weight = weight!!.toFloat(), phone = phone!!, birthday!!
                )
            }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                    with(binding) {
                        when(it.userDataState) {
                            is State.Error -> {
                                when(it.userDataState.error) {
                                    is IncorrectTokenException -> (requireActivity() as MainActivity).showSnackbar("Время сессии истекло, пожалуйста, перезайдите в приложение")
                                    is ForbiddenException -> (requireActivity() as MainActivity).showSnackbar("У вас нет доступа")
                                    is NotFoundException -> (requireActivity() as MainActivity).showSnackbar("Указанный пользователь не найден")
                                    else -> (requireActivity() as MainActivity).showSnackbar("Что-то пошло не так. Связаться с нами: sportikitocka@gmail.com")
                                }
                            }
                            State.Loading -> {
                                loadingLayout.visibility = View.VISIBLE
                                editProfileLayout.visibility = View.GONE
                                errorLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                editProfileLayout.visibility = View.VISIBLE
                                loadingLayout.visibility = View.GONE
                                errorLayout.visibility = View.GONE

                                Glide.with(this@EditProfileFragment)
                                    .load(com.example.data.network.EndPoints.BASE_URL +it.image)
                                    .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                                    .circleCrop()
                                    .into(binding.profileImage)

                                Glide.with(this@EditProfileFragment)
                                    .asBitmap()
                                    .load(com.example.data.network.EndPoints.BASE_URL +it.image)
                                    .into(object : SimpleTarget<Bitmap>() {
                                        override fun onResourceReady(
                                            resource: Bitmap,
                                            transition: Transition<in Bitmap>?
                                        ) {
                                            imageBitmap = resource
                                        }

                                    })
                                viewModel.loadImage(it.userDataState.value.image)
                                signUpName.setText(it.userDataState.value.name)

                                val inputFormat = SimpleDateFormat("yyyy-MM-dd")
                                val outputFormat = SimpleDateFormat("dd.MM.yyyy")
                                val date = inputFormat.parse(it.userDataState.value.birthday)
                                val outputDate = outputFormat.format(date)

                                signUpBirthday.setText(outputDate)
                                signUpPhone.setText(it.userDataState.value.phone)
                                signUpCalories.setText(it.userDataState.value.weight.toString())
                            }
                        }

                        when(it.changeDataState) {
                            is State.Error -> {
                                when(it.changeDataState.error) {
                                    is IncorrectInputException -> (requireActivity() as MainActivity).showSnackbar("Неверно указаны данные")
                                    is IncorrectTokenException -> (requireActivity() as MainActivity).showSnackbar("Время сессии истекло, пожалуйста, перезайдите в приложение")
                                    is ForbiddenException -> (requireActivity() as MainActivity).showSnackbar("У вас нет доступа")
                                    is NotFoundException -> (requireActivity() as MainActivity).showSnackbar("Указанный пользователь не найден")
                                    else -> (requireActivity() as MainActivity).showSnackbar("Что-то пошло не так. Связаться с нами: sportikitocka@gmail.com")
                                }
                            }
                            State.Loading -> {
                                loadingLayout.visibility = View.VISIBLE
                                editProfileLayout.visibility = View.GONE
                                errorLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                AppMetrica.reportEvent("Profile changed")
                                findNavController().navigate(
                                    R.id.action_editProfileFragment_to_profileFragment
                                )
                            }
                        }

                        if (it.image!=null){
                            imageString = it.image
                            val decodedString: ByteArray? = Base64.decode(it.image, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size ?: 0)
                            imageBitmap = bitmap
                            binding.profileImage.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }
    }


}