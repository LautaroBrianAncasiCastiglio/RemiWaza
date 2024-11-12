package com.calipso.remiwaza

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ActivityBuscarRemisero : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var searchField: EditText
    private lateinit var searchButton: Button
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var companyName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_remisero)

        database = FirebaseDatabase.getInstance().getReference("users")
        searchField = findViewById(R.id.searchRemisero)
        searchButton = findViewById(R.id.btnSearchUser)
        resultRecyclerView = findViewById(R.id.recyclerUserResults)
        addButton = findViewById(R.id.btnAddToAgencia)

        // Configuración del RecyclerView
        resultRecyclerView.layoutManager = LinearLayoutManager(this)
        
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
                    val usersList = mutableListOf<User>()
                    for (userSnapshot in snapshot.children) {
                        val userId = userSnapshot.key
                        val userName = userSnapshot.child("name").getValue(String::class.java) ?: ""
                        usersList.add(User(userId, userName, email)) // Clase 'User' para almacenar datos
                    }
                    resultRecyclerView.adapter = UserAdapter(usersList) // Asigna los resultados al adaptador
                    addButton.tag = usersList.firstOrNull()?.userId // Asocia el ID con el botón si hay resultados
                    addButton.visibility = if (usersList.isNotEmpty()) Button.VISIBLE else Button.GONE
                } else {
                    Toast.makeText(this@ActivityBuscarRemisero, "No se encontró al usuario.", Toast.LENGTH_SHORT).show()
                    addButton.visibility = Button.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ActivityBuscarRemisero, "Error en la búsqueda: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addUserToCompany(userId: String) {
        if (companyName.isNotEmpty()) {
            val companyDriverRef = FirebaseDatabase.getInstance().getReference("agency_drivers")
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

// Clase 'User' para almacenar datos del usuario
data class User(val userId: String?, val name: String, val email: String)

// Adaptador para el RecyclerView (simplificado para visualización)
class UserAdapter(private val usersList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    // ViewHolder y otros métodos aquí para manejar los elementos de la lista...
}
