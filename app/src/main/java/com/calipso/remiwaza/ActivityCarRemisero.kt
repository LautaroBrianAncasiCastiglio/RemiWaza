package com.calipso.remiwaza

import android.R
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
import com.calipso.remiwaza.databinding.ActivityCarRemiseroBinding

class ActivityCarRemisero : AppCompatActivity() {

    var listaCarro: ArrayList<ListaAutoRemisero> = ArrayList()
    lateinit var arrayAdapter: ArrayAdapter<*>
    private lateinit var binding : ActivityCarRemiseroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarRemiseroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaCarro.add(
            ListaAutoRemisero("a","a","a")
        )
        listaCarro.add(
            ListaAutoRemisero("a","a","a")
        )
        listaCarro.add(
            ListaAutoRemisero("a","a","a")
        )
        listaCarro.add(
            ListaAutoRemisero("a","a","a")
        )

        //val adapter = ListaAutoRemisero(this, listaCarro)

        val adapter = MyAdapter(this, listaCarro)
        binding.listaDerecha.adapter = adapter


    }

}