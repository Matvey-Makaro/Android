package com.example.tabatatimer

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
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
        addIntervalBtn.text = getString(R.string.add_btn_text)
        addIntervalBtn.setOnClickListener{
            viewModel!!.addInterval(viewModel?.curTrainingWithIntervals?.intervals?.size!! + 1)
            recyclerView.adapter!!.notifyItemInserted(viewModel?.curTrainingWithIntervals?.intervals?.size!! - 1)
        }

        updateTheme()
    }

    override fun onBackPressed() {
        val mainIntent = Intent(this, MainActivity::class.java)
        this.startActivity(mainIntent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.training_settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.apply_changes)
        {
            val mainIntent = Intent(this, MainActivity::class.java)
            this.startActivity(mainIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTheme() {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val theme : Boolean = sharedPreference.getBoolean("theme_switch_preference", false)
        val bcg : ConstraintLayout = findViewById(R.id.trainingSettingsConstraintLayout)
        val tainingNameEditText : EditText = findViewById(R.id.trainingNameEditText)

        val font : String? = sharedPreference.getString("font_preference", "-1")


        if (font == "1")
            tainingNameEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, tainingNameEditText.textSize * (0.5).toFloat())

        if (font == "3")
            tainingNameEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, tainingNameEditText.textSize * (1.5).toFloat())

        Toast.makeText(baseContext, font + "/" + tainingNameEditText.textSize.toString(), Toast.LENGTH_LONG).show()

        if(theme) {

            bcg.setBackgroundColor(Color.parseColor("#5e5e5e"))
            val col : ColorDrawable = ColorDrawable(Color.parseColor("#000000"))
            getSupportActionBar()?.setBackgroundDrawable(col)

        } else {
            bcg.setBackgroundColor(Color.parseColor("#FFFFFF"))
            val col : ColorDrawable = ColorDrawable(Color.parseColor("#FF6200EE"))
            getSupportActionBar()?.setBackgroundDrawable(col)

        }
    }
}