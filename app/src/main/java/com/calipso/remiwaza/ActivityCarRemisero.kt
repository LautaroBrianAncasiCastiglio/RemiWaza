package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityCarRemisero : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var customAdapter: MyAdapter
    private lateinit var dataList: ArrayList<ListaAutos>
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
        dataList.add(ListaAutos("Título 1", "Título 1", "Subtítulo 1",true))
        dataList.add(ListaAutos("Título 1", "Título 2", "Subtítulo 2",true))
        dataList.add(ListaAutos("Título 1", "Título 3", "Subtítulo 3",false))

        // Configura el adaptador
        customAdapter = MyAdapter(dataList)
        recyclerView.adapter = customAdapter
    }

}