package com.example.converter

import java.math.BigDecimal

object UnitRepository
{
    private val distanceUnits: List<com.example.converter.Unit> by lazy {
        listOf(
            Unit(R.string.m, 1000, UnitGroup.DISTANCE),
            Unit(R.string.km, 1000000, UnitGroup.DISTANCE),
            Unit(R.string.mm, 1, UnitGroup.DISTANCE)
        )
    }

    private val weightUnits: List<com.example.converter.Unit> by lazy{
        listOf(
            Unit(R.string.kg, 1000, UnitGroup.WEIGHT),
            Unit(R.string.t, 1000000, UnitGroup.WEIGHT),
            Unit(R.string.g, 1, UnitGroup.WEIGHT)
        )
    }

    private val speedUnits: List<com.example.converter.Unit> by lazy{
        listOf(
            Unit(R.string.m_per_s, 1000, UnitGroup.SPEED),
            Unit(R.string.km_per_s, 1000000, UnitGroup.SPEED),
            Unit(R.string.mm_per_s, 1, UnitGroup.SPEED)
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