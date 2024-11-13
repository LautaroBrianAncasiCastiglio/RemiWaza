package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ActivityInicioAgencia : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var empleadosAdapter: CustomAdapterEmpleados
    private lateinit var empleadosList: MutableList<ParametrosEmpleados>
    private lateinit var listViewEmpleados: RecyclerView
    private lateinit var textViewNoEmpleados: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_agencia)

        // Inicializamos las vistas
        listViewEmpleados = findViewById(R.id.listaEmpleados)
        listViewEmpleados.layoutManager = LinearLayoutManager(this)
        textViewNoEmpleados = findViewById(R.id.textViewNoEmpleados)

        // Inicializamos la lista de empleados
        empleadosList = mutableListOf()
        empleadosAdapter = CustomAdapterEmpleados(this, empleadosList)
        listViewEmpleados.adapter = empleadosAdapter

        // Inicializamos Firebase
        database = FirebaseDatabase.getInstance().getReference("companies")

        // Cargar empleados
        cargarEmpleados()
    }

    private fun cargarEmpleados() {
        // Obtener el nombre de la empresa desde las preferencias
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val companyName = sharedPref.getString("companyName", "")

        if (companyName.isNullOrEmpty()) {
            Log.e("ActivityInicioAgencia", "No tiene agencia asignada")
            textViewNoEmpleados.text = "No tiene agencia asignada"
            textViewNoEmpleados.visibility = View.VISIBLE
            listViewEmpleados.visibility = View.GONE
            return
        }

        // Referencia a la base de datos para esa compañía
        val companyRef = database.child(companyName).child("remiseros")

        // Leer los datos de los remiseros (empleados)
        companyRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                empleadosList.clear()
                for (data in snapshot.children) {
                    // Extraer los datos del remisero
                    val empleado = data.getValue(ParametrosEmpleados::class.java)
                    if (empleado != null) {
                        empleadosList.add(empleado)
                    }
                }
                empleadosAdapter.notifyDataSetChanged()

                // Si no hay empleados, mostrar mensaje
                if (empleadosList.isEmpty()) {
                    textViewNoEmpleados.text = "No hay empleados registrados."
                    textViewNoEmpleados.visibility = View.VISIBLE
                    listViewEmpleados.visibility = View.GONE
                } else {
                    textViewNoEmpleados.visibility = View.GONE
                    listViewEmpleados.visibility = View.VISIBLE
                }
            } else {
                // Si no hay remiseros
                textViewNoEmpleados.text = "No hay empleados registrados."
                textViewNoEmpleados.visibility = View.VISIBLE
                listViewEmpleados.visibility = View.GONE
            }
        }.addOnFailureListener { exception ->
            Log.e("ActivityInicioAgencia", "Error al cargar empleados: ${exception.message}")
        }
    }
}
