package com.duocuc.docuvoiceapp

import RecuperarContrasenaForm
import android.content.Context
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duocuc.docuvoiceapp.components.CambiarContrasenaForm
import com.duocuc.docuvoiceapp.components.Contacto
import com.duocuc.docuvoiceapp.components.Home
import com.duocuc.docuvoiceapp.ui.theme.Semana01Theme
import com.duocuc.docuvoiceapp.components.Login
import com.duocuc.docuvoiceapp.components.Menu
import com.duocuc.docuvoiceapp.components.MisMensajes
import com.duocuc.docuvoiceapp.components.Perfil
import com.duocuc.docuvoiceapp.components.Registro
import com.duocuc.docuvoiceapp.utils.UserManager
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            Semana01Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        MyApp()
                    }
                }

            }
        }
        // logRegisteredUsers(this)
    }
}

//private fun logRegisteredUsers(context: Context) {
//    // Obtener la lista de usuarios desde UserManager
//    val users = UserManager.getUsersFromPrefs(context)
//
//    // Verificar si hay usuarios y loguearlos
//    if (users.isNotEmpty()) {
//        users.forEach { user ->
//            Log.d("MainActivity", "Usuario registrado: Email: ${user.email}, Nombre: ${user.firstName}, Apellido: ${user.lastName}, Password: ${user.password}")
//        }
//    } else {
//        Log.d("MainActivity", "No hay usuarios registrados.")
//    }
//}


@Preview(showBackground = true)
@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            Login(
                onLoginClick = { navController.navigate("home") },
                onRegisterClick = { navController.navigate("register") },
                onForgotPasswordClick = { navController.navigate("forgotPassword") }
            )
        }
        composable("register") {
            Registro(
                navController = navController,
                onRegisterSuccess = { navController.popBackStack() }
            )
        }
        composable("forgotPassword") {
            // Aquí se pasa el callback onEmailSent que navegará hacia atrás al login
            RecuperarContrasenaForm(
                navController = navController
            )
        }
        composable("home") {
            Home(
                navController = navController
            )
        }

        composable("changePassword/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            CambiarContrasenaForm(
                navController = navController,
                email = email,
                onPasswordChanged = { navController.popBackStack() },
                context = LocalContext.current
            )
        }

        composable("mensajes") {
            MisMensajes( navController = navController)
        }

        composable("perfil") {
            Perfil( navController = navController)
        }

        composable("menu") {
            Menu(
                navController = navController
            )
        }
        composable("contacto") {
            Contacto(
                onContactSubmit = {navController.navigate("home")},
                navController = navController
            )
        }

    }
}