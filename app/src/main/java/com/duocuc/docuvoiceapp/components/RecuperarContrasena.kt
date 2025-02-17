import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duocuc.docuvoiceapp.R
import com.duocuc.docuvoiceapp.utils.UserManager
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarContrasenaForm(
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    fun enviarCodigo() {
        if (email.isEmpty()) {
            errorMessage = "Ingresa un correo válido."
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Código enviado a tu correo", Toast.LENGTH_LONG).show()
                    navController.navigate("login")
                } else {
                    errorMessage = "Error al enviar el código."
                }
            }
            .addOnFailureListener { e ->
                errorMessage = "Fallo: ${e.localizedMessage}"
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Botón de volver en la esquina superior izquierda
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = 8.dp, top = 15.dp)
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
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "Ingresa tu correo electrónico.",
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Correo Electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("email_field"), // Añadir un testTag aquí
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 25.dp, end = 16.dp) // Padding a la izquierda del ícono
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = null,
                            tint = Color(0xFF0367A6)
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // Mueve el texto un poco a la derecha
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6), // Borde transparente cuando está enfocado
                    unfocusedBorderColor = Color.Gray, // Borde transparente cuando no está en foco
                    disabledBorderColor = Color.Gray, // Borde transparente si está deshabilitado
                    errorBorderColor = Color.Red, // Borde transparente si hay error
                    cursorColor = Color(0xFF0367A6), // Color del cursor

                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Enviar
            Button(
                onClick = {
                    enviarCodigo()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0367A6), // Color de fondo
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Confirmar",
                    color = Color.White,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de Error o Confirmación
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}