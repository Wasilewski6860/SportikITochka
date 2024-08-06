package com.example.sportikitochka.presentation.main.all_activities

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.network.error.AlreadyUsedException
import com.example.data.network.error.IncorrectInputException
import com.example.domain.models.UserType
import com.example.sportikitochka.R
import com.example.sportikitochka.common.State
import com.example.sportikitochka.databinding.FragmentAllActivitiesBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import com.example.sportikitochka.presentation.auth.AuthActivity
import com.example.sportikitochka.presentation.auth.sign_up.SignUpNavigationState
import com.example.sportikitochka.presentation.main.main.MainAdapter
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class AllActivitiesFragment : Fragment() {

    companion object {
        fun newInstance() = AllActivitiesFragment()
    }

    private val viewModel: AllActivitiesViewModel by viewModel()
    private var _binding: FragmentAllActivitiesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainAdapter: MainAdapter

    protected lateinit var connectionLiveData: ConnectionLiveData

    private var isOnline : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllActivitiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this.requireContext())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        connectionLiveData.observe(viewLifecycleOwner) {
            isOnline = it
            viewModel.fetchActivities()
            if (!it) {
                showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootViewMain))
            }
        }

        setupRecyclerView()
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                    with(binding) {
                       when(it.activitiesState) {
                           is State.Error -> showSnackbar("Произошла ошибка при интернет-соединении", requireActivity().findViewById(R.id.rootViewMain))
                           State.Loading -> {
                               binding.loadingLayout.visibility = View.VISIBLE
                               binding.contentLayout.visibility = View.GONE
                               binding.errorLayout.visibility = View.GONE
                               binding.emptyLayout.visibility = View.GONE
                           }
                           State.NotStarted -> Unit
                           is State.Success -> {
                               binding.loadingLayout.visibility = View.GONE
                               binding.contentLayout.visibility = View.VISIBLE
                               binding.errorLayout.visibility = View.GONE
                               binding.emptyLayout.visibility = View.GONE
                               mainAdapter.submitList(it.activitiesState.value)
                           }
                       }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() = binding.recyclerAllActivities.apply {
        mainAdapter = MainAdapter(requireContext())
        adapter = mainAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

}