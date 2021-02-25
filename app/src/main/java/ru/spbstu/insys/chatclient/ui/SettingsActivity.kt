package ru.spbstu.insys.chatclient.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ru.spbstu.insys.chatclient.R


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val baseUrl = findViewById<EditText>(R.id.baseurl)
        val nickname = findViewById<EditText>(R.id.nickname)
        val start = findViewById<Button>(R.id.start)

        baseUrl.setText(getString(R.string.default_base_url))
        nickname.setText(getString(R.string.default_nickname))

        start.setOnClickListener {
            val intent = Intent(baseContext, MessageActivity::class.java)
            intent.putExtra("BASE_URL", baseUrl.text.toString())
            intent.putExtra("NICKNAME", nickname.text.toString())
            startActivity(intent)
        }
    }
}