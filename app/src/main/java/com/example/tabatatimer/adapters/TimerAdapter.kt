package com.example.tabatatimer.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.R
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
        return TimerViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        // TODO: Получить настройки

        holder.interval_btn.text = String.format(
            "%02d. %s",
            position + 1,
            context.resources.getStringArray(R.array.interval_types)[intervals[position].actionType.ordinal + 1]
        )

        holder.interval_btn.setOnClickListener{context.pressBtn(position)}

        // TODO:
//        if (font == "1") {
//            holder.btn.setTextSize(TypedValue.COMPLEX_UNIT_PX,  (52 * 0.5).toFloat())
//        }
//
//
//        if (font == "3") {
//            holder.btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (52 * 1.4).toFloat())
//        }

    }

    override fun getItemCount(): Int {
        return intervals.size
    }
}