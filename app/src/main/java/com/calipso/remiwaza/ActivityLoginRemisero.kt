package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class ActivityLoginRemisero : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_remisero)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val buttonRegistro = findViewById<Button>(R.id.btnRegistro)
        val buttonAgencia = findViewById<Button>(R.id.btnAgencia)

        buttonRegistro.setOnClickListener {
            val intent = Intent(applicationContext, ActivityRegistroRemisero::class.java)
            startActivity(intent)
        }

        buttonAgencia.setOnClickListener {
            val intent = Intent(applicationContext, ActivityLoginAgencia::class.java)
            startActivity(intent)
        }

        val textEmail = findViewById<EditText>(R.id.textMailRemiseroLog)
        val textPassword = findViewById<EditText>(R.id.textPasswordRemiseroLog)
        val btnLogin = findViewById<Button>(R.id.btnLoginRemisero)

        btnLogin.setOnClickListener {
            val email = textEmail.text.toString().trim()
            val password = textPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {

        val intent = Intent(this, ActivityInicioRemisero::class.java)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, redirigir al usuario a la página principal
                    startActivity(intent)
                    finish() // Cerrar la actividad de login
                } else {
                    // Si falla el login, muestra un mensaje de error
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(this, "Error en el login: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
