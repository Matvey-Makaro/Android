package com.example.tabatatimer.adapters

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.preference.PreferenceManager
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
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        val theme : Boolean = sharedPreference.getBoolean("theme_switch_preference", false)
        val font : String? = sharedPreference.getString("font_preference", "-1")
        if (font == "1") {
            holder.interval_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX,  (52 * 0.5).toFloat())
        }
        else if (font == "3") {
            holder.interval_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (52 * 1.4).toFloat())
        }

        if (position == context.curPos)
            holder.interval_btn.setBackgroundColor(Color.parseColor("#011f73"))
        else
        {
            holder.interval_btn.setBackgroundColor(
                Color.parseColor(
                if (theme) {"#5e5e5e"} else {"#FF6200EE" }
            ))
        }

        holder.interval_btn.text = String.format(
            "%02d. %s",
            position + 1,
            context.resources.getStringArray(R.array.interval_types)[intervals[position].actionType.ordinal + 1]
        )

        holder.interval_btn.setOnClickListener{context.pressBtn(position)}
    }

    override fun getItemCount(): Int {
        return intervals.size
    }
}