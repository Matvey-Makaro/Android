package com.example.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class ConverterViewModel : ViewModel()
{
    var valueFrom : MutableLiveData<BigDecimal> = MutableLiveData()
    var valueTo : MutableLiveData<BigDecimal> = MutableLiveData()
    var unitFrom : MutableLiveData<Unit> = MutableLiveData()
    var unitTo : MutableLiveData<Unit> = MutableLiveData()

    init
    {
        valueFrom.value = BigDecimal.ZERO
    }

    fun convert()
    {
        if(unitFrom.value == null || unitTo.value == null)
            return

        if(unitFrom.value!!.group != unitTo.value!!.group)
            return;

        val coeffFrom = unitFrom.value!!.multiplier
        val coeffTo = unitTo.value!!.multiplier
        valueTo.value = valueFrom.value?.multiply(BigDecimal(coeffFrom))!!.divide(BigDecimal(coeffTo)).stripTrailingZeros()

    }

    fun setValueFrom(value: BigDecimal)
    {
        valueFrom.value = value
        convert()
    }

    fun setUnitFrom(unit: com.example.converter.Unit)
    {
        unitFrom.value = unit
        convert()
    }

    fun setUnitTo(unit: com.example.converter.Unit)
    {
        unitTo.value = unit
        convert()
    }

    fun getValueTo(): BigDecimal
    {
        if(valueTo.value != null)
            return valueTo.value!!
        return BigDecimal.ZERO
    }

    fun swap()
    {
        var tmpUnit = unitFrom
        unitFrom = unitTo
        unitTo = tmpUnit
        convert()
    }
}