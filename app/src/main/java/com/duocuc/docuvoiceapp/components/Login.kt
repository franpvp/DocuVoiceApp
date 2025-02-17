package com.duocuc.docuvoiceapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.duocuc.docuvoiceapp.R
import com.duocuc.docuvoiceapp.utils.UserManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isPasswordVisible = remember { mutableStateOf(false) }
    val loginError = remember { mutableStateOf(false) } // Estado para el error
    val isLoading = remember { mutableStateOf(false) } // Estado para mostrar la animación

    // Animación de Lottie
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    // Manejar el efecto de carga
    if (isLoading.value) {
        LaunchedEffect(Unit) {
            delay(3000)
            isLoading.value = false
            onLoginClick()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo en la parte superior
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(bottom = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )

            // Campo de correo electrónico
            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                    loginError.value = false // Limpiar error al cambiar el email
                },
                label = { Text("Correo electrónico", color = Color.Gray) },
                placeholder = { Text("ejemplo@correo.com", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)) // Bordes redondeados
                    .padding(10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                isError = loginError.value, // Mostrar error si el login falla
                shape = RoundedCornerShape(30.dp), // Bordes redondeados en el input
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6), // Borde transparente cuando está enfocado
                    unfocusedBorderColor = Color.Gray, // Borde transparente cuando no está en foco
                    disabledBorderColor = Color.Gray, // Borde transparente si está deshabilitado
                    errorBorderColor = Color.Red, // Borde transparente si hay error
                    cursorColor = Color(0xFF0367A6), // Color del cursor

                )
            )


            // Campo de contraseña
            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                    loginError.value = false // Limpiar error al cambiar la contraseña
                },
                label = { Text("Contraseña", color = Color.Gray) },
                placeholder = { Text("Ingresa tu contraseña", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp))
                    .padding(10.dp),
                visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 25.dp, end = 16.dp) // Padding a la izquierda del ícono
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = null, // No es necesario para accesibilidad
                            tint = Color(0xFF0367A6)
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // Mueve el texto un poco a la derecha
                    }
                },
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 25.dp, end = 16.dp) // Padding a la izquierda del ícono
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isPasswordVisible.value) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            ),
                            contentDescription = if (isPasswordVisible.value) "Ocultar contraseña" else "Mostrar contraseña",
                            modifier = Modifier
                                .clickable { isPasswordVisible.value = !isPasswordVisible.value }
                                .padding(8.dp), // Espaciado para que sea más fácil tocar
                        )
                    }

                },
                isError = loginError.value,
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6), // Borde transparente cuando está enfocado
                    unfocusedBorderColor = Color.Gray, // Borde transparente cuando no está en foco
                    disabledBorderColor = Color.Gray, // Borde transparente si está deshabilitado
                    errorBorderColor = Color.Red, // Borde transparente si hay error
                    cursorColor = Color(0xFF0367A6), // Color del cursor
                )
            )
            Spacer(modifier = Modifier.height(5.dp))

            // Mostrar mensaje de error si las credenciales son incorrectas
            if (loginError.value) {
                Text(
                    text = "Correo o contraseña incorrectos.",
                    color = Color.Red,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Text(
                text = "¿Olvidaste tu contraseña?",
                modifier = Modifier
                    .clickable { onForgotPasswordClick() }
                    .align(Alignment.End)
                    .padding(end = 16.dp, top = 5.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(35.dp))

            // Botón de iniciar sesión
            Button(
                onClick = {
                    if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                        // Activar animación de carga
                        isLoading.value = true

                        // Iniciar sesión con Firebase Authentication
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email.value, password.value)
                            .addOnCompleteListener { task ->
                                isLoading.value = false  // Desactivar animación de carga

                                if (task.isSuccessful) {
                                    val user = FirebaseAuth.getInstance().currentUser
                                    user?.let {
                                        onLoginClick()
                                    }
                                } else {
                                    // Error de autenticación, mostrar mensaje de error
                                    loginError.value = true
                                }
                            }
                    } else {
                        loginError.value = true // Mostrar error si los campos están vacíos
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp) // Padding horizontal
                    .height(60.dp) // Ajusta la altura del botón
                    .testTag("login_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0367A6), // Color de fondo
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "¿No tienes cuenta? Regístrate",
                modifier = Modifier
                    .clickable { onRegisterClick() }
                    .padding(top = 8.dp)
                    .testTag("registro"),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Divider(
                color = Color.Gray, // Color de la línea
                thickness = 2.dp // Grosor de la línea
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "o inicia sesión con",
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Iconos en la parte inferior
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gmail),
                    contentDescription = "Ícono de Gmail",
                    modifier = Modifier.size(35.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.logox),
                    contentDescription = "Ícono de X",
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)) // Más oscuro
                    .padding(16.dp)
                    .testTag("loading_animation"),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(150.dp),
                )
            }
        }
    }
}