package com.calipso.remiwaza

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



class ActivityAdvertencia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_advertencia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fullscreenButton = findViewById<Button>(R.id.fullscreenButton)
        fullscreenButton.setOnClickListener {
            val intent = Intent(this, ActivityMensajeInstructivo1::class.java)
            startActivity(intent)
        }

        showScreen()
    }
    private fun showScreen(){
        object : CountDownTimer(4000,1000){
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, ActivityMensajeInstructivo1::class.java)
                startActivity(intent)
                finish()
            }

        }.start()
    }
}