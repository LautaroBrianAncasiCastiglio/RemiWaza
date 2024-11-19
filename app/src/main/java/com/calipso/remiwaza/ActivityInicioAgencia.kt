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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ActivityInicioAgencia : AppCompatActivity() {

    private lateinit var listaEmpleados: RecyclerView
    private lateinit var textViewNoEmpleados: TextView
    private lateinit var db: FirebaseDatabase
    private lateinit var agencyRef: DatabaseReference
    private lateinit var employeesList: MutableList<ParametrosEmpleados>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_agencia)

        listaEmpleados = findViewById(R.id.listaEmpleados)
        textViewNoEmpleados = findViewById(R.id.textViewNoEmpleados)

        db = FirebaseDatabase.getInstance()
        agencyRef = db.getReference("companies")

        listaEmpleados.layoutManager = LinearLayoutManager(this)

        val currentAgencyName = "nombreDeLaAgencia" // Sustituir con el nombre real de la agencia

        // Obtener los remiseros disponibles
        agencyRef.child(currentAgencyName).child("remiseros").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                employeesList.clear()
                for (empleadoSnapshot in snapshot.children) {
                    val empleado = empleadoSnapshot.getValue(ParametrosEmpleados::class.java)
                    if (empleado != null && empleado.state == "disponible") { // Solo agregar si está disponible
                        employeesList.add(empleado)
                    }
                }
                // Actualizar la lista en el RecyclerView
                val adapter = CustomAdapterEmpleados(this@ActivityInicioAgencia, employeesList)
                listaEmpleados.adapter = adapter

                // Mostrar o ocultar el mensaje de "No empleados" según corresponda
                if (employeesList.isEmpty()) {
                    textViewNoEmpleados.visibility = View.VISIBLE
                    listaEmpleados.visibility = View.GONE
                } else {
                    textViewNoEmpleados.visibility = View.GONE
                    listaEmpleados.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ActivityInicioAgencia", "Error al obtener los datos de Firebase", error.toException())
            }
        })
    }
}
