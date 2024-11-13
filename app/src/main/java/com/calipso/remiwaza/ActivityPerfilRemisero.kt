package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

class ActivityPerfilRemisero : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var textName: TextView
    private lateinit var textLastName: TextView
    private lateinit var textMail: TextView
    private lateinit var textPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_remisero)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userButton: LinearLayout = findViewById(R.id.btnCountRemisero)
        userButton.setOnClickListener {
            val intent = Intent(this, ActivityPerfilRemisero::class.java)
            startActivity(intent)
        }
        val agenciaButton: LinearLayout = findViewById(R.id.btnAgenciaRemisero)
        agenciaButton.setOnClickListener {
            val intent = Intent(this, ActivityInicioRemisero::class.java)
            startActivity(intent)
        }
        val button =findViewById<Button>(R.id.btnModificar)
        button.setOnClickListener{
            val intent1= Intent(applicationContext, ActivityModificarRemisero::class.java)
            startActivity(intent1)
        }
        val button2 =findViewById<Button>(R.id.btnCerrarSesion)
        button2.setOnClickListener{
            val intent1= Intent(applicationContext, ActivityLoginRemisero::class.java)
            startActivity(intent1)
        }

        textName = findViewById(R.id.textName)
        textLastName = findViewById(R.id.textLastName)
        textMail = findViewById(R.id.textMail)
        textPassword = findViewById(R.id.textPassword)


        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("drivers")

        loadUserData()
    }
    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        textName.setText(snapshot.child("name").getValue(String::class.java))
                        textLastName.setText(snapshot.child("lastName").getValue(String::class.java))
                        textMail.setText(snapshot.child("email").getValue(String::class.java))
                        textPassword.setText(snapshot.child("password").getValue(String::class.java))
                    } else {
                        Toast.makeText(this@ActivityPerfilRemisero, "No se encontraron datos del usuario.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ActivityPerfilRemisero, "Error al cargar los datos: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        }
    }
}
