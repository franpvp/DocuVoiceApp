package com.duocuc.docuvoiceapp.components

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.duocuc.docuvoiceapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun Menu(navController: NavController) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(2) }
    var showDialog by remember { mutableStateOf(false) } // Estado para mostrar el pop-up
    val userNameState = remember { mutableStateOf("") }
    val handleLogout = {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("login")
    }
    val fileName = "profile_image.jpg"
    var profileImage by remember { mutableStateOf<Bitmap?>(loadImageFromStorage(context, fileName)) }

    val database = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(Unit) {
        currentUser?.uid?.let { uid ->
            database.child("usuarios").child(uid).child("nombre")
                .get()
                .addOnSuccessListener { snapshot ->
                    val nombre = snapshot.value as? String
                    if (nombre != null) {
                        userNameState.value = nombre
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al obtener el nombre", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Contenedor principal
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center
        ) {
            // Foto de perfil centrada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                // Imagen de perfil
                if (profileImage != null) {
                    Image(
                        bitmap = profileImage!!.asImageBitmap(),
                        contentDescription = "Imagen de perfil seleccionada",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color.Gray, CircleShape)
                            .background(Color.White)
                            .clickable {
                                // Navegar al perfil para cambiar la foto
                                navController.navigate("perfil")
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "Imagen de perfil predeterminada",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(10.dp)
                            .clickable {
                                navController.navigate("perfil")
                            }
                    )
                }
            }
            Text(
                text = userNameState.value,
                style = TextStyle(
                    fontSize = 28.sp, // Ajusta el tamaño según lo necesites
                    fontWeight = FontWeight.Bold, // Opcional: aumentar el grosor
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(70.dp))

            // Opciones del menú centradas
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Opciones del menú como botones
                val buttonModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp)
                    .height(60.dp)

                Button(
                    onClick = {
                        navController.navigate("perfil")
                    },
                    modifier = buttonModifier

                ) {
                    Text(
                        text = "Mi Perfil",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Button(
                    onClick = { navController.navigate("forgotPassword") },
                    modifier = buttonModifier
                ) {
                    Text(
                        text = "Cambio de Contraseña",
                        fontSize = 20.sp,
                    )
                }

                Button(
                    onClick = { navController.navigate("contacto") },
                    modifier = buttonModifier
                ) {
                    Text(text = "Contacto",
                        fontSize = 20.sp,
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Botón para cerrar sesión que muestra el pop-up
                Button(
                    onClick = { showDialog = true },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F), // Color de fondo
                        contentColor = Color.White  // Color del texto
                    )
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 20.sp
                    )
                }
            }
        }

        // Tab en la parte inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Lo posiciona en la parte inferior del `Box` principal
                //.padding(horizontal = 6.dp) // Espaciado alrededor
                //.clip(RoundedCornerShape(24.dp)) // Bordes redondeados
                .background(Color.Black)
                //.padding(vertical = 6.dp) // Espaciado interno del tab
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp), // Altura fija para garantizar consistencia
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically // Centra los íconos verticalmente
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    tint = if (selectedTab == 0) Color.White else Color.Gray,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { selectedTab = 0
                            navController.navigate("home")}
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

        // Pop-up para confirmar el cierre de sesión
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "¿Cerrar sesión?") },
                text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            handleLogout() // Acción de cerrar sesión
                            showDialog = false // Cerrar el pop-up
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false } // Cerrar el pop-up sin hacer nada
                    ) {
                        Text("No")
                    }
                }
            )
        }
    }
}