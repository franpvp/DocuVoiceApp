package com.example.semana01.components

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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.sp
import com.example.semana01.R

@Composable
fun Menu(navController: NavController) {
    var selectedTab by remember { mutableStateOf(2) }
    var showDialog by remember { mutableStateOf(false) } // Estado para mostrar el pop-up

    // Función para cerrar sesión (puedes personalizarla según tu lógica)
    val handleLogout = {
        navController.navigate("login")
    }

    // Contenedor principal
    Box(modifier = Modifier.fillMaxSize()) {
        // Botón de volver en la esquina superior izquierda
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.TopStart) // Posicionar en la esquina superior izquierda
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        // Contenedor principal centrado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center // Centrar verticalmente los elementos dentro de la columna
        ) {
            // Foto de perfil centrada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape) // Recorta la imagen en forma circular
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape) // Borde suave
                        .shadow(4.dp, CircleShape) // Sombra suave
                        .background(MaterialTheme.colorScheme.secondary, shape = CircleShape) // Fondo circular
                        .padding(40.dp)
                )
            }
            Text(
                text = "Nombre de usuario test",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center // Centrar el texto
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
                    .padding(bottom = 12.dp)
                    .height(48.dp)

                Button(
                    onClick = {
                        navController.navigate("perfil") // Navegar a la pantalla de Mi Perfil
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "Mi Perfil",
                        fontSize = 16.sp
                    )
                }

                Button(
                    onClick = { navController.navigate("forgotPassword") },
                    modifier = buttonModifier
                ) {
                    Text(
                        text = "Cambio de Contraseña",
                        fontSize = 16.sp
                    )
                }

                Button(
                    onClick = { navController.navigate("contacto") },
                    modifier = buttonModifier
                ) {
                    Text(text = "Contacto",
                        fontSize = 16.sp
                    )
                }

                // Botón para cerrar sesión que muestra el pop-up
                Button(
                    onClick = { showDialog = true },
                    modifier = buttonModifier
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Tab en la parte inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Lo posiciona en la parte inferior del `Box` principal
                .padding(horizontal = 6.dp) // Espaciado alrededor
                .clip(RoundedCornerShape(24.dp)) // Bordes redondeados
                .background(Color.Black)
                .padding(vertical = 6.dp) // Espaciado interno del tab
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
                            navController.navigate("hpme")}
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