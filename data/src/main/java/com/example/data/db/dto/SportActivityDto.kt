package com.example.data.db.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_entity")
data class SportActivityDto(
    @PrimaryKey(autoGenerate = true)
    var id : Int =0,
    @ColumnInfo(name = "activityType")
    var activityType: String,
    @ColumnInfo(name = "img")
    var img: String,
    @ColumnInfo(name = "timestamp")
    var timestamp: String,
    @ColumnInfo(name = "avgSpeedInKMH")
    var avgSpeedInKMH: Float,
    @ColumnInfo(name = "distanceInMeters")
    var distanceInMeters: Long,
    @ColumnInfo(name = "timeInMillis")
    var timeInMillis: Long,
    @ColumnInfo(name = "caloriesBurned")
    var caloriesBurned: Long
)