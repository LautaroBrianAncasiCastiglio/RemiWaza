package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ActivityAgregarAuto : AppCompatActivity() {
    private lateinit var textMarca: EditText
    private lateinit var textModelo: EditText
    private lateinit var textColor: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_auto)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicializar vistas
        textMarca = findViewById(R.id.textMarca)
        textModelo = findViewById(R.id.textModelo)
        textColor = findViewById(R.id.textColor)
        btnRegistrar = findViewById(R.id.btnRegistroAgencia)

        // Configuración de botones de navegación
        val userButton: LinearLayout = findViewById(R.id.btnCountAgenci)
        userButton.setOnClickListener {
            startActivity(Intent(this, ActivityPerfilAgencia::class.java))
        }
        val agenciaButton: LinearLayout = findViewById(R.id.btnEmpleados)
        agenciaButton.setOnClickListener {
            startActivity(Intent(this, ActivityInicioAgencia::class.java))
        }

        // Obtener el ID de la agencia y configurar el botón de registro
        obtenerNombreDeLaAgencia { agencyId ->
            if (!agencyId.isNullOrEmpty()) {
                btnRegistrar.setOnClickListener {
                    registrarAuto(agencyId)
                }
            } else {
                Toast.makeText(this, "Error al obtener el ID de la agencia", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarAuto(agencyId: String) {
        val marca = textMarca.text.toString().trim()
        val modelo = textModelo.text.toString().trim()
        val color = textColor.text.toString().trim()
        val state = "available"

        if (marca.isNotEmpty() && modelo.isNotEmpty() && color.isNotEmpty()) {
            val companyCarsRef = FirebaseDatabase.getInstance().getReference("companies/$agencyId/cars")
            val autoId = companyCarsRef.push().key

            if (autoId != null) {
                val auto = Auto(marca, modelo, color, state)
                companyCarsRef.child(autoId).setValue(auto).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Auto registrado exitosamente", Toast.LENGTH_SHORT).show()
                        clearFields()
                    } else {
                        Toast.makeText(this, "Error al registrar el auto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerNombreDeLaAgencia(callback: (String?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("owners").child(userId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val agencyId = snapshot.child("name").getValue(String::class.java)
                    callback(agencyId)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                    Toast.makeText(this@ActivityAgregarAuto, "Error al obtener el ID de la agencia: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            callback(null)
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        textMarca.text.clear()
        textModelo.text.clear()
        textColor.text.clear()
    }
}

data class Auto(val marca: String = "", val modelo: String = "", val color: String = "", val state: String = "")
