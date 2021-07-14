package api

import android.content.Context
import api.interfaces.ArtfeltApiService
import com.artfelt.artfelt.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArtfeltClient {

    private lateinit var apiService: ArtfeltApiService

    fun getApiService(context: Context): ArtfeltApiService {
        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient(context))
                .build()

            apiService = retrofit.create(ArtfeltApiService::class.java)
        }
        return apiService
    }


    private fun okHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }
}