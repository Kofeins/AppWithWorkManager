package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker

import androidx.work.WorkerParameters


class MarsWeatherWorker(context: Context, userParameters: WorkerParameters) :
    Worker(context, userParameters) {



    override fun doWork(): Result {

        try {

            val outputData = Data.Builder()
                .putString("WEATHER", getMarsWeather())
                .build()

            return Result.success(outputData)
        } catch (e: Exception) {
            return Result.failure()
        }

    }

    fun getMarsWeather(): String {

        val weatherList = listOf("Sunny", "Cloudy", "Foggy", "Murky", "Snowfall", "Windy", "Stormy")

        return weatherList.random()
    }

}