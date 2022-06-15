package com.example.appericolo.sharelocation.fcm

import com.example.appericolo.utils.CommonInfo.SERVER_KEY
import com.example.appericolo.utils.CommonInfo.CONTENT_TYPE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>}