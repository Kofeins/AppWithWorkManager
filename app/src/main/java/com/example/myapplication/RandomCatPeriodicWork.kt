package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker

import androidx.work.WorkerParameters


class RandomCatPeriodicWork(context: Context, userParameters: WorkerParameters) :
    Worker(context, userParameters) {



    override fun doWork(): Result {

        try {

            val outputData = Data.Builder()
                .putString("CAT", getCatNumber())
                .build()

            return Result.success(outputData)
        } catch (e: Exception) {
            return Result.failure()
        }

    }

    private fun getCatNumber(): String {



        return (1..6).random().toString()
    }

}