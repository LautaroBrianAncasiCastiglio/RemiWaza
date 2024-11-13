package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ActivityInicioAgencia : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_agencia)

        // Inicializar vistas
        // Configuración de botones
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
    }

}
