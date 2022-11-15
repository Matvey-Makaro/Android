package com.example.tabatatimer.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
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
import com.example.tabatatimer.TimerActivity
import com.example.tabatatimer.TrainingSettingsActivity
import com.example.tabatatimer.database.models.Training
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
            val setIntent = Intent (context, TimerActivity::class.java)

            val id : Int = trainingWithIntervals.id

            setIntent.putExtra("id", id)
            context.startActivity(setIntent)

            context.finish()
        }

        holder.settingsTrainingBtn.setOnClickListener{
            val settingsIntent = Intent(context, TrainingSettingsActivity::class.java)
            settingsIntent.putExtra("id", trainingWithIntervals.id)
            context.startActivity(settingsIntent)
            context.finish()
        }

        holder.deleteTrainingBtn.setOnClickListener{
            trainingWithIntervals = trainingsViewModel.trainingWithIntervals.
            find { it.id == trainingWithIntervals.id }!!

            var pos: Int = 0
            for(i in trainingsViewModel.trainingWithIntervals)
            {
                if(i.id == trainingWithIntervals.id)
                    break
                pos++
            }

            var dao = trainingsViewModel.dao

            trainingsViewModel.trainingWithIntervals.remove(trainingWithIntervals)
            dao?.deleteTraining(Training(
                trainingWithIntervals.id,
                trainingWithIntervals.repeats,
                trainingWithIntervals.color,
                trainingWithIntervals.name,
                trainingWithIntervals.soundEffect
            ))

            for(i in dao?.getTrainingsIntervals(trainingWithIntervals.id)!!)
                dao?.deleteInterval(i)

            notifyItemRemoved(pos)
        }

        holder.intervalsInfo.movementMethod = ScrollingMovementMethod()
        holder.intervalsInfo.text = trainingWithIntervals.getPrettyIntervals(context, font!!)
        holder.trainingsName.text = trainingWithIntervals.name
        holder.overralTraining.text = context.getString(R.string.total, trainingWithIntervals.getOverallTime(), trainingWithIntervals.getIntervalsNum())

        val hex : String = "#" + trainingWithIntervals.color.toString(16)
        holder.background.setBackgroundColor(Color.parseColor(hex))
        holder.startTrainingBtn.setBackgroundColor(Color.parseColor(hex))
        holder.settingsTrainingBtn.setBackgroundColor(Color.parseColor(hex))

        if (font == "1")
        {
            holder.trainingsName.setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.trainingsName.textSize * (0.5).toFloat())
            holder.intervalsInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.intervalsInfo.textSize * (0.5).toFloat())
            holder.overralTraining.setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.overralTraining.textSize * (0.5).toFloat())
        }
        else if (font == "3")
        {
            holder.trainingsName.setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.trainingsName.textSize * (1.5).toFloat())
            holder.intervalsInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.intervalsInfo.textSize * (1.5).toFloat())
            holder.overralTraining.setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.overralTraining.textSize * (1.5).toFloat())
        }
    }

    override fun getItemCount(): Int
    {
        return trainingsViewModel.trainingWithIntervals.size
    }
}