package com.calipso.remiwaza

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ActivityStateRemisero : AppCompatActivity() {
    private var buttonPress = true
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_state_remisero)
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

        // Inicializar Firebase Auth y DatabaseReference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("drivers")

        val switchButton: Button = findViewById(R.id.buttonState)
        val textState: TextView = findViewById(R.id.textState)

        switchButton.setOnClickListener {
            if (buttonPress) {
                // Cambiar estado a "No Disponible"
                updateDriverState("not available")
                textState.text = "No Disponible"
                switchButton.setBackgroundColor(ContextCompat.getColor(this, R.color.stateRed))
                buttonPress = false
            } else {
                // Cambiar estado a "Disponible"
                updateDriverState("available")
                textState.text = "Disponible"
                switchButton.setBackgroundColor(ContextCompat.getColor(this, R.color.stateGreen))
                buttonPress = true
            }
        }
    }

    private fun updateDriverState(state: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).child("state").setValue(state)
                .addOnSuccessListener {
                    Toast.makeText(this, "Estado actualizado correctamente.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar el estado: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        }
    }
}