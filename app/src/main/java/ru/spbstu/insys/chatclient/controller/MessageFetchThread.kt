package ru.spbstu.insys.chatclient.controller

import androidx.lifecycle.MutableLiveData
import ru.spbstu.insys.chatclient.data.MessageEndpoints
import ru.spbstu.insys.chatclient.data.model.Message

@Suppress("DEPRECATION")
class MessageFetchThread(private val endpoints: MessageEndpoints,
                         private val liveData: MutableLiveData<List<Message>>) : Thread() {
    override fun run() {
        while (!isInterrupted) {
            val response = endpoints.messageList().execute()
            if (!response.isSuccessful) {
                continue
            }
            val messageList = checkNotNull(response.body())
            liveData.postValue(messageList)
            sleep(1000)
        }
    }
}