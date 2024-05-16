package com.example.sportikitochka.presentation.main.main

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sportikitochka.databinding.RunItemBinding
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.bitmapToString
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainAdapter(
) : ListAdapter<SportActivity, MainAdapter.MainViewHolder>(DiffCallBack), View.OnClickListener {

    class MainViewHolder(val binding: RunItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RunItemBinding.inflate(inflater, parent, false)

//        binding.root.setOnClickListener(this)
//        binding.favouriteBtnItem.setOnClickListener(this)
//        binding.taskCard.setOnClickListener(this)
//        binding.itemLayout.setOnClickListener(this)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = getItem(position)
//        holder.itemView.setOnClickListener {
//            actionListener.onClickItem(item)
//        }

        with(holder.binding) {
//            val calendar = Calendar.getInstance().apply {
//                timeInMillis = item.timestamp
//            }
//            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = item.timestamp

            val avgSpeed = "${item.avgSpeed}км/ч"
            tvSpeed.text = avgSpeed

            val distanceInKm = "${item.distanceInMeters / 1000f} км"
            tvDistance.text = distanceInKm

            tvTime.text = TrackingUtility.getFormattedStopWatchTime(item.timeInMillis)
            tvTime.text = TrackingUtility.getFormattedStopWatchTime(item.timeInMillis)

            val caloriesBurned = "${item.caloriesBurned}ккал"
            tvCalories.text = caloriesBurned


            val decodedString: ByteArray? = Base64.decode(item.img, Base64.DEFAULT)

            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size ?: 0)

// Установка Bitmap в ImageView
            imageView1.setImageBitmap(bitmap)
        }
    }

    override fun onClick(v: View?) {
//        var cat = v?.tag as Task
//        when (v.id) {
//            R.id.task_card ->{
//                actionListener.onClickItem(cat)
//            }
//
//            else -> actionListener.onClickItem(cat)
//        }
    }

    interface TaskActionListener {
        fun onClickItem(task : SportActivity)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<SportActivity>() {

            override fun areItemsTheSame(oldItem: SportActivity, newItem: SportActivity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: SportActivity, newItem: SportActivity): Boolean {
                return oldItem == newItem
            }
        }
    }

}