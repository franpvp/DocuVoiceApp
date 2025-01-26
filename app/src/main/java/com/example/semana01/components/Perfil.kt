package com.example.semana01.components

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
import androidx.navigation.NavController
import com.example.semana01.R

@Composable
fun Perfil(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Minuta Semanal",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    tint = if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier
                        .size(30.dp)  // Tamaño modificado
                        .clickable { selectedTab = 0 }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Mensajes",
                    tint = if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier
                        .size(30.dp)  // Tamaño modificado
                        .clickable {
                            selectedTab = 1
                            navController.navigate("mensajes")
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Menu",
                    tint = if (selectedTab == 2) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier
                        .size(30.dp)  // Tamaño modificado
                        .clickable {
                            selectedTab = 2
                            navController.navigate("menu")
                        }
                )
            }

            // Botón para mostrar el pop-up de cierre de sesión con estilo mejorado
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Cerrar sesión", style = MaterialTheme.typography.bodySmall)
            }
        }

        // Dialogo de confirmación para cerrar sesión con estilo Material Design
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("¿Está seguro de que desea cerrar sesión?", style = MaterialTheme.typography.bodySmall) },
                text = {
                    Text("Se cerrará su sesión actual y perderá cualquier cambio no guardado.",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Lógica para cerrar sesión (redirigir a Login)
                            navController.navigate("login") // Navegar al login
                            showDialog = false // Cerrar el diálogo
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Sí", style = MaterialTheme.typography.bodySmall)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text("Cancelar", style = MaterialTheme.typography.bodySmall)
                    }
                }
            )
        }
    }
}
