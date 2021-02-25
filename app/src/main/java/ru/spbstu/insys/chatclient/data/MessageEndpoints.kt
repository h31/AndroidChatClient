package ru.spbstu.insys.chatclient.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.spbstu.insys.chatclient.data.model.Message

interface MessageEndpoints {
    @GET("messages/")
    fun messageList(): Call<List<Message>>

    @POST("messages/")
    fun sendMessage(@Body message: Message): Call<Message>
}