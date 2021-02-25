package ru.spbstu.insys.chatclient.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import ru.spbstu.insys.chatclient.controller.MessageFetchThread
import ru.spbstu.insys.chatclient.data.RetrofitClient
import ru.spbstu.insys.chatclient.data.model.Message

class MessageViewModel(val baseUrl: String,
                       val nickname: String) : ViewModel() {
    val endpoints = RetrofitClient.buildMessageService(baseUrl)
    val messageList = MutableLiveData<List<Message>>()
    private val thread = MessageFetchThread(endpoints, messageList)

    init {
        thread.start()
    }

    override fun onCleared() {
        thread.interrupt()
    }

    class MessageViewModelFactory(private val baseUrl: String,
                                  private val nickname: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MessageViewModel(baseUrl, nickname) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}