package com.calipso.remiwaza

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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.util.Patterns

class ActivityRegistroRemisero : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_remisero)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonAgencia = findViewById<Button>(R.id.btnAgencia)

        buttonLogin.setOnClickListener {
            val intent2 = Intent(applicationContext, ActivityLoginRemisero::class.java)
            startActivity(intent2)
        }

        buttonAgencia.setOnClickListener {
            val intent3 = Intent(applicationContext, ActivityRegistroAgencia::class.java)
            startActivity(intent3)
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        val textName = findViewById<EditText>(R.id.textNameRemisero)
        val textLastName = findViewById<EditText>(R.id.textLastNameRemisero)
        val textMail = findViewById<EditText>(R.id.textMailRemisero)
        val textPassword = findViewById<EditText>(R.id.textPasswordRemisero)
        val btnRegistro = findViewById<Button>(R.id.btnRegistroRemisero)

        btnRegistro.setOnClickListener {
            val name = textName.text.toString().trim()
            val lastName = textLastName.text.toString().trim()
            val email = textMail.text.toString().trim()
            val password = textPassword.text.toString().trim()

            // Validar los campos
            if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Validar el formato del correo electrónico
                Toast.makeText(this, "El correo electrónico no es válido.", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(name, lastName, email, password)
            }
        }
    }

    private fun registerUser(name: String, lastName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userData = mapOf(
                        "name" to name,
                        "lastName" to lastName,
                        "email" to email
                    )

                    userId?.let {
                        database.child(it).setValue(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
