package com.calipso.remiwaza

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ActivityBucarRemisero : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var searchField: EditText
    private lateinit var searchButton: Button
    private lateinit var resultText: TextView
    private lateinit var addButton: Button
    private lateinit var companyName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_remisero)

        database = FirebaseDatabase.getInstance().getReference("users") // Refiere a la tabla 'users'
        searchField = findViewById(R.id.seachRemisero)
        searchButton = findViewById(R.id.btnSearchUser)
        resultText = findViewById(R.id.textUserResult)
        addButton = findViewById(R.id.btnAddToAgencia)

        database = FirebaseDatabase.getInstance().getReference("users")  // Se asume que los usuarios están bajo "users"
        searchField = findViewById(R.id.seachRemisero)
        searchButton = findViewById(R.id.btnSearchUser)
        resultText = findViewById(R.id.textUserResult)
        addButton = findViewById(R.id.btnAddToAgencia)

        // Obtener el nombre de la agencia desde SharedPreferences
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        companyName = sharedPref.getString("companyName", "") ?: ""

        // Acción de búsqueda de usuario por correo
        searchButton.setOnClickListener {
            val email = searchField.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce un correo electrónico.", Toast.LENGTH_SHORT).show()
            } else {
                searchUserByEmail(email)
            }
        }

        // Acción de agregar el usuario a la agencia
        addButton.setOnClickListener {
            val userId = addButton.tag as? String
            if (userId != null) {
                addUserToCompany(userId)
            } else {
                Toast.makeText(this, "Error: No se puede agregar al usuario.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para buscar al remisero por su correo electrónico
    private fun searchUserByEmail(email: String) {
        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userId = userSnapshot.key  // Obtiene el ID del usuario
                        val userName = userSnapshot.child("name").getValue(String::class.java)
                        resultText.text = "Usuario encontrado: $userName"
                        addButton.tag = userId  // Asocia el ID del usuario con el botón
                        addButton.visibility = Button.VISIBLE  // Muestra el botón de agregar
                    }
                } else {
                    resultText.text = "No se encontró al usuario."
                    addButton.visibility = Button.GONE  // Oculta el botón si no se encuentra el usuario
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ActivityBucarRemisero, "Error en la búsqueda: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Método para agregar el remisero a la agencia
    private fun addUserToCompany(userId: String) {
        // Verificar que el nombre de la agencia esté disponible
        if (companyName.isNotEmpty()) {
            // Referencia a la nueva tabla donde se almacenan los remiseros de la agencia
            val companyDriverRef = FirebaseDatabase.getInstance().getReference("agency_drivers")
            val data = mapOf(
                "companyName" to companyName,
                "driverId" to userId  // Asociamos el ID del remisero con la agencia
            )

            // Guardar los datos en la base de datos bajo la tabla "agency_drivers"
            companyDriverRef.push().setValue(data)
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
}