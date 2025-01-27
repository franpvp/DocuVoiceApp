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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.semana01.R
import com.example.semana01.utils.UserManager

@Composable
fun RecuperarContrasenaForm(
    onCodeSent: () -> Unit,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var isEmailSent by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Recuperar Contraseña",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Ingresa tu correo electrónico.",
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Correo Electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Enviar
            Button(
                onClick = {
                    val resetCode = (100000..999999).random().toString() // Código aleatorio
                    val isUserFound = UserManager.saveResetCode(context, email, resetCode)

                    if (isUserFound) {
                        isEmailSent = true
                        errorMessage = null
                        onCodeSent() // Navegar a la pantalla de verificación
                    } else {
                        isEmailSent = false
                        errorMessage = "El correo ingresado no está registrado."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Altura personalizada
                    .padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    "Confirmar",
                    color = Color.White,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge // Tipografía personalizada
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de Error o Confirmación
            when {
                isEmailSent -> {
                    Text(
                        text = "Se ha enviado un código a tu correo electrónico.",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }


}