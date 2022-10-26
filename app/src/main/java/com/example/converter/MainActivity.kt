package com.example.converter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val key = "unitGroupName"
        val intentToConverterActivity = Intent(this, ConverterActivity::class.java)

        val distanceButton = findViewById<Button>(R.id.button_distance)
        distanceButton.setOnClickListener{
            intentToConverterActivity.putExtra(key, R.array.distanceUnits)
            startActivity(intentToConverterActivity)
        }

        val weightButton = findViewById<Button>(R.id.button_weight)
        weightButton.setOnClickListener{
            intentToConverterActivity.putExtra(key, R.array.weightUnits)
            startActivity(intentToConverterActivity)
        }

        val speedButton = findViewById<Button>(R.id.button_speed)
        speedButton.setOnClickListener{
            intentToConverterActivity.putExtra(key, R.array.speedUnits)
            startActivity(intentToConverterActivity)
        }

    }
}