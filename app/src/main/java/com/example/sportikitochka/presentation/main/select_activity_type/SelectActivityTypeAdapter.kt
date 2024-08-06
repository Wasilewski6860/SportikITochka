package com.example.sportikitochka.presentation.main.select_activity_type

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportikitochka.databinding.ActivityTypeItemBinding
import com.example.domain.models.ActivityType

class SelectActivityTypeAdapter(
) : ListAdapter<com.example.domain.models.ActivityType, SelectActivityTypeAdapter.ActivityTypeViewHolder>(DiffCallBack), View.OnClickListener {

    class ActivityTypeViewHolder(val binding: ActivityTypeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityTypeItemBinding.inflate(inflater, parent, false)

//        binding.root.setOnClickListener(this)
//        binding.favouriteBtnItem.setOnClickListener(this)
//        binding.taskCard.setOnClickListener(this)
//        binding.itemLayout.setOnClickListener(this)
        return ActivityTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityTypeViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            Glide.with(holder.itemView).load(item.image).into(profileImage)
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

    fun getActivityItem(position: Int): com.example.domain.models.ActivityType {
        // Возвращение данных элемента по позиции
        return getItem(position)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<com.example.domain.models.ActivityType>() {

            override fun areItemsTheSame(oldItem: com.example.domain.models.ActivityType, newItem: com.example.domain.models.ActivityType): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: com.example.domain.models.ActivityType, newItem: com.example.domain.models.ActivityType): Boolean {
                return oldItem == newItem
            }
        }
    }

}