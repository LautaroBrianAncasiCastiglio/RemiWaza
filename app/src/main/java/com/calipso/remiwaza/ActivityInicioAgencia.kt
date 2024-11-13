package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityInicioAgencia : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var customAdapter: CustomAdapterEmpleados
    private lateinit var dataList: ArrayList<ParametrosEmpleados>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_agencia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.listaEmpleados)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa la lista y el adaptador
        recyclerView = findViewById(R.id.listaEmpleados)
        recyclerView.layoutManager = LinearLayoutManager(this)
        dataList = ArrayList()
        customAdapter = CustomAdapterEmpleados(dataList)
        recyclerView.adapter = customAdapter
        // Inicializar la referencia a la base de datos
        database = FirebaseDatabase.getInstance()
            .getReference("companies/owners")  // Ajusta el path según tu estructura

        // Cargar datos desde Firebase
        loadRemiserosData()

        // Configuración de botones
        val button = findViewById<Button>(R.id.btnAgregarEmpleados)
        button.setOnClickListener {
            val intent1 = Intent(applicationContext, ActivityBuscarRemisero::class.java)
            startActivity(intent1)
        }
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
    }

    private fun loadRemiserosData() {
        obtenerNombreDeLaAgencia { agencyId ->
            if (!agencyId.isNullOrEmpty()) {
                // Referencia a los remiseros de la empresa
                val companyRemiserosRef =
                    FirebaseDatabase.getInstance().getReference("companies/$agencyId/remiseros")

                // Añadir un listener para escuchar los cambios
                companyRemiserosRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val availableList = mutableListOf<ParametrosEmpleados>()
                        val unavailableList = mutableListOf<ParametrosEmpleados>()

                        // Iterar sobre los datos y separarlos según el estado
                        for (dataSnapshot in snapshot.children) {
                            val remisero = dataSnapshot.getValue(ParametrosEmpleados::class.java)
                            if (remisero != null) {
                                // Filtrar según el estado del remisero
                                if (remisero.state == "available") {
                                    availableList.add(remisero)
                                } else {
                                    unavailableList.add(remisero)
                                }
                            }
                        }

                        // Combinar las listas: primero los disponibles, luego los no disponibles
                        val sortedList = availableList + unavailableList

                        // Actualizar el adaptador con la lista combinada
                        customAdapter.updateData(sortedList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@ActivityInicioAgencia,
                            "Error al cargar los datos: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(
                    this@ActivityInicioAgencia,
                    "Error: No se pudo obtener el nombre de la agencia.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun obtenerNombreDeLaAgencia(callback: (String?) -> Unit) {
        // Obtener el uid del usuario autenticado
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Referencia a la base de datos donde tienes los datos de los usuarios
            val databaseRef = FirebaseDatabase.getInstance().getReference("owners").child(userId)

            // Escuchar el evento de obtener los datos
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Obtener el agencyId asociado a este usuario
                    val agencyId = snapshot.child("agencyId").getValue(String::class.java)

                    // Llamar al callback con el agencyId
                    callback(agencyId)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejo de errores
                    Toast.makeText(
                        this@ActivityInicioAgencia,
                        "Error al obtener los datos: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(null)
                }
            })
        } else {
            callback(null)
        }
    }
}

