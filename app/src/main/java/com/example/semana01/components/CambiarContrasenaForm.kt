package com.example.semana01.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.semana01.R
import com.example.semana01.utils.UserManager

@Composable
fun CambiarContrasenaForm(
    navController: NavController,
    email: String,
    onPasswordChanged: () -> Unit,
    context: Context
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPasswordChanged by remember { mutableStateOf(false) }

    // Validación de los campos de la contraseña
    fun handleChangePassword() {
        // Verifica si los campos de contraseñas están vacíos
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage = "Ambos campos de contraseña deben ser llenados."
            return
        }

        // Verifica si las contraseñas coinciden
        if (newPassword != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden."
        } else {
            // Verificar si el correo existe
            val user = UserManager.getUsersFromPrefs(context).find { it.email == email }
            if (user != null) {
                // Actualizar la contraseña
                val isUpdated = UserManager.updatePassword(context, email, newPassword)
                if (isUpdated) {
                    isPasswordChanged = true
                    errorMessage = null
                    onPasswordChanged() // Llamar la función de callback para notificar el cambio
                    // Redirigir al login después de un cambio exitoso
                    navController.navigate("login") {
                        popUpTo("forgotPassword") { inclusive = true }
                    }
                } else {
                    errorMessage = "Error al actualizar la contraseña."
                }
            } else {
                errorMessage = "Correo no registrado"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Botón de retroceder en la esquina superior izquierda
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.TopStart) // Posicionarlo en la esquina superior izquierda
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cambiar Contraseña",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Ingresa una nueva contraseña.",
                fontSize = 20.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de nueva contraseña
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nueva Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de confirmación de contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(40.dp))

            // Botón de Cambiar Contraseña
            Button(
                onClick = { handleChangePassword() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "Confirmar",
                    fontSize = 22.sp // Aumenta el tamaño de la letra a 20sp (puedes ajustar el valor)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de error o éxito
            if (isPasswordChanged) {
                Text(
                    text = "Contraseña cambiada exitosamente.",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}