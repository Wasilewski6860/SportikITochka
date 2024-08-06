package com.example.sportikitochka.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.sportikitochka.R
import com.example.data.models.response.auth.UserType
import com.example.data.network.EndPoints.BASE_URL
import com.example.domain.models.Achievement
import com.example.domain.models.User
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.roundFloat
import com.example.sportikitochka.presentation.main.rating.AchievementAdapter
import com.example.sportikitochka.presentation.main.rating.RatingAdapter

class RatingItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private var profileImageRatingItem: com.makeramen.roundedimageview.RoundedImageView //profile_image_rating_item
    private var nameView: TextView // username_rating_item_tv
    private var totalCountView: TextView // total_count_value_rating_item_tv
    private var premiumIcon: ImageView // premium_icon_rating_item_iv
    private var ratingTv: TextView // rating_rating_item_tv
    private var increaseDecreaseButton: ImageButton // increase_decrease_button_rating
    private var deleteButton: ImageButton // delete_button_rating
    private var recyclerAchievements: RecyclerView // achievement_recycler

    private var trackingInfoCardView: CardView // tracking_info_rating_fragment_cv
    private var blockedTv: TextView // blocked_tv

    private var distanceContainer: ConstraintLayout // distanceContainer
    private var distanseTv: TextView // distanse_tv
    private var avDistanseTv: TextView // av_distance_value_tv

    private var caloriesContainer: ConstraintLayout // caloriesContainer
    private var totalKcalBurnedTv: TextView // total_kcal_burned_tv
    private var avKcalBurnedTv: TextView // av_kcal_value_tv

    private var timeContainer: ConstraintLayout // timeContainer
    private var totalTimeTv: TextView // total_time_value_tv
    private var avTimeTv: TextView // av_time_value_tv

    private var borderLeft: View // borderLeft
    private var borderRight: View // borderRight

    private val adapter: AchievementAdapter
    init {
        View.inflate(context, R.layout.rating_item, this)
        profileImageRatingItem = findViewById(R.id.profile_image_rating_item)
        nameView = findViewById(R.id.username_rating_item_tv)
        totalCountView = findViewById(R.id.total_count_value_rating_item_tv)
        premiumIcon = findViewById(R.id.premium_icon_rating_item_iv)
        ratingTv = findViewById(R.id.rating_rating_item_tv)
        increaseDecreaseButton = findViewById(R.id.increase_decrease_button_rating)
        deleteButton = findViewById(R.id.delete_button_rating)
        recyclerAchievements = findViewById(R.id.achievement_recycler)

        trackingInfoCardView = findViewById(R.id.tracking_info_rating_fragment_cv)
        blockedTv = findViewById(R.id.blocked_tv)

        distanceContainer = findViewById(R.id.distanceContainer)
        distanseTv = findViewById(R.id.distanse_tv)
        avDistanseTv = findViewById(R.id.av_distance_value_tv)

        caloriesContainer = findViewById(R.id.caloriesContainer)
        totalKcalBurnedTv = findViewById(R.id.total_kcal_burned_tv)
        avKcalBurnedTv = findViewById(R.id.av_kcal_value_tv)

        timeContainer = findViewById(R.id.timeContainer)
        totalTimeTv = findViewById(R.id.total_time_value_tv)
        avTimeTv = findViewById(R.id.av_time_value_tv)

        borderLeft = findViewById(R.id.borderLeft)
        borderRight = findViewById(R.id.borderRight)



        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingItemView)
        val imageRes = typedArray.getResourceId(R.styleable.RatingItemView_image_res, 0)
        val name = typedArray.getString(R.styleable.RatingItemView_name)
        val totalCount = typedArray.getInteger(R.styleable.RatingItemView_total_count, 0)
        val isPremium = typedArray.getBoolean(R.styleable.RatingItemView_is_premium, false)
        val isBlocked = typedArray.getBoolean(R.styleable.RatingItemView_is_blocked, false)
        val isButtonsVisible = typedArray.getBoolean(R.styleable.RatingItemView_is_buttons_visible, false)
        val rating = typedArray.getInteger(R.styleable.RatingItemView_rating, 0)
        val distance = typedArray.getFloat(R.styleable.RatingItemView_distanse, 0F)
        val averageDistance = typedArray.getFloat(R.styleable.RatingItemView_average_distanse, 0F)
        val calories = typedArray.getInteger(R.styleable.RatingItemView_calories, 0)
        val averageCalories = typedArray.getFloat(R.styleable.RatingItemView_average_calories, 0F)
        val time = typedArray.getInteger(R.styleable.RatingItemView_time, 0)
        val averageTime = typedArray.getInteger(R.styleable.RatingItemView_average_time, 0)
        typedArray.recycle()

        imageRes.takeIf { it != 0 }?.let { profileImageRatingItem.setImageResource(it) }

        nameView.text = name
        totalCountView.text="Общее кол-во: "+totalCount.toString()
        ratingTv.text = "#"+rating
        distanseTv.text = "${distance / 1000f}"
        avDistanseTv.text = "${roundFloat((distance / 1000f)/totalCount, 3)} км"

        totalKcalBurnedTv.text = "${calories}"
        avKcalBurnedTv.text = "${averageCalories} ккал"

        totalTimeTv.text = TrackingUtility.getFormattedStopWatchTime(time.toLong())
        avTimeTv.text = TrackingUtility.getFormattedStopWatchTime(averageTime.toLong())

        val role: com.example.data.models.response.auth.UserType = if(isPremium) com.example.data.models.response.auth.UserType.Premium else if (isButtonsVisible) com.example.data.models.response.auth.UserType.Admin else com.example.data.models.response.auth.UserType.Normal
        when(role) {
            is com.example.data.models.response.auth.UserType.Normal -> {
                premiumIcon.visibility = View.GONE
                increaseDecreaseButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
                increaseDecreaseButton.setBackgroundResource(R.drawable.ic_update_user)
            }
            is com.example.data.models.response.auth.UserType.Premium -> {
                premiumIcon.visibility = View.VISIBLE
                increaseDecreaseButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
                increaseDecreaseButton.setBackgroundResource(R.drawable.ic_decrease_user)
            }
            else -> {
                premiumIcon.visibility = View.GONE
                increaseDecreaseButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
            }
        }

        if (isButtonsVisible) {
            increaseDecreaseButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
        }
        else {
            increaseDecreaseButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
        }

        if (isBlocked) {
            deleteButton.setBackgroundResource(R.drawable.ic_unblock)
            blockedTv.visibility = View.VISIBLE
            blockedTv.text = "Заблокирован"
            borderLeft.visibility = View.GONE
            borderRight.visibility = View.GONE
            distanceContainer.visibility = View.GONE
            timeContainer.visibility = View.GONE
            caloriesContainer.visibility = View.GONE
            increaseDecreaseButton.visibility = View.GONE
        }
        else {
            deleteButton.setBackgroundResource(R.drawable.ic_block)
            blockedTv.visibility = View.GONE
            borderLeft.visibility = View.VISIBLE
            borderRight.visibility = View.VISIBLE
            distanceContainer.visibility = View.VISIBLE
            timeContainer.visibility = View.VISIBLE
            caloriesContainer.visibility = View.VISIBLE
        }


        adapter = AchievementAdapter(
            object : AchievementAdapter.ActionListener {
                override fun onClickItem(item: com.example.domain.models.Achievement) {
                    Toast.makeText(context, item.achievementName+": "+item.achievementDistance.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        )
        recyclerAchievements.adapter = adapter
        recyclerAchievements.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        // Получаем размеры содержимого
//        val contentWidth = MeasureSpec.getSize(widthMeasureSpec)
//        val contentHeight = MeasureSpec.getSize(heightMeasureSpec)
//
//        // Устанавливаем высоту вьюшки в зависимости от ее содержимого
//        val cardHeight = contentHeight + paddingTop + paddingBottom
//
//        // Устанавливаем размеры вьюшки
//        setMeasuredDimension(contentWidth, cardHeight)
//    }

    fun setData(item: com.example.domain.models.User, isAdmin: Boolean, premiumActionListener: RatingAdapter.UserPremiumActionListener, blockActionListener: RatingAdapter.UserBlockActionListener
                ) {
//        profileImageRatingItem.setImageResource(R.drawable.profile_image_2)

        Glide.with(context)
            .load(BASE_URL+item.image)
            .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
            .circleCrop()
            .into(profileImageRatingItem)


        nameView.text = item.name
        val tC = if(item.totalCount != 0) item.totalCount else 1
        totalCountView.text="Общее кол-во: "+item.totalCount.toString()
        ratingTv.text = "#"+item.place
        distanseTv.text = "${item.totalDistanse / 1000f}"
        avDistanseTv.text = "${roundFloat((item.totalDistanse / 1000f)/tC, 3)} км"

        totalKcalBurnedTv.text = "${item.totalCalories}"
        avKcalBurnedTv.text = "${(item.totalCalories)/tC} ккал"

        totalTimeTv.text = TrackingUtility.getFormattedStopWatchTime(item.totalTime)
        avTimeTv.text = TrackingUtility.getFormattedStopWatchTime(item.totalTime/tC)


        increaseDecreaseButton.setOnClickListener {
            premiumActionListener.onClickItem(item)
        }
        deleteButton.setOnClickListener {
            blockActionListener.onClickItem(item)
        }
        when(item.role) {
            is com.example.data.models.response.auth.UserType.Normal -> {
                premiumIcon.visibility = View.GONE
                increaseDecreaseButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
                increaseDecreaseButton.setBackgroundResource(R.drawable.ic_update_user)
            }
            is com.example.data.models.response.auth.UserType.Premium -> {
                premiumIcon.visibility = View.VISIBLE
                increaseDecreaseButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
                increaseDecreaseButton.setBackgroundResource(R.drawable.ic_decrease_user)
            }
            else -> {
                premiumIcon.visibility = View.GONE
                increaseDecreaseButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
            }
        }

        if (isAdmin) {
            increaseDecreaseButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
        }
        else {
            increaseDecreaseButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
        }

        if (item.isBlocked) {
            deleteButton.setBackgroundResource(R.drawable.ic_unblock)
            blockedTv.visibility = View.VISIBLE
            blockedTv.text = "Заблокирован"
            borderLeft.visibility = View.GONE
            borderRight.visibility = View.GONE
            distanceContainer.visibility = View.GONE
            timeContainer.visibility = View.GONE
            caloriesContainer.visibility = View.GONE
            increaseDecreaseButton.visibility = View.GONE
        }
        else {
            deleteButton.setBackgroundResource(R.drawable.ic_block)
            blockedTv.visibility = View.GONE
            borderLeft.visibility = View.VISIBLE
            borderRight.visibility = View.VISIBLE
            distanceContainer.visibility = View.VISIBLE
            timeContainer.visibility = View.VISIBLE
            caloriesContainer.visibility = View.VISIBLE
        }

        adapter.submitList(item.achievements)
    }
}
