package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*

import androidx.work.WorkManager
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var T1: TextView?= null
    private var showCat: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        T1 = binding.kostyl1
        showCat = binding.cat


        binding.weatherBtn.setOnClickListener{
            setOneTimeMarsWeatherRequest()
        }

        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setRandomCatPeriodicWorkRequest()
            } else {
                cancelRandomCatPeriodicWork()
            }
        }
    }

    private fun marsKostyl(mars: String){
        T1!!.text = mars
    }

    private fun catKostyl(cat: String){
        when(cat.toInt()){
            1 -> showCat!!.setImageResource(R.drawable.cat_1)
            2 -> showCat!!.setImageResource(R.drawable.cat_2)
            3 -> showCat!!.setImageResource(R.drawable.cat_3)
            4 -> showCat!!.setImageResource(R.drawable.cat_4)
            5 -> showCat!!.setImageResource(R.drawable.cat_5)
            6 -> showCat!!.setImageResource(R.drawable.cat_6)
        }
    }

    private fun cancelRandomCatPeriodicWork(){
        WorkManager.getInstance(this).cancelAllWorkByTag("cat");
    }

    private fun setOneTimeMarsWeatherRequest() {

        val workManager = WorkManager.getInstance(this)



        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val marsWeatherRequest = OneTimeWorkRequestBuilder<MarsWeatherWorker>()

            .setConstraints(constraints)
            .build()

        workManager.enqueue(marsWeatherRequest)

        workManager.getWorkInfoByIdLiveData(marsWeatherRequest.id)
            .observe(this, Observer { workInfo ->


                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val weatherResult = workInfo.outputData.getString("WEATHER")
                    marsKostyl(weatherResult.toString())
                    Toast.makeText(this, weatherResult, Toast.LENGTH_SHORT).show()
                }
                else if (workInfo.state == WorkInfo.State.FAILED) {

                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            })
    }




//Не работает, не понимаю почему
    private fun setRandomCatPeriodicWorkRequest(){

        val workManager = WorkManager.getInstance(this)


        val constraints = Constraints.Builder()//Думал буду подгружать картиночки котов, НО что то не получилось с API
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val randomCatPeriodicWorkRequest = PeriodicWorkRequestBuilder<RandomCatPeriodicWork>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("cat")
            .build()


        workManager.enqueue(randomCatPeriodicWorkRequest)

        workManager.getWorkInfoByIdLiveData(randomCatPeriodicWorkRequest.id)
            .observe(this, Observer { workInfo ->


                when(workInfo.state){
                    WorkInfo.State.RUNNING ->{
                        Toast.makeText(this, "Running", Toast.LENGTH_SHORT).show()
                    }
                    WorkInfo.State.SUCCEEDED ->{
                        val catResult = workInfo.outputData.getString("CAT")
                        Toast.makeText(this, catResult, Toast.LENGTH_SHORT).show()
                        catKostyl(catResult.toString())
                    }
                    WorkInfo.State.FAILED ->{
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }

                    WorkInfo.State.CANCELLED ->{
                        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
                    }
                    else -> {//Мне кажется вот тут у меня ошибка
                        Toast.makeText(this, "Smth else", Toast.LENGTH_SHORT).show()
                    }
                }
                Log.d("MyLog", "work : $workInfo")
            })


    }





}