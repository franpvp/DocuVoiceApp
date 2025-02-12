package com.duocuc.docuvoiceapp.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duocuc.docuvoiceapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun Perfil(navController: NavController) {
    var selectedTab by remember { mutableStateOf(2) }
    var showDialog by remember { mutableStateOf(false) }

    // Obtener datos de usuario logeado
    val context = LocalContext.current
    val userNameState = remember { mutableStateOf("") }
    val userLastNameState = remember { mutableStateOf("") }
    val userEmailState = remember { mutableStateOf("") }
    val fechaNacimientoState = remember { mutableStateOf("") }

    // Obtener referencia a la base de datos
    val database = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Obtener el nombre del usuario desde Firebase Database
    LaunchedEffect(Unit) {
        currentUser?.uid?.let { uid ->
            database.child("usuarios").child(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val nombre = snapshot.child("nombre").value as? String
                    val apellido = snapshot.child("apellido").value as? String
                    val correo = snapshot.child("correo").value as? String
                    val fechaNacimiento = snapshot.child("fechaNacimiento").value as? String

                    if (!nombre.isNullOrEmpty()) {
                        userNameState.value = nombre
                    }
                    if (!apellido.isNullOrEmpty()) {
                        userLastNameState.value = apellido
                    }
                    if (!correo.isNullOrEmpty()) {
                        userEmailState.value = correo
                    }
                    if (!fechaNacimiento.isNullOrEmpty()) {
                        fechaNacimientoState.value = fechaNacimiento
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Fondo claro
            .verticalScroll(rememberScrollState()) // Scroll general
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp), // Margen superior
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado con flecha de retroceso y título
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Margen del encabezado
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }, // Navegar hacia atrás
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 30.sp
                )

                Spacer(modifier = Modifier.size(30.dp)) // Espaciador invisible
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Foto de perfil
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Campos de texto no editables
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = userNameState.value,
                    onValueChange = {},
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = userLastNameState.value,
                    onValueChange = {},
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = userEmailState.value,
                    onValueChange = {},
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fechaNacimientoState.value,
                    onValueChange = {},
                    label = { Text("Fecha de nacimiento") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
            }
        }

        // Barra de navegación en la parte inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 6.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Black)
                .padding(vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    tint = if (selectedTab == 0) Color.White else Color.Gray,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            selectedTab = 0
                            navController.navigate("home")
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Mensajes",
                    tint = if (selectedTab == 1) Color.White else Color.Gray,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            selectedTab = 1
                            navController.navigate("mensajes")
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Menu",
                    tint = if (selectedTab == 2) Color.White else Color.Gray,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            selectedTab = 2
                            navController.navigate("menu")
                        }
                )
            }
        }
    }
}