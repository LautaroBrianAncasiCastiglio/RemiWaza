package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityPerfilAgencia : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var textName: TextView
    private lateinit var textMail: TextView
    private lateinit var textPassword: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_agencia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
        val button = findViewById<Button>(R.id.btnModificar)
        button.setOnClickListener {
            val intent1 = Intent(applicationContext, ActivityModificarAgencia::class.java)
            startActivity(intent1)
        }
        val autoButton: LinearLayout = findViewById(R.id.btnCarro)
        autoButton.setOnClickListener {
            val intent = Intent(this, ActivityAgregarAuto::class.java)
            startActivity(intent)
        }
        val button2 = findViewById<Button>(R.id.btnCerrarSesion)
        button2.setOnClickListener {
            val intent1 = Intent(applicationContext, ActivityLoginAgencia::class.java)
            startActivity(intent1)
        }

        textName = findViewById(R.id.textNombre)  // O cambia el ID según el XML
        textMail = findViewById(R.id.textMail)
        textPassword = findViewById(R.id.textPassword)


        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("owners")
        // Cargar los datos de la agencia
        loadAgencyData()
    }

    private fun loadAgencyData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Rellenar los campos con los datos de la agencia
                        textName.text = snapshot.child("name").getValue(String::class.java)
                        textMail.text = snapshot.child("email").getValue(String::class.java)
                        textPassword.text = snapshot.child("password").getValue(String::class.java)
                    } else {
                        Toast.makeText(this@ActivityPerfilAgencia, "No se encontraron datos de la agencia.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ActivityPerfilAgencia, "Error al cargar los datos: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        }
    }
}