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

class ActivityModificarAgencia : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var textName: EditText
    private lateinit var textMail: EditText
    private lateinit var textPassword: EditText
    private lateinit var btnCancelar: Button
    private lateinit var btnReturn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modificar_remisero)  // Reemplaza por el XML adecuado si es necesario

        // Configuración de padding para pantallas con borde
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de botones de navegación
        val userButton: LinearLayout = findViewById(R.id.btnCountAgenci)  // Puede que necesites cambiar este botón
        userButton.setOnClickListener {
            val intent = Intent(this, ActivityPerfilAgencia::class.java)  // Cambiar la actividad correspondiente
            startActivity(intent)
        }

        val agenciaButton: LinearLayout = findViewById(R.id.btnEmpleados)  // Cambiar según el contexto de la agencia
        agenciaButton.setOnClickListener {
            val intent = Intent(this, ActivityInicioAgencia::class.java)  // Cambiar a la actividad de la agencia
            startActivity(intent)
        }

        // Inicialización de Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("agencies")  // Aquí cambiamos a "agencies"

        // Referencias a las vistas de la interfaz
        textName = findViewById(R.id.textNameModifi)  // O cambia el ID según el XML
        textMail = findViewById(R.id.textMailModifi)
        textPassword = findViewById(R.id.textPasswordModifi)
        btnCancelar = findViewById(R.id.btnCancelarModificación)
        btnReturn = findViewById(R.id.btnReturn)

        // Cargar los datos de la agencia
        loadAgencyData()

        // Acción al cancelar la modificación
        btnCancelar.setOnClickListener {
            finish() // Cierra la actividad
        }

        // Acción al regresar y guardar los cambios
        btnReturn.setOnClickListener {
            updateAgencyData()
        }
    }

    // Cargar los datos de la agencia desde Firebase
    private fun loadAgencyData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Rellenar los campos con los datos de la agencia
                        textName.setText(snapshot.child("name").getValue(String::class.java))
                        textMail.setText(snapshot.child("email").getValue(String::class.java))
                        textPassword.setText(snapshot.child("password").getValue(String::class.java))
                    } else {
                        Toast.makeText(this@ActivityModificarAgencia, "No se encontraron datos de la agencia.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ActivityModificarAgencia, "Error al cargar los datos: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para actualizar los datos de la agencia en Firebase
    private fun updateAgencyData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            // Recoger los datos modificados
            val updatedData = mapOf(
                "name" to textName.text.toString().trim(),
                "email" to textMail.text.toString().trim(),
                "password" to textPassword.text.toString().trim()
            )

            // Actualizar los datos en Firebase
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
