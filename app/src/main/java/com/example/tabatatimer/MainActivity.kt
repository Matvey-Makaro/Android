package com.example.tabatatimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.adapters.TrainingsAdapter
import com.example.tabatatimer.database.models.Training
import com.example.tabatatimer.database.models.TrainingWithIntervals
import com.example.tabatatimer.viewmodels.TrainingsViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: Получить настройки()

        val viewModel = ViewModelProvider(this).get(TrainingsViewModel::class.java)
        viewModel.initVars(application)

        val recyclerView : RecyclerView = findViewById(R.id.trainingsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrainingsAdapter(this, viewModel)

        val addBtn : Button = findViewById(R.id.addTrainingButton)
        addBtn.setOnClickListener{
            val basicTraining: Training = TrainingWithIntervals.getBasicTraining(this)
            viewModel.dao?.addTraining(basicTraining)
            val id: Int = viewModel.dao?.getMaxTrainingId()!!
            val basicIntervals = TrainingWithIntervals.getBasicIntervals(id)

            for(i in basicIntervals)
                viewModel.dao?.addInterval(i)

            viewModel.trainingWithIntervals.add(TrainingWithIntervals(id, basicTraining.color,
                basicTraining.repeats, basicTraining.Name, basicTraining.soundEffect, basicIntervals))
            recyclerView.adapter?.notifyItemInserted(viewModel.trainingWithIntervals.size - 1)

            val settingsIntent = Intent(this, TrainingSettingsActivity::class.java)
            settingsIntent.putExtra("id", id)
            startActivity(settingsIntent)

            finish()
        }

        // TODO: updateTheme()
    }
}