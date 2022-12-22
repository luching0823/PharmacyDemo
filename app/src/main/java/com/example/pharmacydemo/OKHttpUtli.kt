package com.example.pharmacydemo
import okhttp3.*
import okio.IOException

public class OKHttpUtli {
    private var mOkHttpClient: OkHttpClient? = null

    companion object {
        val mOKHttpUtli: OKHttpUtli by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            OKHttpUtli()
        }
    }

    init {
        // 宣告 OKHttpClient
        mOkHttpClient = OkHttpClient().newBuilder().build()
    }

    // Get 非同步
    fun getAsync( url: String, callBack: ICallback ) {
        // 宣告 request
        val request = with(Request.Builder()) {
            url( url )
            get()
            build()
        }

        // 宣告 Call
        val call = mOkHttpClient?.newCall(request)

        // 執行 Call 連線後，採用 enqueue 非同步方式，獲取到回應的結果資料
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                callBack.onFailure(e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                callBack.onResponse(response)
            }
        })
    }

    interface ICallback {
        fun onResponse(response: Response)

        fun onFailure(e: IOException)
    }
}