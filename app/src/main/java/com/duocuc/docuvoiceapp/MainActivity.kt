package com.duocuc.docuvoiceapp

import RecuperarContrasenaForm
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : ComponentActivity() {

    private lateinit var locationHelper: LocationHelper
    private var currentCity by mutableStateOf<String?>("")
    private lateinit var database: DatabaseReference

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
        locationHelper = LocationHelper(this, this)
        database = FirebaseDatabase.getInstance().getReference("ciudad")

        locationHelper.getCurrentCity { city ->
            city?.let {
                currentCity = it
                Toast.makeText(this, "Ciudad actual: $it", Toast.LENGTH_LONG).show()
            } ?: run {
                Toast.makeText(this, "No se pudo obtener la ciudad actual", Toast.LENGTH_LONG).show()
            }
        }

    }


}


@Preview(showBackground = true)
@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
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