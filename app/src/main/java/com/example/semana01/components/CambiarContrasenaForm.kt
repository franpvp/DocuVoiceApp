package com.example.semana01.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semana01.utils.UserManager

@Composable
fun CambiarContrasenaForm(
    email: String,
    resetCode: String,
    onPasswordChanged: () -> Unit,
    context: Context
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPasswordChanged by remember { mutableStateOf(false) }

    // Validación de los campos de la contraseña
    fun handleChangePassword() {
        if (newPassword != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden."
        } else {
            val isCodeValid = UserManager.validateResetCode(context, email, resetCode)
            if (isCodeValid) {
                val isUpdated = UserManager.updatePassword(context, email, newPassword)
                if (isUpdated) {
                    isPasswordChanged = true
                    errorMessage = null
                    onPasswordChanged() // Navegar después del cambio
                } else {
                    errorMessage = "Error al actualizar la contraseña."
                }
            } else {
                errorMessage = "Código de recuperación inválido."
            }
        }
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
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ingresa una nueva contraseña.",
            fontSize = 14.sp,
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
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Cambiar Contraseña
        Button(
            onClick = { handleChangePassword() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cambiar Contraseña")
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

@Preview
@Composable
fun PreviewChangePasswordForm() {
    MaterialTheme {
        Surface {
            CambiarContrasenaForm(email = "test@example.com", resetCode = "123456", onPasswordChanged = {}, context = LocalContext.current)
        }
    }
}