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

        val button = findViewById<Button>(R.id.btnAgregarEmpleados)
        button.setOnClickListener {
            val intent1 = Intent(applicationContext, ActivityBuscarEmpleado::class.java)
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
        val autoButton: LinearLayout = findViewById(R.id.btnCarro)
        autoButton.setOnClickListener {
            val intent = Intent(this, ActivityAgregarAuto::class.java)
            startActivity(intent)
        }

        listaEmpleados = findViewById(R.id.listaEmpleados)
        textViewNoEmpleados = findViewById(R.id.textViewNoEmpleados)

        db = FirebaseDatabase.getInstance()
        agencyRef = db.getReference("companies") // Ruta base de las empresas

        // Inicializa tu RecyclerView
        listaEmpleados.layoutManager = LinearLayoutManager(this)

        // Obtener el nombre de la agencia actual (puede venir de un login o ser fijo por ahora)
        val currentAgencyName = "nombreDeLaAgencia" // Sustituir con el nombre real de la agencia

        // Obtenemos los datos de los remiseros dentro de la agencia
        agencyRef.child(currentAgencyName).child("remiseros").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val empleadosList = mutableListOf<ParametrosEmpleados>()

                // Iteramos sobre los remiseros y extraemos los datos
                for (empleadoSnapshot in snapshot.children) {
                    val empleado = empleadoSnapshot.getValue(ParametrosEmpleados::class.java)
                    if (empleado != null) {
                        empleadosList.add(empleado)
                    }
                }

                // Asignamos el adaptador al RecyclerView
                val adapter = CustomAdapterEmpleados(this@ActivityInicioAgencia, empleadosList)
                listaEmpleados.adapter = adapter

                // Si no hay empleados, mostramos el mensaje correspondiente
                if (empleadosList.isEmpty()) {
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
