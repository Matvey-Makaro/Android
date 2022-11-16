package com.example.tabatatimer

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.adapters.TrainingsAdapter
import com.example.tabatatimer.database.models.Training
import com.example.tabatatimer.database.models.TrainingWithIntervals
import com.example.tabatatimer.databinding.ActivityMainBinding
import com.example.tabatatimer.viewmodels.TrainingsViewModel
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = sharedPreferences.getBoolean("theme_switch_preference", false)
        val lang = sharedPreferences.getBoolean("language_switch_preference", false)
        if(lang)
            setLocale("ru")
        else setLocale("")

        val viewModel = ViewModelProvider(this).get(TrainingsViewModel::class.java)
        viewModel.initVars(application)

        val recyclerView : RecyclerView = findViewById(R.id.trainingsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrainingsAdapter(this, viewModel)

        val addBtn : Button = findViewById(R.id.addTrainingButton)
        addBtn.text = getString(R.string.add_btn_text)
        addBtn.setOnClickListener{
            val basicTraining: Training = TrainingWithIntervals.getBasicTraining(this)
            basicTraining.Name = getString(R.string.select_name)
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

        updateTheme()
    }

    override fun onBackPressed()
    {
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if(item.itemId == R.id.change_application_settings)
        {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            this.startActivity(settingsIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setLocale(language: String)
    {
        val locale: Locale = Locale(language)
        Locale.setDefault(locale)

        val conf: Configuration = Configuration()
        conf.setLocale(locale)

        baseContext.resources.updateConfiguration(conf, baseContext.resources.displayMetrics)
    }

    fun updateTheme() {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val theme : Boolean = sharedPreference.getBoolean("theme_switch_preference", false)

        val bcg : ConstraintLayout = findViewById(R.id.mainConstraintLayout)

        if(theme)
        {

            bcg.setBackgroundColor(Color.parseColor("#5e5e5e"))
            val col: ColorDrawable = ColorDrawable(Color.parseColor("#000000"))
            getSupportActionBar()?.setBackgroundDrawable(col)
        }

        else {
            bcg.setBackgroundColor(Color.parseColor("#FFFFFF"))
            val col : ColorDrawable = ColorDrawable(Color.parseColor("#FF6200EE"))
            getSupportActionBar()?.setBackgroundDrawable(col)
        }
    }
}