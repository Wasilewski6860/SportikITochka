package com.example.sportikitochka.presentation.main.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.sportikitochka.R
import com.example.data.network.EndPoints.BASE_URL
import com.example.sportikitochka.databinding.RunItemBinding
import com.example.domain.models.SportActivity
import com.example.sportikitochka.other.TrackingUtility

class MainAdapter(
    val context: Context
) : ListAdapter<com.example.domain.models.SportActivity, MainAdapter.MainViewHolder>(DiffCallBack), View.OnClickListener {

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

//
//            val decodedString: ByteArray? = Base64.decode(item.img, Base64.DEFAULT)
//
//            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size ?: 0)
//
//// Установка Bitmap в ImageView
//            imageView1.setImageBitmap(bitmap)

            val radius = context.resources.getDimensionPixelSize(R.dimen.corner_radius)
            Glide.with(context)
                .load(BASE_URL + item.img)
                .transform(RoundedCorners(radius))
                // Alternative: .transforms(CenterCrop(), RoundedCorners(radius))
                .transition(DrawableTransitionOptions.withCrossFade()).into(imageView1)
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
        fun onClickItem(task : com.example.domain.models.SportActivity)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<com.example.domain.models.SportActivity>() {

            override fun areItemsTheSame(oldItem: com.example.domain.models.SportActivity, newItem: com.example.domain.models.SportActivity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: com.example.domain.models.SportActivity, newItem: com.example.domain.models.SportActivity): Boolean {
                return oldItem == newItem
            }
        }
    }

}