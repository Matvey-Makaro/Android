package com.example.tabatatimer.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class TrainingsAdapter : RecyclerView.Adapter<TrainingsAdapter.TrainingViewHolder>()
{
    class TrainingViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    {
        val startTrainingBtn : Button = view.findViewById(com.example.tabatatimer.R.id.StartTrainingButton)
        val settingsTrainingBtn : Button = view.findViewById(com.example.tabatatimer.R.id.SettingsTrainingButton)
        val deleteTrainingBtn : Button = view.findViewById(com.example.tabatatimer.R.id.DeleteTrainingButton)

        val intervalsInfo : TextView = view.findViewById(com.example.tabatatimer.R.id.IntervalsInfoTextView)
        val trainingsName : TextView = view.findViewById(com.example.tabatatimer.R.id.TrainingNameTextView)
        val overralTraining : TextView = view.findViewById(com.example.tabatatimer.R.id.OverallTrainingTextEdit)

        val background : ConstraintLayout = view.findViewById(com.example.tabatatimer.R.id.trainingItemConstrLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder
    {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int)
    {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int
    {
        TODO("Not yet implemented")
    }
}