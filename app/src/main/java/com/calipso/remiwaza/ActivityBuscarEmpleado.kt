package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityBuscarEmpleado : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var searchField: EditText
    private lateinit var searchButton: Button
    private lateinit var resultText: TextView
    private lateinit var addButton: Button
    private lateinit var companyName: String
    private lateinit var auth: FirebaseAuth

    private var userName: String = ""
    private var userLastName: String = ""
    private var userState: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_remisero)

        // Inicializar las vistas
        searchField = findViewById(R.id.seachRemisero)
        searchButton = findViewById(R.id.btnSearchUser)
        resultText = findViewById(R.id.textUserResult)
        addButton = findViewById(R.id.btnAddToAgencia)

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
        val autoButton: LinearLayout = findViewById(R.id.btnCarro)
        autoButton.setOnClickListener {
            val intent = Intent(this, ActivityAgregarAuto::class.java)
            startActivity(intent)
        }

        // Inicializar la referencia de la base de datos
        database = FirebaseDatabase.getInstance().getReference("drivers")

        // Obtener el nombre de la agencia (puedes cambiar la forma de obtener este dato según tu lógica)
        companyName = "owners"

        // Configurar la acción del botón de búsqueda
        searchButton.setOnClickListener {
            val email = searchField.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(
                    this,
                    "Por favor, introduce un correo electrónico.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                searchUserByEmail(email)
            }
        }

        // Configurar la acción del botón de agregar
        addButton.setOnClickListener {
            val userId = addButton.tag as? String
            if (userId != null) {
                addUserToCompany(userId, userName, userLastName, userState)
            } else {
                Toast.makeText(this, "Error: No se puede agregar al usuario.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun searchUserByEmail(email: String) {
        database.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userId = userSnapshot.key
                            //val userName = userSnapshot.child("name").getValue(String::class.java)
                            userName = userSnapshot.child("name").getValue(String::class.java) ?: ""
                            userLastName =
                                userSnapshot.child("lastName").getValue(String::class.java) ?: ""
                            userState =
                                userSnapshot.child("state").getValue(String::class.java) ?: ""
                            resultText.text = "Usuario encontrado: $userName"
                            addButton.tag = userId
                        }
                    } else {
                        resultText.text = "No se encontró al usuario."
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@ActivityBuscarEmpleado,
                        "Error en la búsqueda: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun addUserToCompany(userId: String, name: String, lastName: String, state: String) {
        obtenerNombreDeLaAgencia { currentAgencyName ->
            if (currentAgencyName.isNotEmpty()) {
                val database = FirebaseDatabase.getInstance()

                // Referencia a la tabla de la agencia
                val companyDriverRef = database.getReference("companies/$currentAgencyName/remiseros")

                // Referencia al remisero en la tabla principal de remiseros
                val remiseroRef = database.getReference("drivers/$userId")

                // Datos del remisero que se guardarán en la agencia
                val data = mapOf(
                    "driverId" to userId,
                    "name" to name,
                    "lastName" to lastName,
                    "state" to state
                )

                // Datos adicionales para actualizar en la tabla principal del remisero
                val remiseroData = mapOf(
                    "agencia" to currentAgencyName
                )

                // Guardar el remisero en la agencia
                companyDriverRef.child(userId).setValue(data)
                    .addOnSuccessListener {
                        // Actualizar el campo "agencia" en la tabla principal de remiseros
                        remiseroRef.updateChildren(remiseroData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Usuario agregado a la agencia y actualizado.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al actualizar el campo agencia: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al agregar al usuario: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "No se pudo obtener el nombre de la agencia.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerNombreDeLaAgencia(callback: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("owners").child(userId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombreAgencia = snapshot.child("name").getValue(String::class.java) ?: ""
                    callback(nombreAgencia)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ActivityBuscarEmpleado, "Error al obtener el nombre de la agencia: ${error.message}", Toast.LENGTH_SHORT).show()
                    callback("") // Llamada al callback con un valor vacío en caso de error
                }
            })
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            callback("") // Llamada al callback con un valor vacío si no hay un usuario autenticado
        }
    }
    private fun asignarConductorAlAuto(conductor: String) {
        val db = FirebaseDatabase.getInstance().reference
        val autoId = intent.getStringExtra("carId") // Obtener el ID del auto (debe pasarse como extra)

        if (autoId != null) {
            val autoRef = db.child("companies").child("agencia_name").child("cars").child(autoId)

            // Actualizamos el campo 'conductor' en la base de datos
            autoRef.child("conductor").setValue(conductor).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Conductor asignado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al asignar conductor", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

