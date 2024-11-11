// Archivo para la lógica de autenticación:
package com.tu_paquete.remiwaza

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object AuthHelper {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: DatabaseReference by lazy { FirebaseDatabase.getInstance().getReference("users") }

    fun registerUser(email: String, password: String, displayName: String, userType: String, callback: (Boolean) -> Unit) {
        // Crear usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userData = mapOf(
                        "email" to email,
                        "displayName" to displayName,
                        "userType" to userType
                    )
                    // Guardar datos adicionales en Firebase Realtime Database
                    userId?.let {
                        database.child(it).setValue(userData)
                            .addOnSuccessListener {
                                Log.d("Register", "Usuario registrado y datos guardados.")
                                callback(true) // Registro exitoso
                            }
                            .addOnFailureListener { e ->
                                Log.e("Register", "Error al guardar datos del usuario: ${e.message}")
                                callback(false) // Error al guardar datos
                            }
                    } ?: callback(false) // Error si userId es nulo
                } else {
                    Log.e("Register", "Error en el registro: ${task.exception?.message}")
                    callback(false) // Error en el registro
                }
            }
    }

    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        database.child(it).get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                val userType = dataSnapshot.child("userType").getValue(String::class.java)
                                callback(true, userType) // Login exitoso con tipo de usuario
                            } else {
                                Log.e("Login", "No se encontraron datos del usuario en la base de datos.")
                                callback(false, null) // Error al obtener datos del usuario
                            }
                        }.addOnFailureListener { e ->
                            Log.e("Login", "Error al obtener datos del usuario: ${e.message}")
                            callback(false, null) // Error al obtener datos
                        }
                    }
                } else {
                    Log.e("Login", "Error en el login: ${task.exception?.message}")
                    callback(false, null) // Error en el login
                }
            }
    }
}
