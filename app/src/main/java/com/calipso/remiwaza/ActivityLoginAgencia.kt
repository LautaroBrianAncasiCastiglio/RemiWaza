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
import com.google.firebase.database.FirebaseDatabase

class ActivityLoginAgencia : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_agencia)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonRegistro = findViewById<Button>(R.id.btnRegistro)
        val buttonAgencia = findViewById<Button>(R.id.btnRemisero)

        buttonRegistro.setOnClickListener{
            val intent1= Intent(applicationContext, ActivityRegistroAgencia::class.java)
            startActivity(intent1)
        }
        buttonAgencia.setOnClickListener{
            val intent3= Intent(applicationContext, ActivityLoginRemisero::class.java)
            startActivity(intent3)
        }

        auth = FirebaseAuth.getInstance()

        val textEmail = findViewById<EditText>(R.id.textMailAgencia)
        val textPassword = findViewById<EditText>(R.id.textPasswordAgencia)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = textEmail.text.toString().trim()
            val password = textPassword.text.toString().trim()

            // Validación de los campos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar a la función de login
                loginAgency(email, password)
            }
        }
    }

    private fun loginAgency(email: String, password: String) {

        val intent = Intent(this, ActivityPerfilAgencia::class.java)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                        // Redirigir a la página principal de la agencia
                        startActivity(intent)
                        finish() // Cerrar la actividad de login para que no regrese
                } else {
                    // Si falla el login, muestra un mensaje de error
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(this, "Error en el login: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
