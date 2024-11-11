//Archivo para la lógica de autenticación:
package com.calipso.remiwaza

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object AuthHelper {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    fun registerUser(email: String, password: String, displayName: String, userType: String) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userData = mapOf(
                        "email" to email,
                        "displayName" to displayName,
                        "userType" to userType
                    )
                    userId?.let {
                        database.child(it).setValue(userData)
                            .addOnSuccessListener {
                                Log.d("Register", "Usuario registrado y datos guardados.")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Register", "Error al guardar datos del usuario: ${e.message}")
                            }
                    }
                } else {
                    Log.e("Register", "Error en el registro: ${task.exception?.message}")
                }
            }
    }

    fun loginUser(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        database.child(it).get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                val userType = dataSnapshot.child("userType").getValue(String::class.java)
                                when (userType) {
                                    "driver" -> Log.d("Login", "Usuario es un remisero.")
                                    "owner" -> Log.d("Login", "Usuario es el dueño de la remisería.")
                                    else -> Log.d("Login", "Tipo de usuario desconocido.")
                                }
                            } else {
                                Log.e("Login", "No se encontraron datos del usuario en la base de datos.")
                            }
                        }.addOnFailureListener { e ->
                            Log.e("Login", "Error al obtener datos del usuario: ${e.message}")
                        }
                    }
                } else {
                    Log.e("Login", "Error en el login: ${task.exception?.message}")
                }
            }
    }
}
