package nl.medify.doctoruser.common.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MedifyRetrofitAPI {
    companion object {
        // The base url off the api.
        private const val baseUrl = "http://35.181.79.21:8080/"

        // the lazy keyword makes sure the createApi function is not called until these properties are accessed
        val apiService by lazy { createApi() }

        /**
         * @return [MedifyAPIService] The service class off the retrofit client.
         */
        private fun createApi(): MedifyAPIService {
            // Create an OkHttpClient to be able to make a log of the network traffic
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            // Create the Retrofit instance
            val patientApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Return the Retrofit PatientApiService
            return patientApi.create(MedifyAPIService::class.java)
        }
    }
}