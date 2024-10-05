package com.calipso.remiwaza

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityStateRemisero : AppCompatActivity() {
    private var buttonPress = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_state_remisero)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val switchButton : Button = findViewById(R.id.buttonState)
        val textState : TextView = findViewById(R.id.textState)

        switchButton.setOnClickListener{
            if (buttonPress) {
                // Si el empleado ya está dentro, cambia a "No" y color rojo
                textState.text = "No Disponible"
                switchButton.setBackgroundColor(ContextCompat.getColor(this, R.color.stateRed))
                buttonPress = false
            } else {
                // Si el empleado no está dentro, cambia a "Sí" y color verde
                textState.text = "Disponible"
                switchButton.setBackgroundColor(ContextCompat.getColor(this, R.color.stateGreen))
                buttonPress = true
            }
        }
    }
}