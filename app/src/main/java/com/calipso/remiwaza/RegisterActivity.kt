AuthHelper.registerUser("correo@example.com", "contraseña", "Nombre", "driver")

package com.calipso.remiwaza

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val registerButton = findViewById<Button>(R.id.btnRegister)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)

        registerButton.setOnClickListener {
    val email = emailEditText.text.toString()
    val password = passwordEditText.text.toString()
    val name = nameEditText.text.toString()
    val role = "driver"  // o el rol que corresponda

    if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
        AuthHelper.registerUser(email, password, name, role) { success ->
            if (success) {
                val intent = Intent(this, ActivityInicioRemisero::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Registro fallido", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
    }
}


    

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveUserToFirestore(user)
                    updateUI(user)
                } else {
                    Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun saveUserToFirestore(user: FirebaseUser?) {
        user?.let {
            val userMap = hashMapOf(
                "uid" to user.uid,
                "email" to user.email
            )
            db.collection("users").document(user.uid).set(userMap)
                .addOnSuccessListener { Log.d("RegisterActivity", "User added to Firestore") }
                .addOnFailureListener { e -> Log.w("RegisterActivity", "Error adding user", e) }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, ActivityInicioRemisero::class.java)
            startActivity(intent)
            finish()
        }
    }
}
