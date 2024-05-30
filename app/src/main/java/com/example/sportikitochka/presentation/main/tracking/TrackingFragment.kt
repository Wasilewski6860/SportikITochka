package com.example.sportikitochka.presentation.main.tracking

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.FragmentTrackingBinding
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.other.ActivityType
import com.example.sportikitochka.other.Constants.ACTION_PAUSE_SERVICE
import com.example.sportikitochka.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.sportikitochka.other.Constants.ACTION_STOP_SERVICE
import com.example.sportikitochka.other.Constants.MAP_ZOOM
import com.example.sportikitochka.other.Polyline
import com.example.sportikitochka.other.TrackingService
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.bitmapToFile
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import com.example.sportikitochka.other.isGpsEnabled
import com.example.sportikitochka.other.mapToActivityType
import com.example.sportikitochka.presentation.main.main.MainViewModel
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.snackbar.Snackbar
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.util.Calendar
import kotlin.math.round
import org.koin.androidx.viewmodel.ext.android.viewModel
import world.mappable.mapkit.Animation
import world.mappable.mapkit.MapKitFactory
import world.mappable.mapkit.geometry.BoundingBox
import world.mappable.mapkit.geometry.Geometry
import world.mappable.mapkit.geometry.Point
import world.mappable.mapkit.map.CameraPosition
import world.mappable.mapkit.map.MapObjectCollection
import java.text.SimpleDateFormat
import java.util.Locale

class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: TrackingViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!


    private var timeInMillis = 0L
    private var speedInMps = 0F
    private var calories = 0L
    private var distanceInMeters = 0

    private var menu: Menu? = null

    var weight = 80f

    private lateinit var collection: MapObjectCollection

    private  var activityType: ActivityType = ActivityType.RUNNING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            activityType = it.getString("activityType")?.mapToActivityType() ?: ActivityType.RUNNING
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        _binding = FragmentTrackingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Add a nested map objects collection
        collection = binding.mapView.map.mapObjects.addCollection()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapKitFactory.initialize(requireContext());
        AppMetrica.reportEvent("Tracking screen viewed")
        binding.tvTrackingTimeInfo.text = when(activityType) {
            ActivityType.RUNNING -> "Время пробежки"
            ActivityType.BYCICLE -> "Время велозаезда"
            ActivityType.SWIMMING -> "Время заплыва"
        }
        binding.pauseButton.setOnClickListener {
            toggleRun()
        }

        binding.stopButton.setOnClickListener {
            if (this.requireContext().isGpsEnabled) {
                zoomToSeeWholeTrack()
                endRunAndSaveToDb()
            }
            else {
                showSnackbar("Включите геолокацию устройства", requireActivity().findViewById(R.id.rootViewMain))
            }
        }

        addAllPolylines()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {

        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ScreenTrackingState.Success -> Unit
                is ScreenTrackingState.Error -> {
                    showSnackbar(it.message.toString(), requireActivity().findViewById(R.id.rootViewMain))
                }
                else -> Unit
            }
        }

        viewModel.userInfo.observe(viewLifecycleOwner) {
            weight = it.weight
        }

        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints.addAll(it)
            Log.d("POINTS",pathPoints.size.toString())
            addLatestPolyline()
            moveCameraToUser()
//            zoomToSeeWholeTrack()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            timeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(timeInMillis)
            binding.tvTrackingTime.text = formattedTime
        })

        TrackingService.distanceInMeters.observe(viewLifecycleOwner, Observer {
            binding.distanseTv.text = roundFloat((it/1000f),3).toString()
            distanceInMeters = it
            binding.kcalBurnedTv.text = roundFloat((0.75f * weight)*(it / 1000f),3).toString()
            calories = ((0.75f * weight)*(it / 1000f)).toLong()
        })

        TrackingService.speedInMps.observe(viewLifecycleOwner, Observer {
            binding.speedValueTv.text = roundFloat(it,3).toString()
            speedInMps = roundFloat(it,3)
        })
    }

    private fun toggleRun() {
        if(isTracking) {
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        mainViewModel.fetchActivities()
        findNavController().navigate(R.id.action_trackingFragment_to_mainFragment2)
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if(!isTracking) {
            binding.pauseButton.setImageResource(R.drawable.ic_play)
//            binding.btnToggleRun.text = "Start"
//            binding.btnFinishRun.visibility = View.VISIBLE
        } else {
            binding.pauseButton.setImageResource(R.drawable.ic_pause)
//            binding.btnToggleRun.text = "Stop"
//            menu?.getItem(0)?.isVisible = true
//            binding.btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser() {



        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {

//            val cameraPosition = CameraPosition(Point(pathPoints.last().last().latitude,pathPoints.last().last().longitude), MAP_ZOOM, 0.0F, 0.0F)
//            // Создаем анимацию перемещения камеры
//            val mapAnimations = Animation(Animation.Type.SMOOTH, 1F)
//
//            // Focus camera on polyline
//            binding.mapView.map.move(cameraPosition, mapAnimations) {
//
//            }

            binding.mapView.map?.move(
                CameraPosition(
                    Point(pathPoints.last().last().latitude,pathPoints.last().last().longitude), MAP_ZOOM,0.0f,0.0f
                ),
                Animation(
                    Animation.Type.SMOOTH,1f
                ),
                null
            )
        }
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints) {
            for(pos in polyline) {
                bounds.include(pos)
            }
        }

        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {

            var northeast = bounds.build().northeast
            var southwest = bounds.build().southwest

            var boundingBox = BoundingBox(Point(southwest.latitude,southwest.longitude), Point(northeast.latitude,northeast.longitude)) // getting BoundingBox between two points
            var geometry: Geometry = Geometry.fromBoundingBox(boundingBox)
            var cameraPosition = binding.mapView.map?.cameraPosition(geometry) // getting cameraPosition
            var target = cameraPosition?.target ?: Point(pathPoints.last().last().latitude,pathPoints.last().last().longitude)
            cameraPosition = CameraPosition(target, (cameraPosition?.zoom ?: MAP_ZOOM) - 0.8f,
                cameraPosition?.azimuth ?: 0.0F, cameraPosition?.tilt ?: 0.0F) // Zoom 80%
            binding.mapView.map?.move(cameraPosition, Animation(Animation.Type.SMOOTH, 0f), null) // move camera

        }
        else {
            showSnackbar("Не удалось определить ваше местоположение", requireActivity().findViewById(R.id.rootViewMain))

        }
//        map?.moveCamera(
//            CameraUpdateFactory.newLatLngBounds(
//                bounds.build(),
//                binding.mapView.width,
//                binding.mapView.height,
//                ( binding.mapView.height * 0.05f).toInt()
//            )
//        )
    }

    private fun endRunAndSaveToDb() {

        val enabled: Boolean = binding.mapView.isDrawingCacheEnabled()

        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            val bitmap = binding.mapView.screenshot

            val string = bitmapToString(bitmap?: throw Exception(""))


            var distanceInMeters: Long = 0L
            for(polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }

            val avgSpeed = round((distanceInMeters / 1000f) / (timeInMillis / 1000f / 60 / 60) * 10) / 10f
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toLong()

            val calendar = Calendar.getInstance().apply {
                timeInMillis = dateTimestamp
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val image = bitmapToFile(dateFormat.format(calendar.time),requireContext(), bitmap)

            val run = SportActivity(
                activityType = ActivityType.RUNNING,
                img = string,
                timestamp = dateFormat.format(calendar.time),
                avgSpeed = avgSpeed,
                distanceInMeters = distanceInMeters,
                timeInMillis = timeInMillis,
                caloriesBurned = caloriesBurned
            )
            viewModel.stopActivity(run, image)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootViewMain),
                "Run saved successfully",
                Snackbar.LENGTH_LONG
            ).show()
            stopRun()
        }
        else {
            showSnackbar("Не удалось определить ваше местоположение", requireActivity().findViewById(R.id.rootViewMain))

        }
//        binding.mapView.setDrawingCacheEnabled(true)
//
//        // this is the important code :)
//        // Without it the view will have a dimension of 0,0 and the bitmap will be null
//
//        // this is the important code :)
//        // Without it the view will have a dimension of 0,0 and the bitmap will be null
//        binding.mapView.measure(
//            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
//        )
//        binding.mapView.layout(0, 0, binding.mapView.getMeasuredWidth(), binding.mapView.getMeasuredHeight())
//        //Forces the drawing cache to be built if the drawing cache is invalid.
//        //Forces the drawing cache to be built if the drawing cache is invalid.
//        binding.mapView.buildDrawingCache(true)
//        val b: Bitmap = Bitmap.createBitmap(binding.mapView.getDrawingCache())
//        binding.mapView.setDrawingCacheEnabled(false) // clear drawing cache



    }

    private fun addAllPolylines() {
        for(polyline in pathPoints) {
            var points = mutableListOf<Point>()
            for (latlng in polyline){
                points.add(Point(latlng.latitude,latlng.longitude))
            }
            val polyline = world.mappable.mapkit.geometry.Polyline(points)
            val polylineObject = binding.mapView.map.mapObjects.addPolyline(polyline)
            polylineObject.apply {
                strokeWidth = 5f
                setStrokeColor(ContextCompat.getColor(this@TrackingFragment.requireContext(), R.color.colorPrimary))
                outlineWidth = 1f
                outlineColor = ContextCompat.getColor(this@TrackingFragment.requireContext(), com.google.android.material.R.color.m3_ref_palette_black)
            }

        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            var prelastPoint = Point(preLastLatLng.latitude,preLastLatLng.longitude)
            var lastPoint = Point(lastLatLng.latitude,lastLatLng.longitude)
            var points = listOf<Point>(prelastPoint,lastPoint)
            val polyline = world.mappable.mapkit.geometry.Polyline(points)
            val polylineObject = binding.mapView.map.mapObjects.addPolyline(polyline)
            polylineObject.apply {
                strokeWidth = 5f
                setStrokeColor(ContextCompat.getColor(this@TrackingFragment.requireContext(), com.google.android.material.R.color.m3_ref_palette_black))
                outlineWidth = 1f
                outlineColor = ContextCompat.getColor(this@TrackingFragment.requireContext(), com.google.android.material.R.color.m3_ref_palette_black)
            }
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    private fun captureScreen() {
        binding.mapView.setDrawingCacheEnabled(true);
        var bmp: Bitmap = Bitmap.createBitmap(binding.mapView.getDrawingCache())
    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }


    fun takeScreenshotOfView(view: View): Bitmap? {
        try {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
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

    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onLowMemory() {
        super.onLowMemory()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    fun roundFloat(value: Float, decimalPlaces: Int): Float {
        val decimalFormat = DecimalFormat("#.${"#".repeat(decimalPlaces)}")
        val format= decimalFormat.format(value).replace(",",".")
        return format.toFloat()
    }

}