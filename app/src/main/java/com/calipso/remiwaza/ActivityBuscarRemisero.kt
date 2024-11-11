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

        database = FirebaseDatabase.getInstance().getReference("users")
        searchField = findViewById(R.id.seachRemisero)
        searchButton = findViewById(R.id.btnSearchUser)
        resultText = findViewById(R.id.textUserResult)
        addButton = findViewById(R.id.btnAddToAgencia)

        // Obtener el nombre de la agencia desde SharedPreferences
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        companyName = sharedPref.getString("companyName", "") ?: ""

        searchButton.setOnClickListener {
            val email = searchField.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce un correo electrónico.", Toast.LENGTH_SHORT).show()
            } else {
                searchUserByEmail(email)
            }
        }

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
                        addButton.visibility = Button.VISIBLE
                    }
                } else {
                    resultText.text = "No se encontró al usuario."
                    addButton.visibility = Button.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ActivityBucarRemisero, "Error en la búsqueda: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addUserToCompany(userId: String) {
        // Usar el nombre de la agencia almacenado en SharedPreferences
        if (companyName.isNotEmpty()) {
            val companyDriverRef = FirebaseDatabase.getInstance().getReference("company_driver_$companyName")
            val data = mapOf(
                "companyName" to companyName,
                "driverId" to userId
            )
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
