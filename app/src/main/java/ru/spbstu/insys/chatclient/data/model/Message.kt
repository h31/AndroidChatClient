package ru.spbstu.insys.chatclient.data.model

import java.time.Instant
import java.util.*

data class Message(
    val date: Date,
    val nickname: String,
    val text: String
)