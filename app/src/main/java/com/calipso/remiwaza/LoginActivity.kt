AuthHelper.loginUser("correo@example.com", "contraseña")


fun loginUser(email: String, password: String) {
    auth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance().getReference("users")

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Obtener el UID del usuario autenticado
                val userId = auth.currentUser?.uid

                userId?.let {
                    // Obtener el tipo de usuario desde Realtime Database
                    database.child(it).get().addOnSuccessListener { dataSnapshot ->
                        if (dataSnapshot.exists()) {
                            val userType = dataSnapshot.child("userType").getValue(String::class.java)

                            when (userType) {
                                "driver" -> {
                                    // Redirigir a la vista del remisero
                                    Log.d("Login", "Usuario es un remisero.")
                                }
                                "owner" -> {
                                    // Redirigir a la vista del dueño
                                    Log.d("Login", "Usuario es el dueño de la remisería.")
                                }
                                else -> {
                                    Log.d("Login", "Tipo de usuario desconocido.")
                                }
                            }
                        } else {
                            Log.e("Login", "No se encontraron datos del usuario en la base de datos.")
                        }
                    }.addOnFailureListener { e ->
                        Log.e("Login", "Error al obtener datos del usuario: ${e.message}")
                    }
                }
            } else {
                // Manejo de errores en el login
                Log.e("Login", "Error en el login: ${task.exception?.message}")
            }
        }
}
