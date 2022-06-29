package com.example.appericolo.sharelocation.fcm

import com.example.appericolo.utils.CommonInfo.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Classe per la gestione delle chiamate API
 */
class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder() //definisco il client http
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        //creo l'api da prendere dall'istanza di retrofit
        val api by lazy { //creo la richiesta post del client verso firebase
            retrofit.create(NotificationAPI::class.java)
        }
    }
}