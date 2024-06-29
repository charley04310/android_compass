package com.example.app

object AzimuthStore {
    private var azimuth: Float = 0f

    fun setAzimuth(value: Float) {
        azimuth = value
    }

    fun getAzimuth(): Float {
        return azimuth
    }
}
