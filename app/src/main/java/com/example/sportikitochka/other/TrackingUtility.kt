package com.example.sportikitochka.other

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

object TrackingUtility {

    const val USER_TYPE_KEY = "USER_TYPE_KEY"

    fun bitmapToString(bitmap: Bitmap): String = runBlocking {
        return@runBlocking withContext(Dispatchers.Default) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val encodedString: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            return@withContext encodedString
        }
    }
    fun hasLocationPermissions(context: Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun calculatePolylineLength(polyline: Polyline): Float {
        var distance = 0f
        for(i in 0..polyline.size - 2) {
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if(!includeMillis) {
            return "${if(hours < 10) "0" else ""}$hours:" +
                    "${if(minutes < 10) "0" else ""}$minutes:" +
                    "${if(seconds < 10) "0" else ""}$seconds"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds:" +
                "${if(milliseconds < 10) "0" else ""}$milliseconds"
    }

    fun showSnackbar(text : String, view: View){
        Snackbar.make(
            view,
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun roundFloat(value: Float, decimalPlaces: Int): Float {
        val decimalFormat = DecimalFormat("#.${"#".repeat(decimalPlaces)}")
        val format= decimalFormat.format(value).replace(",",".")
        return format.toFloat()
    }

    fun uriToString(context: Context, imageUri: Uri?, view: View): String? {
        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null
        var string: String? =null

        try {
            inputStream = imageUri?.let { context.contentResolver.openInputStream(it) }
            bitmap = BitmapFactory.decodeStream(inputStream)
            string = bitmapToString(bitmap)
        } catch (e: FileNotFoundException) {
            showSnackbar("Файл не найден", view)
        }
        catch (e: Exception) {
            showSnackbar("Что-то пошло не так", view)
        }finally {
            inputStream?.close()
        }

        return string
    }

    fun bitmapToFile(name:String, context: Context, bitmap: Bitmap): File {
        val file = File(context.cacheDir, name)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }
}