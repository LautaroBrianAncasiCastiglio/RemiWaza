package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityRegistroRemisero : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_remisero)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonRegistro = findViewById<Button>(R.id.btnRegistro)
        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonAgencia = findViewById<Button>(R.id.btnAgencia)

        buttonRegistro.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener {
            val intent = Intent(applicationContext, ActivityLoginRemisero::class.java)
            startActivity(intent)
        }
        buttonAgencia.setOnClickListener {
            val intent = Intent(applicationContext, ActivityRegistroAgencia::class.java)
            startActivity(intent)
        }
    }
}
