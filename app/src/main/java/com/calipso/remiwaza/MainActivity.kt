package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Verificar si ya hay un usuario autenticado
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // El usuario está autenticado, redirigir a la página principal
            val intent = Intent(this, ActivityInicioRemisero::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad de login
        } else {
            // Si no hay usuario autenticado, permitir que se loguee
            val intent = Intent(this, ActivityLoginRemisero::class.java)
            startActivity(intent)
        }

        showScreen()
    }

    private fun showScreen(){
        object : CountDownTimer(3000,1000){
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, ActivityAdvertencia::class.java)
                startActivity(intent)
                finish()
            }

        }.start()
    }
}