package com.example.semana01.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object UserManager {

    private const val PREF_NAME = "user_prefs"
    private const val KEY_USERS = "users"

    // Obtener SharedPreferences
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Convertir la lista de usuarios a JSON para almacenarla
    private fun saveUsersToPrefs(context: Context, users: List<User>) {
        val sharedPreferences = getPreferences(context)
        val gson = Gson()
        val usersJson = gson.toJson(users)
        sharedPreferences.edit().putString(KEY_USERS, usersJson).apply()
    }

    // Obtener los usuarios almacenados en SharedPreferences
    fun getUsersFromPrefs(context: Context): List<User> {
        val sharedPreferences = getPreferences(context)
        val usersJson = sharedPreferences.getString(KEY_USERS, null)
        val gson = Gson()

        return if (usersJson != null) {
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(usersJson, type)
        } else {
            emptyList()
        }
    }

    // Agregar un nuevo usuario
    fun addUser(context: Context, email: String, password: String, firstName: String, lastName: String) {
        val users = getUsersFromPrefs(context).toMutableList()
        if (users.any { it.email == email }) {
            throw IllegalArgumentException("El usuario con este correo ya existe.")
        }
        users.add(User(email, password, firstName = firstName, lastName = lastName))
        saveUsersToPrefs(context, users)
    }

    // Validar usuario y contraseña
    fun validateUser(context: Context, email: String, password: String): Boolean {
        val users = getUsersFromPrefs(context)
        return users.any { it.email == email && it.password == password }
    }


    // Actualizar la contraseña de un usuario
    fun updatePassword(context: Context, email: String, newPassword: String): Boolean {
        val users = getUsersFromPrefs(context).toMutableList()
        val user = users.find { it.email == email }
        return if (user != null) {
            user.password = newPassword
            saveUsersToPrefs(context, users)
            true
        } else {
            false
        }
    }
    fun saveLoggedInUserEmail(context: Context, email: String) {
        val sharedPreferences = getPreferences(context)
        sharedPreferences.edit().putString("logged_in_email", email).apply()
    }
}

// Data class para el usuario
data class User(
    val email: String,
    var password: String,
    val firstName: String,
    val lastName: String
)