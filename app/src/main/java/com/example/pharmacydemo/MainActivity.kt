package com.example.pharmacydemo

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.pharmacydemo.OKHttpUtli.Companion.mOKHttpUtli
import com.example.pharmacydemo.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPharmacyData()
    }

    private fun getPharmacyData() {
        runOnUiThread {
            binding.progressBar.visibility = View.VISIBLE
        }

        // 資料網址
        val pharmaciesDataUrl = PHARMACIES_DATA_URL

        mOKHttpUtli.getAsync(pharmaciesDataUrl, object : OKHttpUtli.ICallback {
            override fun onResponse(response: Response) {
                val pharmaciesData = response.body?.string()
                val pharmacyInfo = Gson().fromJson(pharmaciesData, PharmacyInfo::class.java)

                val pharmacyNames = StringBuilder()
                for (feature in pharmacyInfo.features) {
                    pharmacyNames.append(feature.property.name + "\n")
                }

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.tvPharmaciesData.text = pharmacyNames
                }
            }

            override fun onFailure(e: okio.IOException) {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                }

                Log.d("Log", "onFailure: $e")
            }
        })
//        // 宣告 OKHttpClient
//        // 利用 HttpLoggingInterceptor 印出連線資訊
//        val okHttpClient = OkHttpClient().newBuilder().addInterceptor(
//            HttpLoggingInterceptor().setLevel(
//                if (BuildConfig.DEBUG) {
//                    HttpLoggingInterceptor.Level.BASIC
//                }
//                else{
//                    HttpLoggingInterceptor.Level.NONE
//                }
//            )
//        ).build()
//
//        // 宣告 Request，連線到指定網址
//        val request: Request = Request.Builder().url(pharmaciesDataUrl).get().build()
//
//        // 宣告 Call
//        val call = okHttpClient.newCall(request)
//
//        // 執行 Call 連線後，採用 enqueue 非同步方式，獲取到回應的結果資料
//        call.enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    binding.progressBar.visibility = View.GONE
//                }
//
//                Log.d("Log", "onFailure: $e")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//
//                val pharmaciesData = response.body?.string()
//                val pharmacyInfo = Gson().fromJson(pharmaciesData, PharmacyInfo::class.java)
//
//                val pharmacyNames = StringBuilder()
//                for (feature in pharmacyInfo.features) {
//                    pharmacyNames.append(feature.property.name + "\n")
//                }
//
////                // 整包字串資料轉成 JSONObject 格式
////                val jsonObj = JSONObject(pharmaciesData)
////
////                // features 是一個陣列，因此需要轉換成 JSON Array
////                val featuresArray = JSONArray(jsonObj.getString("features"))
////
////                // 藥局名稱
////                // 使用 String 處理串接文字，當資料很多時很容易造成 OOM 記憶體不足，建議使用 StringBuilder
////                val pharmacyNames = StringBuilder()
////                for (features in 0 until featuresArray.length()) {
////                    val properties = featuresArray.getJSONObject(features).getString("properties")
////                    val propertiesObj = JSONObject(properties)
////
////                    // 將每次獲取到的藥局名稱，多加跳行符號，存到變數中
////                    pharmacyNames.append(propertiesObj.getString("name") + "\n")
////                }
//
//                runOnUiThread {
//                    binding.progressBar.visibility = View.GONE
//                    binding.tvPharmaciesData.text = pharmacyNames
//                }
//
//            }
//        })
    }
}

class PharmacyInfo(
    @SerializedName("features")
    val features: List<Feature>
)

class Feature(
    @SerializedName("properties")
    val property: Property
)

class Property(
    @SerializedName("name")
    val name: String
)