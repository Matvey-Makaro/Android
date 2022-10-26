package com.example.converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider

class ConverterActivity : AppCompatActivity() {
    var unitsOfGroupInt = R.array.distanceUnits
    lateinit var converterViewModel: ConverterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        val dataFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerData) as DataFragment

        converterViewModel = ViewModelProvider(this)[ConverterViewModel::class.java]
        dataFragment.converterViewModel = converterViewModel

        unitsOfGroupInt = intent.getIntExtra("unitGroupName", 0)
        dataFragment.unitsOfGroupInt = unitsOfGroupInt





    }
}