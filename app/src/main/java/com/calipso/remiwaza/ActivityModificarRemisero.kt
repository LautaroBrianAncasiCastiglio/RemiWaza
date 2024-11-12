package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityModificarRemisero : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var textName: EditText
    private lateinit var textLastName: EditText
    private lateinit var textMail: EditText
    private lateinit var textPassword: EditText
    private lateinit var btnCancelar: Button
    private lateinit var btnReturn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modificar_remisero)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userButton: LinearLayout = findViewById(R.id.btnCountRemisero)
        userButton.setOnClickListener {
            val intent = Intent(this, ActivityPerfilRemisero::class.java)
            startActivity(intent)
        }
        val agenciaButton: LinearLayout = findViewById(R.id.btnAgenciaRemisero)
        agenciaButton.setOnClickListener {
            val intent = Intent(this, ActivityInicioRemisero::class.java)
            startActivity(intent)
        }

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("drivers")

        // Referencias a las vistas
        textName = findViewById(R.id.textNameModifi)
        textLastName = findViewById(R.id.textLastNameModifi)
        textMail = findViewById(R.id.textMailModifi)
        textPassword = findViewById(R.id.textPasswordModifi)
        btnCancelar = findViewById(R.id.btnCancelarModificación)
        btnReturn = findViewById(R.id.btnReturn)

        // Cargar los datos del usuario
        loadUserData()

        // Botón de cancelar (puedes personalizar la funcionalidad)
        btnCancelar.setOnClickListener {
            finish() // Cierra la actividad
        }

        // Botón de regresar (puedes personalizar la funcionalidad)
        btnReturn.setOnClickListener {
            updateUserData()
        }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        textName.setText(snapshot.child("name").getValue(String::class.java))
                        textLastName.setText(snapshot.child("lastName").getValue(String::class.java))
                        textMail.setText(snapshot.child("email").getValue(String::class.java))
                        textPassword.setText(snapshot.child("password").getValue(String::class.java))
                    } else {
                        Toast.makeText(this@ActivityModificarRemisero, "No se encontraron datos del usuario.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ActivityModificarRemisero, "Error al cargar los datos: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val updatedData = mapOf(
                "name" to textName.text.toString().trim(),
                "lastName" to textLastName.text.toString().trim(),
                "email" to textMail.text.toString().trim(),
                "password" to textPassword.text.toString().trim()
            )

            database.child(userId).updateChildren(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        }

    }

}