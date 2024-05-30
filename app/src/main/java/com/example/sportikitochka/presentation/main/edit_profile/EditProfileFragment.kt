package com.example.sportikitochka.presentation.main.edit_profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.example.sportactivityapp.other.DateFormatTextWatcher
import com.example.sportactivityapp.other.PhoneFormatTextWatcher
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.network.EndPoints
import com.example.sportikitochka.databinding.FragmentEditProfileBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.bitmapToFile
import com.example.sportikitochka.other.TrackingUtility.roundFloat
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import com.example.sportikitochka.other.TrackingUtility.uriToString
import com.example.sportikitochka.other.WeightTextWatcher
import com.example.sportikitochka.presentation.main.profile.ProfileViewModel
import io.appmetrica.analytics.AppMetrica
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditProfileViewModel by viewModel()

    private var image: MutableLiveData<String?> = MutableLiveData<String?>(null)
    private var imageString: String? = null
    private var imageBitmap: Bitmap? = null

    private var name: String? = null
    private var birthday: String? = null
    private var phone: String? = null
    private var weight: String? = null

    protected lateinit var connectionLiveData: ConnectionLiveData
    private var isOnline : Boolean = false

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        image.postValue(uriToString(requireContext(), it, requireActivity().findViewById(R.id.rootViewMain)))
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
        image.observe(viewLifecycleOwner) {
            if (it!=null){
                imageString = it
                val decodedString: ByteArray? = Base64.decode(it, Base64.DEFAULT)

                val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size ?: 0)
                imageBitmap = bitmap
// Установка Bitmap в ImageView
                binding.profileImage.setImageBitmap(bitmap)
            }
        }

        viewModel.loadUserData()
        viewModel.userInfo.observe(viewLifecycleOwner) {
            with(binding) {
//                val decodedString: ByteArray? = Base64.decode(it.image, Base64.DEFAULT)
//                val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size ?: 0)
//                profileImage.setImageBitmap(bitmap)
                Glide.with(this@EditProfileFragment)
                    .load(EndPoints.BASE_URL +it.image)
                    .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                    .circleCrop()
                    .into(binding.profileImage)

                Glide.with(this@EditProfileFragment)
                    .asBitmap()
                    .load(EndPoints.BASE_URL +it.image)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            imageBitmap = resource
                        }

                    })

                image.postValue(it.image)
                signUpName.setText(it.name)
                val inputFormat = SimpleDateFormat("yyyy-MM-dd")
                val outputFormat = SimpleDateFormat("dd.MM.yyyy")

                val date = inputFormat.parse(it.birthday)
                val outputDate = outputFormat.format(date)
                signUpBirthday.setText(outputDate)
                signUpPhone.setText(it.phone)
                signUpCalories.setText(it.weight.toString())
            }
        }
        if (viewModel.getUserRole() == UserType.Admin) {
            binding.layoutSignUpCalories.visibility = View.GONE
        }
        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ScreenEditProfileState.Error -> {
                    binding.editProfileLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    TrackingUtility.showSnackbar(
                        "Не удалось сохранить изменения",
                        requireActivity().findViewById(R.id.rootViewMain)
                    )
                }
                ScreenEditProfileState.Loading -> {
                    binding.editProfileLayout.visibility = View.GONE
                    binding.loadingLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                }
                ScreenEditProfileState.LoadingError -> {
                    binding.editProfileLayout.visibility = View.GONE
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                }
                ScreenEditProfileState.LoadingSuccess -> {
                    binding.editProfileLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                }
                ScreenEditProfileState.LoadingSuccessLocal -> {
                    binding.editProfileLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    TrackingUtility.showSnackbar(
                        "Не удалось сохранить данные из сети",
                        requireActivity().findViewById(R.id.rootViewMain)
                    )
                }
                ScreenEditProfileState.Success -> {
                    AppMetrica.reportEvent("Profile changed")
                    findNavController().navigate(
                        R.id.action_editProfileFragment_to_profileFragment,
                        savedInstanceState
                    )
                }
            }
        }

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
                showSnackbar("Неверно введено имя", requireActivity().findViewById(R.id.rootViewMain))
            }
            else if (!viewModel.isInputDateValid(birthday)) {
                showSnackbar("Неверно введена дата рождения", requireActivity().findViewById(R.id.rootViewMain))
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

}