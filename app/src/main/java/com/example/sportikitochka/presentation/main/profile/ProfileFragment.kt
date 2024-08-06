package com.example.sportikitochka.presentation.main.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.sportikitochka.R
import com.example.data.network.EndPoints.BASE_URL
import com.example.domain.models.UserType
import com.example.sportikitochka.common.State
import com.example.sportikitochka.databinding.FragmentProfileBinding
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.roundFloat
import com.example.sportikitochka.presentation.auth.AuthActivity
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch
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
            viewModel.loadProfileInitial()
        }
        AppMetrica.reportEvent("Profile viewed")
        initObservers()
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

    fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                    with(binding) {
                        when(it.userRole){
                            UserType.Admin -> {
                                binding.premiumIconRatingItemIv.visibility = View.GONE
                                binding.adminIconRatingItemIv.visibility = View.VISIBLE
                                binding.containerCardProfile.visibility = View.GONE
                                binding.totalCard.visibility = View.GONE
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
                                        R.id.action_profileFragment_to_paymentFragment
                                    )
                                }
                            }
                        }
                        when(it.profileState){
                            is State.Error -> {
                                binding.contentLayout.visibility = View.GONE
                                binding.loadingLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                                binding.containerCardProfile.visibility = View.INVISIBLE
                                binding.containerCardProfileLoading.visibility = View.GONE
                            }
                            State.Loading -> {
                                binding.contentLayout.visibility = View.GONE
                                binding.loadingLayout.visibility = View.VISIBLE
                                binding.errorLayout.visibility = View.GONE
                                binding.containerCardProfile.visibility = View.GONE
                                binding.containerCardProfileLoading.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                Glide.with(this@ProfileFragment)
                                    .load(BASE_URL +it.profileState.value.image)
                                    .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                                    .circleCrop()
                                    .into(binding.profileImageProfileFragment)

                                binding.usernameTv.text = it.profileState.value.name
                                binding.distanseTv.text = roundFloat(it.profileState.value.statistics.totalDistanceInMeters.toFloat()/1000f, 3).toString()
                                binding.totalKcalBurnedTv.text = it.profileState.value.statistics.totalCalories.toString()
                                binding.totalTimeValueTv.text = TrackingUtility.getFormattedStopWatchTime(it.profileState.value.statistics.totalTime)
                            }
                        }
                        when(it.adminProfileState) {
                            is State.Error -> {
                                binding.contentLayout.visibility = View.GONE
                                binding.loadingLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                                binding.containerCardProfile.visibility = View.INVISIBLE
                                binding.containerCardProfileLoading.visibility = View.GONE
                            }
                            State.Loading -> {
                                binding.contentLayout.visibility = View.GONE
                                binding.loadingLayout.visibility = View.VISIBLE
                                binding.errorLayout.visibility = View.GONE
                                binding.containerCardProfile.visibility = View.GONE
                                binding.containerCardProfileLoading.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                binding.premiumIconRatingItemIv.visibility = View.VISIBLE
                                binding.adminIconRatingItemIv.visibility = View.GONE
                                binding.containerCardProfile.visibility = View.VISIBLE
                                binding.containerPremium.visibility = View.VISIBLE
                                binding.tvGetPremium.text = "Отменить премиум"
                                binding.containerPremium.setOnClickListener {
                                    AppMetrica.reportEvent("Premium canceled")
                                    viewModel.cancelPremium()
                                }

                                Glide.with(this@ProfileFragment)
                                    .load(BASE_URL +it.adminProfileState.value.image)
                                    .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                                    .circleCrop()
                                    .into(binding.profileImageProfileFragment)

                                binding.usernameTv.text = it.adminProfileState.value.name
                            }
                        }
                    }
                }
            }
        }
    }

}