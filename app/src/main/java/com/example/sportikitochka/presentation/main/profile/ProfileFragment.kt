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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.network.EndPoints.BASE_URL
import com.example.sportikitochka.databinding.FragmentProfileBinding
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.roundFloat
import com.example.sportikitochka.presentation.auth.AuthActivity
import io.appmetrica.analytics.AppMetrica
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

        viewModel.getUserRole()
        if (viewModel.getRole() == UserType.Admin) {
            viewModel.loadAdminProfile()
        }
        else {
            viewModel.loadProfileForWeek()
        }
        AppMetrica.reportEvent("Profile viewed")
        binding.spTimeProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (viewModel.getRole() != UserType.Admin)
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
            Glide.with(this@ProfileFragment)
                .load(BASE_URL +it.image)
                .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                .circleCrop()
                .into(binding.profileImageProfileFragment)

            binding.usernameTv.text = it.name
            binding.distanseTv.text = roundFloat(it.statistics.totalDistanceInMeters.toFloat()/1000f, 3).toString()
            binding.totalKcalBurnedTv.text = it.statistics.totalCalories.toString()
            binding.totalTimeValueTv.text = TrackingUtility.getFormattedStopWatchTime(it.statistics.totalTime)
        }

        viewModel.adminInfo.observe(viewLifecycleOwner) {
            Glide.with(this@ProfileFragment)
                .load(BASE_URL +it.image)
                .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                .circleCrop()
                .into(binding.profileImageProfileFragment)

            binding.usernameTv.text = it.name
        }

        viewModel.userRole.observe(viewLifecycleOwner) {
            when(it) {
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
                    binding.containerPremium.setOnClickListener {
                        AppMetrica.reportEvent("Premium canceled")
                        viewModel.cancelPremium()
                    }
                }
                UserType.Normal -> {
                    binding.premiumIconRatingItemIv.visibility = View.GONE
                    binding.adminIconRatingItemIv.visibility = View.GONE
                    binding.containerCardProfile.visibility = View.VISIBLE
                    binding.containerPremium.visibility = View.VISIBLE
                    binding.tvGetPremium.text = "Купить премиум-подписку"
                    binding.containerPremium.setOnClickListener {
                        AppMetrica.reportEvent("Profile screen opened")
                        findNavController().navigate(
                            R.id.action_profileFragment_to_paymentFragment,
                            savedInstanceState
                        )
                    }
                }
                else -> Unit
            }
        }


        binding.personalDataContainer.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_editProfileFragment,
                savedInstanceState
            )
        }


        binding.errorLayoutButton.setOnClickListener {
            viewModel.loadProfileForWeek()
            viewModel.getUserRole()
        }
        binding.signOutButton.setOnClickListener {
            viewModel.signOut()
            val intent = Intent(activity, AuthActivity::class.java)
            startActivity(intent) // запускаем новую активность
        }
    }

}