package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ActivityCarRemisero : AppCompatActivity() {

    private lateinit var listaAutos: RecyclerView
    private lateinit var textViewNoAutos: TextView
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_remisero)

        val button = findViewById<Button>(R.id.btnpersonalCar)
        button.setOnClickListener {
            val intent1 = Intent(applicationContext, ActivityStateRemisero::class.java)
            startActivity(intent1)
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

        listaAutos = findViewById(R.id.listCar)
        textViewNoAutos = findViewById(R.id.textViewNoCars)

        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

// Inicializa tu RecyclerView
        listaAutos.layoutManager = LinearLayoutManager(this)

// Llama a obtenerNombreDeLaAgencia para saber la agencia del remisero
        obtenerNombreDeLaAgencia { currentAgencyName ->
            if (!currentAgencyName.isNullOrEmpty()) {
                cargarAutosDeRemisero(currentAgencyName)
            } else {
                Toast.makeText(this, "No se pudo obtener la agencia del remisero.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarAutosDeRemisero(agencyName: String) {
        // Obtiene los datos de los autos asociados a la agencia
        val autosRef = db.getReference("companies/$agencyName/cars")

        autosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val autosList = mutableListOf<ParametrosAutos>()

                // Iteramos sobre los autos y extraemos los datos
                for (autoSnapshot in snapshot.children) {
                    val auto = autoSnapshot.getValue(ParametrosAutos::class.java)
                    if (auto != null) {
                        autosList.add(auto)
                        Log.d("ActivityAutosRemisero", "Auto encontrado: ${auto.modelo} ${auto.marca}")
                    } else {
                        Log.d("ActivityAutosRemisero", "Auto no válido encontrado en snapshot: ${autoSnapshot.key}")
                    }
                }

                // Ordenamos la lista: primero los "available", luego los demás
                autosList.sortByDescending { it.state.lowercase() == "available" }

                // Si no hay autos, mostramos el mensaje correspondiente
                if (autosList.isNotEmpty()) {
                    val adapter = CustomAdapterAutoRemisero(this@ActivityCarRemisero, autosList)
                    listaAutos.adapter = adapter
                    listaAutos.visibility = View.VISIBLE
                    textViewNoAutos.visibility = View.GONE
                    Log.d("ActivityAutosRemisero", "Número de autos cargados: ${autosList.size}")
                } else {
                    listaAutos.visibility = View.GONE
                    textViewNoAutos.visibility = View.VISIBLE
                    Log.d("ActivityAutosRemisero", "Número de autos cargados: ${autosList.size}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ActivityAutosRemisero", "Error al obtener los datos de Firebase", error.toException())
            }
        })
    }

    private fun obtenerNombreDeLaAgencia(callback: (String?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val databaseRef = db.getReference("remiseros").child(userId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val agencyName = snapshot.child("agencia").getValue(String::class.java)
                    callback(agencyName)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                    Toast.makeText(
                        this@ActivityCarRemisero,
                        "Error al obtener la agencia del remisero: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            callback(null)
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}