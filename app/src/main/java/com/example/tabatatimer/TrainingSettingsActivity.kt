package com.example.tabatatimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.adapters.IntervalsAdapter
import com.example.tabatatimer.database.models.Training
import com.example.tabatatimer.viewmodels.TrainingSettingsViewModel

class TrainingSettingsActivity : AppCompatActivity() {
    private var trainingId: Int? = null
    var viewModel: TrainingSettingsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_settings)

        trainingId = intent.getIntExtra("id", -1)
        viewModel = ViewModelProvider(this)[TrainingSettingsViewModel::class.java]
        viewModel?.initVars(application, trainingId!!)

        val trainingNameEditText: EditText = findViewById(R.id.trainingNameEditText)
        trainingNameEditText.setText(viewModel?.curTrainingWithIntervals?.name)
        trainingNameEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel?.curTrainingWithIntervals?.name = s.toString()
                viewModel?.dao?.updateTraining(Training(
                    viewModel?.curTrainingWithIntervals?.id!!,
                    viewModel?.curTrainingWithIntervals?.repeats!!,
                    viewModel?.curTrainingWithIntervals?.color!!,
                    viewModel?.curTrainingWithIntervals?.name!!,
                    viewModel?.curTrainingWithIntervals?.soundEffect!!
                ))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        val recyclerView: RecyclerView = findViewById(R.id.intervalsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = IntervalsAdapter(this, viewModel!!)

        val addIntervalBtn: Button = findViewById(R.id.addIntervalButton)
        addIntervalBtn.setOnClickListener{
            viewModel!!.addInterval(viewModel?.curTrainingWithIntervals?.intervals?.size!! + 1)
            recyclerView.adapter!!.notifyItemInserted(viewModel?.curTrainingWithIntervals?.intervals?.size!! - 1)
        }

        // TODO: updateTheme()
    }

    override fun onBackPressed() {
        val mainIntent = Intent(this, MainActivity::class.java)
        this.startActivity(mainIntent)
        finish()
    }

    // TODO: Дописать фукнции
}