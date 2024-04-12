package com.example.sportikitochka.presentation.main.select_activity_type

import android.Manifest
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.sportikitochka.databinding.FragmentSelectActivityTypeBinding
import com.example.sportikitochka.other.ActivityType
import com.example.sportikitochka.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.sportikitochka.other.TrackingUtility
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class SelectActivityTypeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    // Image Slider View https://github.com/Ajay-2022-Soft-Tech/Image-Slider-View-by-Recycler-view.git
    companion object {
        fun newInstance() = SelectActivityTypeFragment()
    }

    private lateinit var viewModel: SelectActivityTypeViewModel
    private lateinit var typeAdapterHorizontal: SelectActivityTypeAdapter
    private lateinit var typeAdapterVertical: SelectActivityTypeVerticalAdapter
    private var _binding: FragmentSelectActivityTypeBinding? = null
    private val binding get() = _binding!!
    val snapHelper: SnapHelper = LinearSnapHelper()

    var activityType: String = ActivityType.RUNNING.toString()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectActivityTypeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectActivityTypeViewModel::class.java)
        requestPermissions()
        binding.startActivityBtn.setOnClickListener {
            val action =
                SelectActivityTypeFragmentDirections.actionSelectActivityTypeFragmentToTrackingFragment(
                    activityType = activityType
                )
            findNavController().navigate(action)

        }

        setupRecyclerViewHorizontal()
        setupRecyclerViewVertical()

        val types = listOf<ActivityType>(
            ActivityType.RUNNING,
            ActivityType.SWIMMING,
            ActivityType.BYCICLE
        )

        binding.rightArrow.setOnClickListener {
            binding.recyclerHorizontalContainer.visibility = View.GONE
            binding.recyclerVerticalContainer.visibility = View.VISIBLE
        }
        binding.leftArrow.setOnClickListener {
            binding.recyclerHorizontalContainer.visibility = View.VISIBLE
            binding.recyclerVerticalContainer.visibility = View.GONE
        }
        typeAdapterHorizontal.submitList(types)
        typeAdapterVertical.submitList(types)

    }

    private fun setupRecyclerViewHorizontal() = binding.recyclerViewHorizontal.apply {
        typeAdapterHorizontal = SelectActivityTypeAdapter()
        adapter = typeAdapterHorizontal
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

        snapHelper.attachToRecyclerView(this)

        val layoutManager = this.layoutManager

        // Отслеживаем прокрутку Recycler View
        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // Проверяем, если прокрутка останавливается
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Получаем элемент в фокусе
                    val centerView = snapHelper.findSnapView(layoutManager)


                    // Получаем позицию элемента в адаптере
                    val centerPosition: Int = centerView?.let { layoutManager?.getPosition(it) } ?: 0

                    // Получаем данные элемента из адаптера
                    val currentItem: ActivityType = typeAdapterHorizontal.getActivityItem(centerPosition)
                    activityType = currentItem.activityName
                    binding.selectedActivityTypeValueTv.text = currentItem.activityName
                }
            }
        })
    }

    private fun setupRecyclerViewVertical() = binding.recyclerViewVertical.apply {
        typeAdapterVertical = SelectActivityTypeVerticalAdapter(
            object : SelectActivityTypeVerticalAdapter.ActivityTypeActionListener {

                override fun onClickItem(activityType: ActivityType) {
                    binding.recyclerHorizontalContainer.visibility = View.VISIBLE
                    binding.recyclerVerticalContainer.visibility = View.GONE

                    binding.recyclerViewHorizontal.smoothScrollToPosition(activityType.id)

                }
            }
        )
        adapter = typeAdapterVertical
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)


    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}

