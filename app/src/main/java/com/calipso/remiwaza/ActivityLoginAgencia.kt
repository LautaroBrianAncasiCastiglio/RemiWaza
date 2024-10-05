package com.calipso.remiwaza

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityLoginAgencia : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_agencia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonRegistro= findViewById<Button>(R.id.btnRegistro)
        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonAgencia = findViewById<Button>(R.id.btnRemisero)

        buttonRegistro.setOnClickListener{
            val intent1= Intent(applicationContext, ActivityRegistroAgencia::class.java)
            startActivity(intent1)
        }
        buttonLogin.setOnClickListener{
            val intent2= Intent(applicationContext, ActivityInicioAgencia::class.java)
            startActivity(intent2)
        }
        buttonAgencia.setOnClickListener{
            val intent3= Intent(applicationContext, ActivityLoginRemisero::class.java)
           startActivity(intent3)
        }
    }
}