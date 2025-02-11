package com.duocuc.docuvoiceapp.services

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthService(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseService = DatabaseService()

    // Registrar usuario y guardarlo en Realtime Database
    fun registerUser(email: String, password: String, usuario: Usuario, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val usuarioConID = usuario.copy(id = firebaseUser?.uid)
                    databaseService.guardarUsuario(usuarioConID) // Guarda en Firebase Realtime Database
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // Iniciar sesión
    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // Obtener usuario actual
    fun obtenerUsuarioById(): String? {
        return auth.currentUser?.uid
    }

    // Cerrar sesión
    fun logoutUser() {
        auth.signOut()
    }
}