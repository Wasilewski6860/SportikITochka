package com.example.sportikitochka.data.db.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sportikitochka.other.ActivityType
import com.google.gson.annotations.SerializedName

@Entity(tableName = "activity_entity")
data class SportActivityDto(
    @PrimaryKey(autoGenerate = true)
    var id : Int =0,
    @ColumnInfo(name = "activityType")
    var activityType: String,
    @ColumnInfo(name = "img")
    var img: String,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long,
    @ColumnInfo(name = "avgSpeedInKMH")
    var avgSpeedInKMH: Float,
    @ColumnInfo(name = "distanceInMeters")
    var distanceInMeters: Long,
    @ColumnInfo(name = "timeInMillis")
    var timeInMillis: Long,
    @ColumnInfo(name = "caloriesBurned")
    var caloriesBurned: Long
)