package ru.spbstu.insys.chatclient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.spbstu.insys.chatclient.R
import ru.spbstu.insys.chatclient.data.model.Message
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class MessageActivity : AppCompatActivity() {
    lateinit var viewModel: MessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        val baseUrl = checkNotNull(intent.getStringExtra("BASE_URL"))
        val nickname = checkNotNull(intent.getStringExtra("NICKNAME"))

        val viewModelFactory = MessageViewModel.MessageViewModelFactory(baseUrl, nickname)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MessageViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val messageAdapter = MessageAdapter()
        recyclerView.adapter = messageAdapter

        viewModel.messageList.observe(this, Observer {
            messageAdapter.submitList(it)
        })
    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        val text = editText.text.toString()
        if (text.isEmpty()) {
            return
        }
        val message = Message(date = Date(), nickname = viewModel.nickname, text = text)
        viewModel.endpoints.sendMessage(message).enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                editText.text.clear()
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                Toast.makeText(this@MessageActivity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    class MessageAdapter :
        ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback) {
        class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val date = itemView.findViewById<TextView>(R.id.date)
            private val nickname = itemView.findViewById<TextView>(R.id.nickname)
            private val text = itemView.findViewById<TextView>(R.id.message)

            fun bind(message: Message) {
                date.text = hoursAndMinutes.format(message.date)
                nickname.text = message.nickname
                text.text = message.text
            }
            companion object {
                val hoursAndMinutes: Format = SimpleDateFormat("HH.mm", Locale.ENGLISH)
            }
        }

        object MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message, parent, false)
            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = getItem(position)
            holder.bind(message)
        }
    }
}