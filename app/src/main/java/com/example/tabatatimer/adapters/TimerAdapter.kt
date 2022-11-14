package com.example.tabatatimer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.TimerActivity
import com.example.tabatatimer.database.AppDatabase

class TimerAdapter(
    private var context: TimerActivity,
    private var trainingId: Int
) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>()
{
    private var dao = AppDatabase.getDatabase(context).trainingDao()
    private var intervals = dao.getTrainingsIntervals(trainingId)

    class TimerViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val interval_btn: Button = view.findViewById(com.example.tabatatimer.R.id.intervalButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(com.example.tabatatimer.R.layout.timer_item, parent, false)
        return TimerAdapter.TimerViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        // TODO: Получить настройки


    }

    override fun getItemCount(): Int {
        return intervals.size
    }
}