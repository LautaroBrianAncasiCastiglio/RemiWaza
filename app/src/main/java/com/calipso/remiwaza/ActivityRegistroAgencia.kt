package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ActivityRegistroAgencia : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_agencia)

        // Ajuste de las barras de sistema (Edge to Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("owners") // Referencia a la tabla 'companies' en Firebase

        // Referencias a las vistas
        val textName = findViewById<EditText>(R.id.textNameAgencia)
        val textMail = findViewById<EditText>(R.id.textMailAgencia)
        val textPassword = findViewById<EditText>(R.id.textPasswordAgencia)
        val btnRegistro = findViewById<Button>(R.id.btnRegistroAgencia)
        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonRemisero = findViewById<Button>(R.id.btnRemisero)

        // Navegar a la pantalla de Login o de Registro Remisero
        buttonLogin.setOnClickListener{
            val intent2 = Intent(applicationContext, ActivityLoginAgencia::class.java)
            startActivity(intent2)
        }

        buttonRemisero.setOnClickListener{
            val intent3 = Intent(applicationContext, ActivityRegistroRemisero::class.java)
            startActivity(intent3)
        }

        // Registrar agencia
        btnRegistro.setOnClickListener {
            val name = textName.text.toString().trim()
            val email = textMail.text.toString().trim()
            val password = textPassword.text.toString().trim()

            // Validaciones
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "El correo electrónico no es válido.", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
            } else {
                // Registrar la agencia
                registerAgency(name, email, password)
            }
        }
    }

    // Función para registrar la agencia
    private fun registerAgency(name: String, email: String, password: String) {
        val intent = Intent(this, ActivityInicioAgencia::class.java)

        // Crear el usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val agencyData = mapOf(
                        "name" to name,
                        "email" to email,
                        "password" to password,
                        "type" to "agencia"
                    )

                    userId?.let {
                        // Guardar los datos de la agencia en Firebase Realtime Database
                        database.child(it).setValue(agencyData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Agencia registrada con éxito.", Toast.LENGTH_SHORT).show()
                                // Redirigir al inicio de la agencia
                                startActivity(intent)
                                finish() // Finaliza la actividad de registro
                            }
                            .addOnFailureListener { e ->
                                // Manejar error al guardar los datos en la base de datos
                                Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Manejar error en la creación del usuario
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
