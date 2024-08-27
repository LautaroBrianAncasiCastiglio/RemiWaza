package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityRegistroAgencia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_agencia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonRegistro= findViewById<Button>(R.id.btnRegistro)
        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonRemisero = findViewById<Button>(R.id.btnRemisero)

        //buttonRegistro.setOnClickListener{
        //    val intent1= Intent(applicationContext, ActivityInicioAgencia::class.java)
        //    startActivity(intent1)
        //}
        buttonLogin.setOnClickListener{
            val intent2= Intent(applicationContext, ActivityLoginAgencia::class.java)
            startActivity(intent2)
        }
        buttonRemisero.setOnClickListener{
            val intent3= Intent(applicationContext, ActivityRegistroRemisero::class.java)
            startActivity(intent3)
        }
    }
}