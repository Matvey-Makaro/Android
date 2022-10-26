package com.example.converter

import androidx.annotation.StringRes
import java.math.BigDecimal

data class Unit(
    @StringRes val Name: Int,
    var multiplier: BigDecimal,
    val group: UnitGroup
)
