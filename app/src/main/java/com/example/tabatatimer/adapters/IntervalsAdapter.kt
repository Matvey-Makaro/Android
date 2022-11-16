package com.example.tabatatimer.adapters

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.R
import com.example.tabatatimer.database.models.Interval
import com.example.tabatatimer.database.models.TrainingActionType
import com.example.tabatatimer.viewmodels.TrainingSettingsViewModel

class IntervalsAdapter(
    private var context: Context,
    private var trainingSettingsViewModel: TrainingSettingsViewModel
) : RecyclerView.Adapter<IntervalsAdapter.IntervalViewHolder>()
{
    class IntervalViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    {
        val intervalNumber : TextView = view.findViewById(com.example.tabatatimer.R.id.intervalNumberTextView)
        val intervalTime : TextView = view.findViewById(com.example.tabatatimer.R.id.intervalTimeTextView)
        val addTime : Button = view.findViewById(com.example.tabatatimer.R.id.addTimeButton)
        val decTime : Button = view.findViewById(com.example.tabatatimer.R.id.decTimeButton)
        val changeIntervalTypeSpinner : Spinner = view.findViewById(com.example.tabatatimer.R.id.chgIntervalTypeSpinner)
        val description : EditText = view.findViewById(com.example.tabatatimer.R.id.descriptionEditText)
        val curIntervalType : TextView = view.findViewById(com.example.tabatatimer.R.id.curIntervalTypeTextView)
        val addSpinner: Spinner = view.findViewById(com.example.tabatatimer.R.id.addSpinner)
        val layout: ConstraintLayout = view.findViewById(com.example.tabatatimer.R.id.intervalConstraintLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntervalViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(com.example.tabatatimer.R.layout.interval_item, parent, false)
        return IntervalsAdapter.IntervalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IntervalViewHolder, position: Int)
    {
        var item: Interval = trainingSettingsViewModel.curTrainingWithIntervals?.intervals?.get(position)!!

        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        val theme : Boolean = sharedPreference.getBoolean("theme_switch_preference", false)
        val font : String? = sharedPreference.getString("font_preference", "-1")
        holder.layout.setBackgroundColor( if (theme) {
            Color.parseColor("#5e5e5e")} else {
            Color.parseColor("#FFFFFF")})

        if (font == "1") {
            holder.intervalTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (52 *0.5).toFloat())
            holder.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, (52 *0.5).toFloat())
            holder.curIntervalType.setTextSize(TypedValue.COMPLEX_UNIT_PX, (52 *0.5).toFloat())
        }
        else if (font == "3") {
            holder.intervalTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (52 * 1.5).toFloat())
            holder.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, (52 * 1.5).toFloat())
            holder.curIntervalType.setTextSize(TypedValue.COMPLEX_UNIT_PX, (52 * 1.5).toFloat())
        }

        holder.intervalNumber.text = (position + 1).toString()
        holder.intervalTime.setText(item.intervalTime.toString())

        holder.addTime.setOnClickListener{
            // var intervalTime = holder.intervalTime.text.toString().toInt()
            item = trainingSettingsViewModel.curTrainingWithIntervals?.intervals?.find {it.interval_id == item.interval_id}!!
            item.intervalTime++
            holder.intervalTime.setText(item.intervalTime.toString())
            trainingSettingsViewModel.dao?.updateInterval(item)
            trainingSettingsViewModel.curTrainingWithIntervals?.intervals
                ?.find { it.interval_id == item.interval_id }!!.intervalTime = item.intervalTime
        }

        holder.decTime.setOnClickListener {
            item = trainingSettingsViewModel.curTrainingWithIntervals?.intervals?.find { it.interval_id == item.interval_id }!!
            if(item.intervalTime > 0)
            {
                item.intervalTime--
                holder.intervalTime.setText(item.intervalTime.toString())
                trainingSettingsViewModel.dao?.updateInterval(item)
                trainingSettingsViewModel.curTrainingWithIntervals?.intervals
                    ?.find { it.interval_id == item.interval_id }!!.intervalTime = item.intervalTime
            }
        }

        holder.description.setText( if(item.description == "") {
            context.getString(com.example.tabatatimer.R.string.description)
        }
        else{
            item.description
        })

        ArrayAdapter.createFromResource(
            context,
            R.array.interval_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            holder.changeIntervalTypeSpinner.adapter = adapter
        }

        holder.changeIntervalTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                item = trainingSettingsViewModel.curTrainingWithIntervals?.intervals
                    ?.find { it.interval_id == item.interval_id }!!
                val choose: Array<String> = context.resources.getStringArray(R.array.interval_types)

                if(position == 0)
                {
                    holder.changeIntervalTypeSpinner.setSelection(item.actionType.ordinal +1)
                    return
                }

                if(position == 6)
                {
                    trainingSettingsViewModel.deleteInterval(item.order)
                    notifyItemRemoved(item.order - 1)

                    for(i in ((item.order - 1)..
                            (trainingSettingsViewModel.curTrainingWithIntervals?.intervals?.size!! - 1)))
                    {
                        notifyItemChanged(i)
                    }
                    return
                }

                holder.curIntervalType.text = choose[position]
                holder.changeIntervalTypeSpinner.background = when (position) {
                    1 -> ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_directions_walk_24, context.theme)
                    2 -> ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_fitness_center_24, context.theme)
                    3 -> ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_accessibility_new_24, context.theme)
                    4 -> ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_event_seat_24, context.theme)
                    5 -> ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_chair_24, context.theme)
                    else -> ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_fitness_center_24, context.theme)
                }

                item.actionType = TrainingActionType.values()[position - 1]
                trainingSettingsViewModel.curTrainingWithIntervals?.intervals!![item.order - 1].actionType = item.actionType
                trainingSettingsViewModel.dao?.updateInterval(item)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        ArrayAdapter.createFromResource(
            context,
            R.array.add_interval_up_down,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            holder.addSpinner.adapter = adapter
        }

        holder.addSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                item = trainingSettingsViewModel.curTrainingWithIntervals?.intervals
                    ?.find { it.interval_id == item.interval_id }!!

                if(position == 0)
                    return

                val choose: Array<String> = context.resources.getStringArray(R.array.interval_types)
                Toast.makeText(context, "Added new interval", Toast.LENGTH_SHORT).show()

                val addPos = position - 1
                trainingSettingsViewModel.addInterval(item.order + addPos)
                holder.addSpinner.setSelection(0)

                var pos = 0
                if(addPos == 0)
                    pos = item.order - 1
                else
                    pos = item.order + 1

                notifyItemInserted(pos - 1)

                for(i in (pos .. (trainingSettingsViewModel.curTrainingWithIntervals?.intervals?.size!! -1)))
                    notifyItemChanged(i)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    override fun getItemCount(): Int
    {
        val result: Int? = trainingSettingsViewModel.curTrainingWithIntervals?.intervals?.size
        return result ?: 0
    }
}