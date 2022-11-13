package com.example.tabatatimer.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.tabatatimer.database.AppDatabase
import com.example.tabatatimer.database.models.Interval
import com.example.tabatatimer.database.models.TrainingActionType
import com.example.tabatatimer.database.models.TrainingDao
import com.example.tabatatimer.database.models.TrainingWithIntervals

class TrainingSettingsViewModel : ViewModel()
{
    var dao: TrainingDao? = null
    var curTrainingWithIntervals: TrainingWithIntervals? = null

    fun initVars(application: Application, id: Int)
    {
        dao = AppDatabase.getDatabase(application).trainingDao()
        curTrainingWithIntervals = dao?.getJoined()?.find { it.id == id }
    }

    fun updateOrder()
    {
        var pos: Int = 1
        for(i in curTrainingWithIntervals?.intervals!!)
        {
            i.order = pos++
            dao?.updateInterval(i)
        }
    }

    fun addInterval(pos: Int)
    {
        val interval = Interval(0, curTrainingWithIntervals!!.id, TrainingActionType.WORK, 10, "", pos)
        dao?.addInterval(interval)
        interval.interval_id = dao?.getMaxIntervalId()!!
        curTrainingWithIntervals?.intervals?.add(pos - 1, interval)
        updateOrder()
    }

    fun deleteInterval(pos: Int)
    {
        val interval: Interval? = curTrainingWithIntervals?.intervals?.find { it.order == pos}
        dao?.deleteInterval(interval!!)
        curTrainingWithIntervals?.intervals?.remove(interval)
        updateOrder()
    }
}