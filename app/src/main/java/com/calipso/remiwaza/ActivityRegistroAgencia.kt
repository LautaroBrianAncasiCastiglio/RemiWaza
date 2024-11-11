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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonRemisero = findViewById<Button>(R.id.btnRemisero)

        buttonLogin.setOnClickListener{
            val intent2= Intent(applicationContext, ActivityLoginAgencia::class.java)
            startActivity(intent2)
        }
        buttonRemisero.setOnClickListener{
            val intent3= Intent(applicationContext, ActivityRegistroRemisero::class.java)
            startActivity(intent3)
        }


        auth = FirebaseAuth.getInstance()
        // Referencia a la tabla 'agencies' en Firebase
        var database = FirebaseDatabase.getInstance().getReference("agencias")

        val textName = findViewById<EditText>(R.id.textNameAgencia)
        val textMail = findViewById<EditText>(R.id.textMailAgencia)
        val textPassword = findViewById<EditText>(R.id.textPasswordAgencia)
        val btnRegistro = findViewById<Button>(R.id.btnRegistroAgencia)

        btnRegistro.setOnClickListener {
            val name = textName.text.toString().trim()
            val email = textMail.text.toString().trim()
            val password = textPassword.text.toString().trim()

            // Validación de los campos
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Validar el formato del correo electrónico
                Toast.makeText(this, "El correo electrónico no es válido.", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar a la función de registro
                registerAgency(name, email, password)
            }
        }
    }

    private fun registerAgency(name: String, email: String, password: String) {
        // Crear el usuario con email y contraseña
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val agencyData = mapOf(
                        "name" to name,
                        "email" to email
                    )

                    userId?.let {
                        // Guardar los datos de la agencia en Firebase en la tabla 'agencies'
                        database.child(it).setValue(agencyData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Agencia registrada con éxito.", Toast.LENGTH_SHORT).show()
                                val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
                                val editor = sharedPref.edit()
                                editor.putString("name", name) // Guarda el nombre de la agencia
                                editor.apply()
                                // Redirigir a la pantalla de login o inicio
                                val intent = Intent(this, ActivityInicioAgencia::class.java)
                                startActivity(intent)
                                finish() // Finaliza la actividad de registro para que no vuelva atrás
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Si el registro falla
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}