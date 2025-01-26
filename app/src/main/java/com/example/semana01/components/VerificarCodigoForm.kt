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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semana01.utils.UserManager

@Composable
fun VerificarCodigoForm(
    email: String,
    onCodeVerified: () -> Unit // Callback para navegar al cambio de contraseña
) {
    var code by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Obtener el contexto local
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Verificar Código",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Código de Verificación") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Validar el código con el contexto actual
                val isValid = UserManager.validateResetCode(context, email, code)
                if (isValid) {
                    onCodeVerified() // Navegar a la pantalla de cambio de contraseña
                } else {
                    errorMessage = "El código ingresado no es válido."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verificar")
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = Color.Red)
        }
    }
}

@Preview
@Composable
fun PreviewVerificarCodigoForm() {
    MaterialTheme {
        Surface {
            VerificarCodigoForm(
                email = "test@example.com",
                onCodeVerified = {}
            )
        }
    }
}