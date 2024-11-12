package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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


        val intent4 = Intent(this, ActivityInicioRemisero::class.java)
        val intent5 = Intent(this, ActivityInicioAgencia::class.java)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // El usuario está autenticado, obtener el tipo de usuario desde la base de datos
            val userId = user.uid
            getUserType(userId) { userType ->
                when (userType) {
                    "remisero" -> {
                        // Redirigir al inicio de remisero
                        startActivity(intent4)
                    }
                    "agencia" -> {
                        // Redirigir al inicio de agencia
                        startActivity(intent5)
                    }
                    else -> {
                        // En caso de que el tipo de usuario no esté definido, redirigir a una pantalla de error o elegir tipo
                        showScreen()
                    }
                }
                finish() // Finaliza la actividad actual para evitar que el usuario regrese a la pantalla de login
            }
        } else {
            showScreen()
        }


        showScreen()
    }

    private fun getUserType(userId: String, callback: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userId).child("type").get().addOnSuccessListener { snapshot ->
            val userType = snapshot.getValue(String::class.java) ?: "unknown"
            callback(userType)  // Devolver el tipo de usuario al callback
        }.addOnFailureListener { e ->
            callback("unknown")  // En caso de error, devuelve un tipo desconocido
        }
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