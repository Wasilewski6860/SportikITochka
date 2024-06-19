package com.example.sportikitochka.presentation.main.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.CardItemLayoutBinding
import com.example.sportikitochka.databinding.RatingItemBinding
import com.example.sportikitochka.domain.models.CreditCard
import com.example.sportikitochka.domain.models.User
import com.example.sportikitochka.other.TrackingUtility
import java.text.DecimalFormat

class CardsAdapter(
    private val cardActionListener: CardActionListener,
    private val buttonActionListener: CardActionListener,
    private val newCardClickActionListener: CardActionListener,
) : ListAdapter<CreditCard, CardsAdapter.CardViewHolder>(DiffCallBack) {

    class CardViewHolder(val binding: CardItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardItemLayoutBinding.inflate(inflater, parent, false)

//        binding.root.setOnClickListener(this)
//        binding.favouriteBtnItem.setOnClickListener(this)
//        binding.taskCard.setOnClickListener(this)
//        binding.itemLayout.setOnClickListener(this)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = getItem(position)

        if (item.cardNumber!=null) {
            with(holder.binding) {
                mainLayout.visibility = View.VISIBLE
                newCardLayout.visibility = View.GONE
                cardNumberTv.text = "*"+item.cardNumber?.substring(12)
                cardIv.setImageResource(R.drawable.ic_card)
//                card1.setIsFlippable(false)
//                card1.setIsEditable(false)
//                card1.cardName = item.cardName
//                card1.cardNumber = item.cardNumber
//                val expirity = item.month.toString()+"/"+item.year.toString()
//                card1.expiryDate = expirity
//                editButton.setOnClickListener {
//                    buttonActionListener.onClickItem(item)
//                }
                mainLayout.setOnClickListener {
                    cardActionListener.onClickItem(item)
                }
            }
        }
        else {
            with(holder.binding) {
                mainLayout.visibility = View.GONE
                newCardLayout.visibility = View.VISIBLE
                newCardLayout.setOnClickListener {
                    newCardClickActionListener.onClickItem(item)
                }
                cardIv.setImageResource(R.drawable.ic_add_inbox)
            }
        }

    }

    interface CardActionListener {
        fun onClickItem(card: CreditCard)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<CreditCard>() {

            override fun areItemsTheSame(oldItem: CreditCard, newItem: CreditCard): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: CreditCard, newItem: CreditCard): Boolean {
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