package com.calipso.remiwaza

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



class ActivityAdvertencia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_advertencia)

        val textView: TextView = findViewById(R.id.textView)
        val texto = "Texto con Contorno"
        val strokeColor = Color.BLACK  // Color del contorno
        val textColor = Color.WHITE    // Color del texto
        val strokeWidth = 6f           // Grosor del contorno (ajusta según necesidad)

        val spannableString = SpannableString(texto)
        spannableString.setSpan(
            OutlineSpan(strokeWidth, strokeColor, textColor),
            0, texto.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
        textView.textSize = 40f // Tamaño del texto

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}