package com.example.domain.models

import com.example.sportikitochka.R

enum class ActivityType(val image: Int, val activityName: String, val id: Int){
    RUNNING(R.drawable.ic_running, "Бег", 0),
    SWIMMING(R.drawable.ic_swim, "Плавание", 1),
    BYCICLE(R.drawable.ic_bycicle, "Велозаезд", 2);

    override fun toString(): String {
        return when(this) {
            RUNNING -> "RUNNING"
            SWIMMING -> "SWIMMING"
            BYCICLE -> "CYCLE"
        }
    }


}


fun String.mapToActivityType(): ActivityType {
    return when(this){
        ActivityType.RUNNING.activityName -> ActivityType.RUNNING
        ActivityType.SWIMMING.activityName -> ActivityType.SWIMMING
        ActivityType.BYCICLE.activityName -> ActivityType.BYCICLE
        else -> ActivityType.RUNNING
    }
}
