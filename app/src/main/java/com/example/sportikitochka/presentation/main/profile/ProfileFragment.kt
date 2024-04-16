package com.example.sportikitochka.presentation.main.profile

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.databinding.FragmentProfileBinding
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.roundFloat
import com.example.sportikitochka.presentation.auth.AuthActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModel()
    private val isPremium: Boolean = false
    private val isAdmin: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadProfileForWeek()
        binding.spTimeProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> viewModel.loadProfileForWeek()
                    1 -> viewModel.loadProfileForMonth()
                    2 -> viewModel.loadProfileForYear()
                    3 -> viewModel.loadProfileForAllTime()
                }
            }
        }

        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ScreenProfileState.Loading -> {
                    binding.contentLayout.visibility = View.GONE
                    binding.loadingLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                }
                ScreenProfileState.Success -> {
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                }
                ScreenProfileState.Error -> {
                    binding.contentLayout.visibility = View.GONE
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                }
            }
        }

        viewModel.userInfo.observe(viewLifecycleOwner) {
            val decodedString: ByteArray? = Base64.decode(it.image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size ?: 0)
            binding.profileImageProfileFragment.setImageBitmap(bitmap)

            binding.usernameTv.text = it.name
            binding.distanseTv.text = roundFloat(it.statistics.totalDistanceInMeters.toFloat()/1000f, 3).toString()
            binding.totalKcalBurnedTv.text = it.statistics.totalCalories.toString()
            binding.totalTimeValueTv.text = TrackingUtility.getFormattedStopWatchTime(it.statistics.totalTime)
        }
        when(viewModel.getUserRole()) {
            UserType.Admin -> {
                binding.premiumIconRatingItemIv.visibility = View.GONE
                binding.adminIconRatingItemIv.visibility = View.VISIBLE
                binding.containerCardProfile.visibility = View.GONE
                binding.containerPremium.visibility = View.GONE
                binding.borderView.visibility = View.GONE
            }
            UserType.Premium -> {
                binding.premiumIconRatingItemIv.visibility = View.VISIBLE
                binding.adminIconRatingItemIv.visibility = View.GONE
                binding.containerCardProfile.visibility = View.VISIBLE
                binding.containerPremium.visibility = View.VISIBLE
                binding.tvGetPremium.text = "Отменить премиум"
            }
            UserType.Normal -> {
                binding.premiumIconRatingItemIv.visibility = View.GONE
                binding.adminIconRatingItemIv.visibility = View.GONE
                binding.containerCardProfile.visibility = View.VISIBLE
                binding.containerPremium.visibility = View.VISIBLE
                binding.tvGetPremium.text = "Купить премиум-подписку"
            }
            else -> Unit
        }

        binding.tvGetPremium.text = if (isPremium) "Отменить премиум" else "Купить премиум-подписку"
        binding.personalDataContainer.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_editProfileFragment,
                savedInstanceState
            )
        }
        binding.containerPremium.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_paymentFragment,
                savedInstanceState
            )
        }
        binding.signOutButton.setOnClickListener {
            viewModel.signOut()
            val intent = Intent(activity, AuthActivity::class.java)
            startActivity(intent) // запускаем новую активность
        }
    }

}