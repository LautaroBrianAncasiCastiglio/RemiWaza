package com.calipso.remiwaza

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityBuscarRemisero : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var searchField: EditText
    private lateinit var searchButton: Button
    private lateinit var resultText: TextView
    private lateinit var addButton: Button
    private lateinit var companyName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_remisero)

        // Inicializar las vistas
        searchField = findViewById(R.id.seachRemisero)
        searchButton = findViewById(R.id.btnSearchUser)
        resultText = findViewById(R.id.textUserResult)
        addButton = findViewById(R.id.btnAddToAgencia)

        // Inicializar la referencia de la base de datos
        database = FirebaseDatabase.getInstance().getReference("drivers")

        // Obtener el nombre de la agencia (puedes cambiar la forma de obtener este dato según tu lógica)
        companyName = "owners"

        // Configurar la acción del botón de búsqueda
        searchButton.setOnClickListener {
            val email = searchField.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce un correo electrónico.", Toast.LENGTH_SHORT).show()
            } else {
                searchUserByEmail(email)
            }
        }

        // Configurar la acción del botón de agregar
        addButton.setOnClickListener {
            val userId = addButton.tag as? String
            if (userId != null) {
                addUserToCompany(userId)
            } else {
                Toast.makeText(this, "Error: No se puede agregar al usuario.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchUserByEmail(email: String) {
        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userId = userSnapshot.key
                        val userName = userSnapshot.child("name").getValue(String::class.java)
                        resultText.text = "Usuario encontrado: $userName"
                        addButton.tag = userId
                    }
                } else {
                    resultText.text = "No se encontró al usuario."
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ActivityBuscarRemisero, "Error en la búsqueda: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addUserToCompany(userId: String, name: String, lastName: String, state: String) {
        val currentAgencyName = obtenerNombreDeLaAgencia() // Obtiene el nombre de la agencia
        if (currentAgencyName.isNotEmpty()) {
            val companyDriverRef = FirebaseDatabase.getInstance().getReference("companies/$currentAgencyName/remiseros")
            val data = mapOf(
                "driverId" to userId,
                "name" to name,
                "lastName" to lastName,
                "state" to state
            )
            companyDriverRef.child(userId).setValue(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Usuario agregado a la agencia.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al agregar al usuario: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "No se pudo obtener el nombre de la agencia.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun obtenerNombreDeLaAgencia(): String {
        // Simulación de obtención de nombre de la agencia desde Firebase
        var nombreAgencia = ""
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("owners").child(userId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    nombreAgencia = snapshot.child("name").getValue(String::class.java) ?: ""
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ActivityBuscarRemisero, "Error al obtener el nombre de la agencia: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return nombreAgencia
    }

}
