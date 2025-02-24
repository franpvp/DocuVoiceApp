package com.duocuc.docuvoiceapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.duocuc.docuvoiceapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contacto(
    navController: NavController,
    onContactSubmit: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(2) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val isSending = remember { mutableStateOf(false) } // Estado de carga
    val isFormValid = remember { mutableStateOf(true) } // Validación del formulario
    val submissionError = remember { mutableStateOf(false) } // Estado para errores de envío

    // Referencia a la base de datos de Firebase
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("contacto")

    // Función para guardar información de formulario de contacto
    fun guardarContactInfo(name: String, email: String, message: String) {
        val contactId = database.push().key
        if (contactId != null) {
            val contactData = mapOf(
                "name" to name,
                "email" to email,
                "message" to message
            )
            database.child(contactId).setValue(contactData)
                .addOnSuccessListener {
                    isSending.value = false
                    onContactSubmit()
                }
                .addOnFailureListener {
                    submissionError.value = true
                    isSending.value = false
                }
        }
    }

    // Animación de Lottie
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.send2))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    // Manejar el envío del formulario
    if (isSending.value) {
        LaunchedEffect(Unit) {
            delay(4000) // Simula el tiempo de espera para el envío
            if (isFormValid.value) {
                isSending.value = false
                onContactSubmit()
            } else {
                submissionError.value = true // Error si el formulario no es válido
                isSending.value = false // Detener animación si hay un error
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Scroll general
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp), // Margen entre encabezado y campos
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Margen del encabezado
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "Contacto",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 30.sp
                )

                Spacer(modifier = Modifier.size(30.dp)) // Espaciador invisible
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Campos del formulario
//            OutlinedTextField(
//                value = name,
//                onValueChange = { name = it },
//                label = { Text("Nombre") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp),
//                isError = name.isEmpty() && submissionError.value
//            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre", color = Color.Gray) },
                placeholder = { Text("Ingresa tu nombre", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)) // Bordes redondeados
                    .padding(10.dp),
                keyboardOptions = KeyboardOptions.Default,
                isError = name.isEmpty() && submissionError.value,
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6),
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    cursorColor = Color(0xFF0367A6)
                )
            )

//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Correo Electrónico") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//                keyboardActions = KeyboardActions(onNext = { /* Acción Next */ }),
//                isError = email.isEmpty() && submissionError.value
//            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)) // Bordes redondeados
                    .padding(10.dp),
                keyboardOptions = KeyboardOptions.Default,
                isError = email.isEmpty() && submissionError.value,
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6),
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    cursorColor = Color(0xFF0367A6)
                )
            )

//            OutlinedTextField(
//                value = message,
//                onValueChange = { message = it },
//                label = { Text("Mensaje") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//                    .padding(bottom = 24.dp),
//                maxLines = 5,
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(onDone = { /* Acción Done */ }),
//                isError = message.isEmpty() && submissionError.value
//            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Mensaje", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)) // Bordes redondeados
                    .padding(10.dp),
                keyboardOptions = KeyboardOptions.Default,
                isError = message.isEmpty() && submissionError.value,
                shape = RoundedCornerShape(30.dp),
                maxLines = 5, // Número máximo de líneas visibles
                minLines = 3, // Número mínimo de líneas visibles
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6),
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    cursorColor = Color(0xFF0367A6)
                )
            )

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && email.isNotEmpty() && message.isNotEmpty()) {
                        isSending.value = true // Activar animación de carga
                        isFormValid.value = true
                        guardarContactInfo(name, email, message)
                    } else {
                        submissionError.value = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp) // Padding horizontal
                    .height(60.dp), // Ajusta la altura del botón
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0367A6), // Color de fondo
                    contentColor = Color.White // Color del texto o contenido
                )
            ) {
                Text(
                    text = "Enviar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (submissionError.value) {
                Text(
                    text = "Por favor, completa todos los campos correctamente.",
                    color = Color.Red,
                    modifier = Modifier
                        .padding(top = 20.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
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

        // Animación de Lottie mientras se envía
        if (isSending.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize() // Asegura que el fondo cubra toda la pantalla
                    .background(Color.White) // Fondo blanco con opacidad
                    .zIndex(1f) // Coloca el Box con la animación encima de otros elementos
                    .padding(16.dp), // Padding para que no se pegue al borde
                contentAlignment = Alignment.Center // Centra la animación
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(1000.dp) // Ajusta el tamaño de la animación si es necesario
                        .testTag("LottieAnimation")
                )
            }
        }
    }
}