package com.example.tabatatimer.database.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TrainingActionType
{
    PREPARATION,
    WORK,
    REST,
    REST_BETWEEN_SETS,
    CALM_DOWN
}

@Entity(tableName = "interval")
data class Interval(
    @PrimaryKey(autoGenerate = true) var interval_id : Int,
    @NonNull var trainingId: Int,
    @NonNull @ColumnInfo(name = "action_type") var actionType: TrainingActionType,
    @NonNull @ColumnInfo(name = "interval_time") var intervalTime: Int,
    @NonNull @ColumnInfo(name = "description") var description: String,
    @NonNull @ColumnInfo(name = "order") var order: Int
)

@Entity(tableName = "training")
data class Training(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "training_id") var training_id : Int,
    @NonNull @ColumnInfo(name="repeats") val repeats : Int,
    @NonNull @ColumnInfo(name="color") val color : Long,
    @NonNull @ColumnInfo(name="name") val Name: String,
    @NonNull @ColumnInfo(name="sound_effect") val soundEffect: Boolean
)

class TrainingWithIntervals(
    var id: Int,
    var color: Long,
    var repeats: Int,
    var name: String,
    var soundEffect: Boolean,
    var intervals: MutableList<Interval>
){
    fun getOverallTime() : String
    {
        var sum: Int = 0
        for(i in intervals)
            sum += i.intervalTime
        return String.format("%02d:%02d", sum / 60, sum % 60)
    }

    fun getIntervalsNum() : Int
    {
        return intervals.size
    }
}