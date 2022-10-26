package com.example.converter

import java.math.BigDecimal

object UnitRepository
{
    private val distanceUnits: List<com.example.converter.Unit> by lazy {
        listOf(
            Unit(R.string.m, BigDecimal(1.0), UnitGroup.DISTANCE),
            Unit(R.string.km, BigDecimal(1000.0), UnitGroup.DISTANCE),
            Unit(R.string.mm, BigDecimal(1.0e-3), UnitGroup.DISTANCE)
        )
    }

    private val weightUnits: List<com.example.converter.Unit> by lazy{
        listOf(
            Unit(R.string.kg, BigDecimal(1.0), UnitGroup.WEIGHT),
            Unit(R.string.t, BigDecimal(1000.0), UnitGroup.WEIGHT),
            Unit(R.string.g, BigDecimal(1.0e-3), UnitGroup.WEIGHT)
        )
    }

    private val speedUnits: List<com.example.converter.Unit> by lazy{
        listOf(
            Unit(R.string.m_per_s, BigDecimal(1.0), UnitGroup.SPEED),
            Unit(R.string.km_per_s, BigDecimal(1000.0), UnitGroup.SPEED),
            Unit(R.string.mm_per_s, BigDecimal(1.0e-3), UnitGroup.SPEED)
        )
    }

    private val groupToUnits by lazy {
        hashMapOf(
            UnitGroup.DISTANCE to distanceUnits,
            UnitGroup.WEIGHT to weightUnits,
            UnitGroup.SPEED to speedUnits
        )
    }

    private val allUnits: List<Unit> by lazy {
        groupToUnits.values.flatten()
    }

    fun getUnitById(name: Int): Unit
    {
        return allUnits.first { ((it.Name)) == name }
    }


    fun getUnitsByGroup(unitGroup: UnitGroup) : List<com.example.converter.Unit>
    {
        return groupToUnits.getValue(unitGroup)
    }
}