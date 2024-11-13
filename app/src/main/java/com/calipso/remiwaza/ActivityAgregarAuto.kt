package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ActivityAgregarAuto : AppCompatActivity() {
    private lateinit var textMarca: EditText
    private lateinit var textModelo: EditText
    private lateinit var textColor: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_auto)

        val userButton: LinearLayout = findViewById(R.id.btnCountAgenci)
        userButton.setOnClickListener {
            val intent = Intent(this, ActivityPerfilAgencia::class.java)
            startActivity(intent)
        }
        val agenciaButton: LinearLayout = findViewById(R.id.btnEmpleados)
        agenciaButton.setOnClickListener {
            val intent = Intent(this, ActivityInicioAgencia::class.java)
            startActivity(intent)
        }

        // Inicializar vistas
        textMarca = findViewById(R.id.textMarca)
        textModelo = findViewById(R.id.textModelo)
        textColor = findViewById(R.id.textColor)
        btnRegistrar = findViewById(R.id.btnRegistroAgencia)

        // Inicializar la referencia a la base de datos de Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("cars")

        // Configurar el botón de registro
        btnRegistrar.setOnClickListener {
            val marca = textMarca.text.toString().trim()
            val modelo = textModelo.text.toString().trim()
            val color = textColor.text.toString().trim()
            val state = "available"

            if (marca.isNotEmpty() && modelo.isNotEmpty() && color.isNotEmpty()) {
                // Generar un ID único para el auto
                val autoId = database.push().key

                // Crear un objeto Auto
                val auto = Auto(marca, modelo, color, state)

                if (autoId != null) {
                    // Guardar el auto en Firebase
                    database.child(autoId).setValue(auto).addOnCompleteListener { task ->
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
    }

    // Función para limpiar los campos después del registro
    private fun clearFields() {
        textMarca.text.clear()
        textModelo.text.clear()
        textColor.text.clear()
    }
}

// Clase de datos para representar un auto
data class Auto(val marca: String = "", val modelo: String = "", val color: String = "", val state:String = "")
