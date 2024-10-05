package com.calipso.remiwaza

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityInicioAgencia : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var customAdapter: CustomAdapterEmpleados
    private lateinit var dataList: ArrayList<ListEmpleados>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_agencia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inicializa el RecyclerView
        recyclerView = findViewById(R.id.listaEmpleados)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear la lista de datos
        dataList = ArrayList()
        dataList.add(ListEmpleados("Juan", "Castillo", "Disponible",true))
        dataList.add(ListEmpleados("Manuel", "Rosas", "Disponible",true))
        dataList.add(ListEmpleados("Martín", "Juares", "Disponible",true))
        dataList.add(ListEmpleados("Ezequiel", "Lopez", "Disponible",true))
        dataList.add(ListEmpleados("Pedro", "Castiglione", "Disponible",true))
        dataList.add(ListEmpleados("Monica", "Paredes", "Disponible",true))
        dataList.add(ListEmpleados("Marcelo", "Cuatrano", "No Disponible",false))
        dataList.add(ListEmpleados("Luciano", "Tello", "No Disponible",false))
        dataList.add(ListEmpleados("Brian", "Tuillier", "No Disponible",false))
        dataList.add(ListEmpleados("Lautaro", "Ancasi", "No Disponible",false))


        // Configura el adaptador
        customAdapter = CustomAdapterEmpleados(dataList)
        recyclerView.adapter = customAdapter
    }
}