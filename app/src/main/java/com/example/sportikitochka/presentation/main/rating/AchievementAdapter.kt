package com.example.sportikitochka.presentation.main.rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.databinding.AchievementItemBinding
import com.example.sportikitochka.databinding.RatingItemBinding
import com.example.sportikitochka.domain.models.Achievement
import com.example.sportikitochka.domain.models.User
import com.example.sportikitochka.other.TrackingUtility
import java.text.DecimalFormat

class AchievementAdapter(
    private val actionListener: ActionListener
) : ListAdapter<Achievement, AchievementAdapter.AchievementViewHolder>(DiffCallBack) {

    class AchievementViewHolder(val binding: AchievementItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AchievementItemBinding.inflate(inflater, parent, false)

//        binding.root.setOnClickListener(this)
//        binding.favouriteBtnItem.setOnClickListener(this)
//        binding.taskCard.setOnClickListener(this)
//        binding.itemLayout.setOnClickListener(this)
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.achievementItemIv.setOnClickListener {
            actionListener.onClickItem(item)
        }

        with(holder.binding) {
            when(item.achievementId) {
                0 -> {achievementItemIv.setImageResource(R.drawable.ic_rating_icon_1)}
                1 -> {achievementItemIv.setImageResource(R.drawable.ic_rating_icon_2)}
                2 -> {achievementItemIv.setImageResource(R.drawable.ic_rating_item_3)}
            }
        }
    }


    interface ActionListener {
        fun onClickItem(item: Achievement)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Achievement>() {

            override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun roundFloat(value: Float, decimalPlaces: Int): Float {
        val decimalFormat = DecimalFormat("#.${"#".repeat(decimalPlaces)}")
        val format= decimalFormat.format(value).replace(",",".")
        return format.toFloat()
    }

}