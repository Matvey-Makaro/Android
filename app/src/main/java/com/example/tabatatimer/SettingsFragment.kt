package com.example.tabatatimer

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.tabatatimer.database.AppDatabase
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat()
{
    lateinit var activity: SettingsActivity

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val deletePreference: Preference = findPreference("delete_preference")
        val languagePreference: Preference = findPreference("language_switch_preference")
        val themePreference : Preference = findPreference("theme_switch_preference")
        val fontPreference : Preference = findPreference("font_preference")

        deletePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val dao = AppDatabase.getDatabase(requireContext()).trainingDao()
            dao.deleteAllIntervals()
            dao.deleteAllTrainings()
            Toast.makeText(context, getString(R.string.data_cleaned_msg), Toast.LENGTH_SHORT).show()
            true
        }

        languagePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
            val language : String = if ( sharedPreference.getBoolean("language_switch_preference", false) ) { "ru" } else { "" }

            val locale: Locale = Locale(language)
            Locale.setDefault(locale)

            val conf: Configuration = Configuration()
            conf.setLocale(locale)

            context?.resources?.updateConfiguration(conf, context?.resources?.displayMetrics)
            activity.refreshFragment()

            true
        }

        themePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            activity.refreshFragment()
            true
        }

        fontPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            activity.refreshFragment()
            true
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val font: String? = sharedPreferences.getString("font_preference", "-1")
    }
}