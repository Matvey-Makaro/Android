package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.view.WindowManager
import com.example.lab3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_work_space, WorkSpaceFragment())
                .commit()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_basic_functions, BasicFunctionsFragment())
            .commit()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_scientific_functions, ScientificFunctionsFragment())
            .commit()
    }
}