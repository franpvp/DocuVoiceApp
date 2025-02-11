package com.duocuc.docuvoiceapp.services

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class Usuario(
    val id: String? = null, // UID de Firebase
    val nombre: String?,
    val apellido: String?,
    val correo: String?,
    val celular: String?,
    val fechaNacimiento: String?
)

class DatabaseService {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Guardar usuario en Firebase Realtime Database
    fun guardarUsuario(usuario: Usuario) {
        usuario.id?.let { uid ->
            database.child("usuarios").child(uid).setValue(usuario)
                .addOnSuccessListener {
                    Log.d("DatabaseService", "Usuario guardado correctamente")
                }
                .addOnFailureListener { e ->
                    Log.e("DatabaseService", "Error al guardar usuario", e)
                }
        }
    }

    // Obtener usuario por ID
    fun obtenerUsuario(uid: String, onComplete: (Usuario?) -> Unit) {
        database.child("usuarios").child(uid).get()
            .addOnSuccessListener { dataSnapshot ->
                val usuario = dataSnapshot.getValue(Usuario::class.java)
                onComplete(usuario)
            }
            .addOnFailureListener {
                Log.e("DatabaseService", "Error al obtener usuario")
                onComplete(null)
            }
    }
}