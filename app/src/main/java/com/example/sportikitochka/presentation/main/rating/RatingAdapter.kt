package com.example.sportikitochka.presentation.main.rating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sportikitochka.databinding.RatingItemViewBinding
import com.example.domain.models.User
import java.text.DecimalFormat

class RatingAdapter(
    private val isAdmin: Boolean,
    private val premiumActionListener: UserPremiumActionListener,
    private val blockActionListener: UserBlockActionListener
) : ListAdapter<com.example.domain.models.User, RatingAdapter.UserViewHolder>(DiffCallBack) {

    class UserViewHolder(val binding: RatingItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RatingItemViewBinding.inflate(inflater, parent, false)

//        binding.root.setOnClickListener(this)
//        binding.favouriteBtnItem.setOnClickListener(this)
//        binding.taskCard.setOnClickListener(this)
//        binding.itemLayout.setOnClickListener(this)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.ratingView.setData(item,isAdmin, premiumActionListener, blockActionListener)
//        holder.binding.increaseDecreaseButtonRating.setOnClickListener {
//            premiumActionListener.onClickItem(item)
//        }
//
//        holder.binding.deleteButtonRating.setOnClickListener {
//            blockActionListener.onClickItem(item)
//        }
//
//        with(holder.binding) {
//            usernameRatingItemTv.text = item.name
//            totalCountValueRatingItemTv.text="Общее кол-во: "+item.totalCount.toString()
//            ratingRatingItemTv.text = "#"+item.place
//            distanseTv.text = "${item.totalDistanse / 1000f}"
//            avDistanceValueTv.text = "${roundFloat((item.totalDistanse / 1000f)/item.totalCount, 3)} км"
//
//            totalKcalBurnedTv.text = "${item.totalCalories}"
//            avKcalValueTv.text = "${(item.totalCalories)/item.totalCount} ккал"
//
//            totalTimeValueTv.text = TrackingUtility.getFormattedStopWatchTime(item.totalTime)
//            avTimeValueTv.text = TrackingUtility.getFormattedStopWatchTime(item.totalTime/item.totalCount)
//
//            when(item.role) {
//                is UserType.Normal -> {
//                    premiumIconRatingItemIv.visibility = View.GONE
//                    increaseDecreaseButtonRating.visibility = View.GONE
//                    deleteButtonRating.visibility = View.GONE
//                    increaseDecreaseButtonRating.setBackgroundResource(R.drawable.ic_update_user)
//                }
//                is UserType.Premium -> {
//                    premiumIconRatingItemIv.visibility = View.VISIBLE
//                    increaseDecreaseButtonRating.visibility = View.GONE
//                    deleteButtonRating.visibility = View.GONE
//                    increaseDecreaseButtonRating.setBackgroundResource(R.drawable.ic_decrease_user)
//                }
//                else -> {
//                    premiumIconRatingItemIv.visibility = View.GONE
//                    increaseDecreaseButtonRating.visibility = View.GONE
//                    deleteButtonRating.visibility = View.GONE
//                }
//            }
//
//            if (isAdmin) {
//                increaseDecreaseButtonRating.visibility = View.VISIBLE
//                deleteButtonRating.visibility = View.VISIBLE
//            }
//            else {
//                increaseDecreaseButtonRating.visibility = View.GONE
//                deleteButtonRating.visibility = View.GONE
//            }
//
//            if (item.isBlocked) {
//                deleteButtonRating.setBackgroundResource(R.drawable.ic_unblock)
//                blockedTv.visibility = View.VISIBLE
//                blockedTv.text = "Заблокирован"
//                borderLeft.visibility = View.GONE
//                borderRight.visibility = View.GONE
//                distanceContainer.visibility = View.GONE
//                timeContainer.visibility = View.GONE
//                caloriesContainer.visibility = View.GONE
//                increaseDecreaseButtonRating.visibility = View.GONE
//            }
//            else {
//                deleteButtonRating.setBackgroundResource(R.drawable.ic_block)
//                blockedTv.visibility = View.GONE
//                borderLeft.visibility = View.VISIBLE
//                borderRight.visibility = View.VISIBLE
//                distanceContainer.visibility = View.VISIBLE
//                timeContainer.visibility = View.VISIBLE
//                caloriesContainer.visibility = View.VISIBLE
//            }
//
//        }
    }


    interface UserPremiumActionListener {
        fun onClickItem(user: com.example.domain.models.User)
    }

    interface UserBlockActionListener {
        fun onClickItem(user: com.example.domain.models.User)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<com.example.domain.models.User>() {

            override fun areItemsTheSame(oldItem: com.example.domain.models.User, newItem: com.example.domain.models.User): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: com.example.domain.models.User, newItem: com.example.domain.models.User): Boolean {
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