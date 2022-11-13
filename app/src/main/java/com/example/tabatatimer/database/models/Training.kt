package com.example.tabatatimer.database.models

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tabatatimer.R
import kotlin.math.min

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

    fun getPrettyIntervals(context: Context, font:String): String
    {
        var result: String = ""

        val mx : Int = when (font) {
            "1" -> 10
            "3" -> 3
            else -> 4
        }

        for (i in (0..min(mx, intervals.size - 1))) {
            result += String.format("%02d. %s", i + 1, context.resources.
            getStringArray(R.array.interval_types)[intervals[i].actionType.ordinal + 1])
            result += "\n"
        }

        if (intervals.size > 5)
            result += "..."

        return result
    }

    companion object{
        fun getBasicTraining(context: Context) : Training
        {
            val rndNum: Int = (0..4).random()
            val color: Long = when(rndNum){
                0 -> (4278190080).toLong()
                1 -> (16092482).toLong()
                2 -> (16073282).toLong()
                3 -> (15483637).toLong()
                else -> (4388171).toLong()
            }

            return Training(0, 1, color, "Select name", true)
        }

        fun getBasicIntervals(id: Int): MutableList<Interval>
        {
            return mutableListOf(
                Interval(0, id, TrainingActionType.PREPARATION, 10, "", 1),
                Interval(0, id, TrainingActionType.WORK, 10, "", 2),
                Interval(0, id, TrainingActionType.REST, 10, "", 3),
                Interval(0, id, TrainingActionType.REST_BETWEEN_SETS, 10, "", 4),
                Interval(0, id, TrainingActionType.CALM_DOWN, 0,"", 5)
            )
        }
    }
}