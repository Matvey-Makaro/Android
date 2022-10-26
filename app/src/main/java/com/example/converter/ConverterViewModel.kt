package com.example.converter

import androidx.lifecycle.MutableLiveData
import java.math.BigDecimal
import java.math.RoundingMode

class ConverterViewModel {
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

        var diff = unitFrom.value!!.multiplier.divide(unitTo.value!!.multiplier, 50, RoundingMode.HALF_UP)

        valueTo.value = valueFrom.value?.multiply(diff)?.stripTrailingZeros()
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