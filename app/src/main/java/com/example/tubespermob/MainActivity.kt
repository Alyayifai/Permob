package com.example.tubespermob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespermob.adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

const val BASE_URL = "https://api.currentsapi.services"

class MainActivity : AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeAPIRequest()
    }

    private fun fadeInFromBlack(){
        v_blackScreen.animate().apply{
            alpha(0f)
            duration = 3000
        }.start()
    }

    private fun setUpRecyclerView(){
        rv_recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        rv_recyclerView.adapter = RecyclerAdapter(titlesList, descList, imagesList, linksList)
    }

    private fun addToList (title: String, description: String, image: String, link: String){
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
        linksList.add(link)
    }


    private fun makeAPIRequest(){
        progressBar.visibility = View.VISIBLE

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        GlobalScope.launch (Dispatchers.IO) {
            try {
                val response= api.getNews()
                for (article in response.news){
                    Log.i("MainActifity", "Result = $article")
                    addToList(article.title, article.description, article.image, article.url)
                }

                withContext(Dispatchers.Main){
                    setUpRecyclerView()
                    fadeInFromBlack()
                    progressBar.visibility = View.GONE

                }

            }catch (e: Exception){
                Log.e("MainActivity", e.toString())

                withContext(Dispatchers.Main){
                    attemptRequestAgain()
                }
            }
        }
    }
    private fun attemptRequestAgain(){
        countDownTimer = object : CountDownTimer(5*1000,1000){
            override fun onFinish() {
                makeAPIRequest()
                countDownTimer.cancel()
            }

            override fun onTick(millisUntilFinidhed :Long) {
                Log.i("MainActivity", "Cloud not retrieve data... Trying again in ${millisUntilFinidhed/1000}seconds")
            }

        }
        countDownTimer.start()
    }
}
