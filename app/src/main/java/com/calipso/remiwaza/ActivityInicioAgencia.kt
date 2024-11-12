package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ActivityInicioAgencia : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var listViewEmpleados: ListView
    private lateinit var empleadosAdapter: RemiseroAdapter
    private lateinit var empleadosList: MutableList<Remisero>
    private lateinit var btnAgregarEmpleados: Button
    private lateinit var textViewEmpleados: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_agencia)

        // Inicializar vistas
        listViewEmpleados = findViewById(R.id.listaEmpleados)
        btnAgregarEmpleados = findViewById(R.id.btnAgregarEmpleados)
        textViewEmpleados = findViewById(R.id.textView7)

        // Configuración de la lista y el adaptador
        empleadosList = mutableListOf()
        empleadosAdapter = RemiseroAdapter(this, empleadosList)
        listViewEmpleados.adapter = empleadosAdapter

        // Inicializar referencia de base de datos
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val companyName = sharedPref.getString("companyName", "")
        if (companyName.isNullOrEmpty()) {
            Toast.makeText(this, "Error al obtener la empresa", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            database = FirebaseDatabase.getInstance().getReference("agency_drivers").child(companyName)
            cargarEmpleados()
        }

        // Configurar el botón de agregar empleados
        btnAgregarEmpleados.setOnClickListener {
            startActivity(Intent(this, ActivityBucarRemisero::class.java))
        }
    }

    private fun cargarEmpleados() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empleadosList.clear()
                for (data in snapshot.children) {
                    val remisero = data.getValue(Remisero::class.java)
                    if (remisero != null) {
                        empleadosList.add(remisero)
                    }
                }
                empleadosAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ActivityInicioAgencia, "Error al cargar empleados: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
