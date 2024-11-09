
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// Variables de Firebase
private lateinit var auth: FirebaseAuth
private lateinit var database: DatabaseReference

fun registerUser(email: String, password: String, displayName: String, userType: String) {
    auth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance().getReference("users")

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Obtener el UID del usuario registrado
                val userId = auth.currentUser?.uid

                // Crear el objeto de datos del usuario
                val userData = mapOf(
                    "email" to email,
                    "displayName" to displayName,
                    "userType" to userType  // Puede ser "driver" o "owner"
                )

                // Guardar los datos del usuario en Realtime Database
                userId?.let {
                    database.child(it).setValue(userData)
                        .addOnSuccessListener {
                            // Registro exitoso
                            Log.d("Register", "Usuario registrado y datos guardados.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Register", "Error al guardar los datos del usuario: ${e.message}")
                        }
                }
            } else {
                // Manejo de errores en el registro
                Log.e("Register", "Error en el registro: ${task.exception?.message}")
            }
        }
}
