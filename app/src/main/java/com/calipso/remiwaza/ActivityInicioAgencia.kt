package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityInicioAgencia : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var customAdapter: CustomAdapterEmpleados
    private lateinit var dataList: ArrayList<ParametrosEmpleados>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_agencia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userButton: LinearLayout = findViewById(R.id.btnCount)
        userButton.setOnClickListener {
            val intent = Intent(this, ActivityPerfilAgencia::class.java)
            startActivity(intent)
        }
        val agenciaButton: LinearLayout = findViewById(R.id.btnAgencia)
        agenciaButton.setOnClickListener {
            val intent = Intent(this, ActivityInicioAgencia::class.java)
            startActivity(intent)
        }

        // Inicializa el RecyclerView
        recyclerView = findViewById(R.id.listaEmpleados)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear la lista de datos
        dataList = ArrayList()
        dataList.add(ParametrosEmpleados("Juan", "Castillo", "Disponible",true))
        dataList.add(ParametrosEmpleados("Manuel", "Rosas", "Disponible",true))
        dataList.add(ParametrosEmpleados("Martín", "Juares", "Disponible",true))
        dataList.add(ParametrosEmpleados("Ezequiel", "Lopez", "Disponible",true))
        dataList.add(ParametrosEmpleados("Pedro", "Castiglione", "Disponible",true))
        dataList.add(ParametrosEmpleados("Monica", "Paredes", "Disponible",true))
        dataList.add(ParametrosEmpleados("Marcelo", "Cuatrano", "No Disponible",false))
        dataList.add(ParametrosEmpleados("Luciano", "Tello", "No Disponible",false))
        dataList.add(ParametrosEmpleados("Brian", "Tuillier", "No Disponible",false))
        dataList.add(ParametrosEmpleados("Lautaro", "Ancasi", "No Disponible",false))


        // Configura el adaptador
        customAdapter = CustomAdapterEmpleados(dataList)
        recyclerView.adapter = customAdapter
    }
}