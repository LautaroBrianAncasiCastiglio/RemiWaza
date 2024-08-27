package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityMensajeInstructivo2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mensaje_instructivo2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button1 = findViewById<Button>(R.id.bntNext)
        val button2 = findViewById<Button>(R.id.btnPreview)

        button1.setOnClickListener{
            val intent= Intent(applicationContext, ActivityMensajeInstructivo3::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener{
            val intent= Intent(applicationContext, ActivityMensajeInstructivo1::class.java)
            startActivity(intent)
        }

    }
}