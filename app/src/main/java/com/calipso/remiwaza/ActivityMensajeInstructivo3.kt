package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityMensajeInstructivo3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mensaje_instructivo3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button = findViewById<Button>(R.id.btnPreview)
        val buttonRemisero = findViewById<Button>(R.id.btnRemisero)
        val buttonAgencia = findViewById<Button>(R.id.btnAgencia)

        button.setOnClickListener{
            val intent1= Intent(applicationContext, ActivityMensajeInstructivo2::class.java)
            startActivity(intent1)
        }
        buttonRemisero.setOnClickListener{
            val intent2= Intent(applicationContext, ActivityRegistroRemisero::class.java)
            startActivity(intent2)
        }
        buttonAgencia.setOnClickListener{
            val intent3= Intent(applicationContext, ActivityRegistroAgencia::class.java)
            startActivity(intent3)
        }
    }
}