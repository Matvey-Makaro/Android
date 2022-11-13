package com.example.tabatatimer.adapters

import android.content.Context
import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.MainActivity
import com.example.tabatatimer.R
import com.example.tabatatimer.viewmodels.TrainingsViewModel

class TrainingsAdapter(
    private var context: MainActivity,
    private var trainingsViewModel: TrainingsViewModel
) : RecyclerView.Adapter<TrainingsAdapter.TrainingViewHolder>()
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
        val itemView = LayoutInflater.from(parent.context)
            .inflate(com.example.tabatatimer.R.layout.training_item, parent, false)

        return TrainingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int)
    {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val font : String? = sharedPreferences.getString("font_preference", "-1")
        var trainingWithIntervals = trainingsViewModel.trainingWithIntervals[position]

        holder.startTrainingBtn.setOnClickListener {
            // TODO: Написать обработчик с переходом в другое activity
        }

        holder.settingsTrainingBtn.setOnClickListener{
            // TODO: Написать обработчик с переходом в другое activity
        }

        holder.deleteTrainingBtn.setOnClickListener{
            // TODO: Написать обработчик
        }

        holder.intervalsInfo.movementMethod = ScrollingMovementMethod()
        holder.intervalsInfo.text // TODO: Установить текст
        holder.trainingsName.text = trainingWithIntervals.name
        holder.overralTraining.text = context.getString(R.string.total, trainingWithIntervals.getOverallTime(), trainingWithIntervals.getIntervalsNum())

        val hex : String = "#" + trainingWithIntervals.color.toString(16)
        holder.background.setBackgroundColor(Color.parseColor(hex))
        holder.startTrainingBtn.setBackgroundColor(Color.parseColor(hex))
        holder.settingsTrainingBtn.setBackgroundColor(Color.parseColor(hex))

    }

    override fun getItemCount(): Int
    {
        return trainingsViewModel.trainingWithIntervals.size
    }
}