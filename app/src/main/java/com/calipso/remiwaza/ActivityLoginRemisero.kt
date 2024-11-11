package com.calipso.remiwaza

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class ActivityLoginRemisero : AppCompatActivity() {

    // Declarar auth como lateinit para usarlo en toda la actividad
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_remisero)


        // Inicializamos FirebaseAuth
        auth = FirebaseAuth.getInstance()

        val buttonRegistro = findViewById<Button>(R.id.btnRegistro)
        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonAgencia = findViewById<Button>(R.id.btnAgencia)

        buttonRegistro.setOnClickListener {
            val intent1 = Intent(applicationContext, ActivityRegistroRemisero::class.java)
            startActivity(intent1)
        }

        buttonLogin.setOnClickListener {
            val intent2 = Intent(applicationContext, ActivityInicioRemisero::class.java)
            startActivity(intent2)
        }

        buttonAgencia.setOnClickListener {
            val intent3 = Intent(applicationContext, ActivityLoginAgencia::class.java)
            startActivity(intent3)
        }

        // Obtención de los campos y botones
        val textEmail = findViewById<EditText>(R.id.textMailRemisero)
        val textPassword = findViewById<EditText>(R.id.textPasswordRemisero)
        val btnLogin = findViewById<Button>(R.id.btnLoginRemisero)

        btnLogin.setOnClickListener {
            val email = textEmail.text.toString().trim()
            val password = textPassword.text.toString().trim()

            // Validación de los campos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Llamamos a la función para iniciar sesión
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        // Iniciar sesión con email y contraseña
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Cerrar la actividad de login para que no regrese
                } else {
                    // Si falla el login, muestra un mensaje de error más detallado
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(this, "Error en el login: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
