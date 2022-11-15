package com.example.tabatatimer

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        refreshFragment()
    }

    fun refreshFragment()
    {
        var fragment: SettingsFragment = SettingsFragment()
        fragment.activity = this

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()

        updateTheme()
    }

    private fun updateTheme() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme : Boolean = sharedPref.getBoolean("theme_switch_preference", false)
        val bcg : ConstraintLayout = findViewById(R.id.settingConstraintLayout)

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

    private fun toMainActivity()
    {
        val mainIntent = Intent(this, MainActivity::class.java)
        this.startActivity(mainIntent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        toMainActivity()
        return true
    }

    override fun onBackPressed() {
        toMainActivity()
    }
}