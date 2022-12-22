package com.example.pharmacydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPharmacyData()
    }

    private fun getPharmacyData() {
        // 資料網址
        val pharmaciesDataUrl = "https://raw.githubusercontent.com/thishkt/pharmacies/master/data/info.json"

        // 宣告 OKHttpClient
        // 利用 HttpLoggingInterceptor 印出連線資訊
        val okHttpClient = OkHttpClient().newBuilder().addInterceptor(
            HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                }
                else{
                    HttpLoggingInterceptor.Level.NONE
                }
            )
        ).build()

        // 宣告 Request，連線到指定網址
        val request: Request = Request.Builder().url(pharmaciesDataUrl).get().build()

        // 宣告 Call
        val call = okHttpClient.newCall(request)

        // 執行 Call 連線後，採用 enqueue 非同步方式，獲取到回應的結果資料
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Log", "onFailure: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("Log", "onResponse: ${response.body?.string()}")
            }
        })
    }
}