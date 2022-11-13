package com.example.tabatatimer.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.tabatatimer.database.AppDatabase
import com.example.tabatatimer.database.models.TrainingDao
import com.example.tabatatimer.database.models.TrainingWithIntervals

class TrainingsViewModel() : ViewModel()
{
    var dao: TrainingDao? = null
    var trainingWithIntervals: MutableList<TrainingWithIntervals> = mutableListOf()

    fun initVars(application: Application)
    {
        dao = AppDatabase.getDatabase(application).trainingDao()
        trainingWithIntervals = dao!!.getJoined()
    }



}