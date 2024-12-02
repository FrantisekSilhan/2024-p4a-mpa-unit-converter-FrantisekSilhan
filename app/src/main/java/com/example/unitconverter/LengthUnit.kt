package com.example.unitconverter

enum class LengthUnit {
    METERS, KILOMETERS, FEET, INCHES, YARDS, MILES;

    fun getName(): String = when (this) {
        METERS -> "Meters"
        KILOMETERS -> "Kilometers"
        FEET -> "Feet"
        INCHES -> "Inches"
        YARDS -> "Yards"
        MILES -> "Miles"
    }

    fun getAbbreviation(): String = when (this) {
        METERS -> "m"
        KILOMETERS -> "km"
        FEET -> "ft"
        INCHES -> "inch"
        YARDS -> "yd"
        MILES -> "mi"
    }
}