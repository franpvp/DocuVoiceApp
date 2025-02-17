package com.duocuc.docuvoiceapp.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.duocuc.docuvoiceapp.R
import com.duocuc.docuvoiceapp.services.AuthService
import java.text.SimpleDateFormat
import java.util.*
import com.duocuc.docuvoiceapp.services.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registro(onRegisterSuccess: () -> Unit, navController: NavController) {
    val nombre = remember { mutableStateOf("") }
    val apellido = remember { mutableStateOf("") }
    val correo = remember { mutableStateOf("") }
    val celular = remember { mutableStateOf("") }
    val fechaNacimiento = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }
    val confirmarContrasena = remember { mutableStateOf("") }
    val showPassword = remember { mutableStateOf(false) }
    val showConfirmPassword = remember { mutableStateOf(false) }
    val correoError = remember { mutableStateOf(false) }
    val celularError = remember { mutableStateOf(false) }
    val nombreError = remember { mutableStateOf(false) }
    val apellidoError = remember { mutableStateOf(false) }
    val contrasenaError = remember { mutableStateOf(false) }
    val confirmarContrasenaError = remember { mutableStateOf(false) }

    // Estado para mostrar el DatePickerDialog
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    LaunchedEffect(selectedDate) {
        if (selectedDate.isNotEmpty()) {
            fechaNacimiento.value = selectedDate // Guardar la fecha seleccionada
        }
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TopAppBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                // Título centrado
                Text(
                    text = "Registro",
                    fontSize = 35.sp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary, // Cambia el color aquí
                    modifier = Modifier
                        .align(Alignment.Center)
                )

                // Icono de retroceso a la izquierda
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            // Campos de entrada con validación
            OutlinedTextField(
                value = nombre.value,
                onValueChange = {
                    nombre.value = it
                    nombreError.value = false // Limpiar error al cambiar el valor
                },
                label = { Text("Nombre", color = Color.Gray) },
                placeholder = { Text("Ingresa tu nombre", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)) // Bordes redondeados
                    .padding(10.dp),
                keyboardOptions = KeyboardOptions.Default,
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 25.dp, end = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user), // Usa un ícono de usuario
                            contentDescription = null,
                            tint = Color(0xFF0367A6)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                },
                isError = nombreError.value, // Mostrar error si hay problema
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6),
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    cursorColor = Color(0xFF0367A6)
                )
            )
            OutlinedTextField(
                value = apellido.value,
                onValueChange = {
                    apellido.value = it
                    apellidoError.value = false // Limpiar error al cambiar el valor
                },
                label = { Text("Apellido", color = Color.Gray) },
                placeholder = { Text("Ingresa tu apellido", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)) // Bordes redondeados
                    .padding(10.dp),
                keyboardOptions = KeyboardOptions.Default,
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 25.dp, end = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user), // Ícono de usuario, usa uno adecuado si lo tienes
                            contentDescription = null,
                            tint = Color(0xFF0367A6)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                },
                isError = apellidoError.value, // Mostrar error si hay problema
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6),
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    cursorColor = Color(0xFF0367A6)
                )
            )

            // Validación de correo electrónico
            OutlinedTextField(
                value = correo.value,
                onValueChange = {
                    correo.value = it
                    correoError.value = !isValidEmail(it) // Validación en tiempo real
                },
                label = { Text("Correo electrónico", color = Color.Gray) },
                placeholder = { Text("ejemplo@correo.com", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)) // Bordes redondeados
                    .padding(10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Teclado de email
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 25.dp, end = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email), // Ícono de email
                            contentDescription = null,
                            tint = Color(0xFF0367A6)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                },
                isError = correoError.value, // Mostrar error si el email es inválido
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6),
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    cursorColor = Color(0xFF0367A6)
                )
            )
//            if (correoError.value) {
//                Text(
//                    text = "Correo inválido",
//                    color = Color.Red,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.align(Alignment.Start)
//                )
//            }

            // Validación celular (solo números)
            OutlinedTextField(
                value = celular.value,
                onValueChange = {
                    // Asegura que solo se ingresen números y no más de 9 dígitos
                    if (it.length <= 9 && it.matches("^[0-9]*$".toRegex())) {
                        celular.value = it
                    }
                    celularError.value = celular.value.isEmpty() || celular.value.length != 9
                },
                label = { Text("Celular", color = Color.Gray) },
                placeholder = { Text("Ingresa tu celular", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)) // Bordes redondeados
                    .padding(10.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), // Teclado numérico
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 25.dp, end = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.llamar), // Ícono de teléfono
                            contentDescription = null,
                            tint = Color(0xFF0367A6),
                            modifier = Modifier.size(24.dp) // Ajusta el tamaño del ícono aquí
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                },
                isError = celularError.value, // Mostrar error si el celular es inválido
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0367A6),
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    cursorColor = Color(0xFF0367A6)
                )
            )

            // Campo de Fecha de Nacimiento
            FechaNacimientoInput(
                selectedDate = fechaNacimiento.value,
                onDateSelected = { selectedDate -> fechaNacimiento.value = selectedDate }
            )

            // Contraseña y Confirmar Contraseña
            PasswordField(
                value = contrasena.value,
                onValueChange = { contrasena.value = it },
                label = "Contraseña",
                showPassword = showPassword.value,
                onTogglePasswordVisibility = { showPassword.value = !showPassword.value }
            )

            PasswordField(
                value = confirmarContrasena.value,
                onValueChange = { confirmarContrasena.value = it },
                label = "Confirmar Contraseña",
                showPassword = showConfirmPassword.value,
                onTogglePasswordVisibility = { showConfirmPassword.value = !showConfirmPassword.value }
            )

            // Botón Registrarse
            Button(
                onClick = {
                    // Validación de campos
                    nombreError.value = nombre.value.isEmpty()
                    apellidoError.value = apellido.value.isEmpty()
                    celularError.value = celular.value.isEmpty() || celularError.value
                    correoError.value = correo.value.isEmpty() || !isValidEmail(correo.value)
                    contrasenaError.value = contrasena.value.isEmpty()
                    confirmarContrasenaError.value = confirmarContrasena.value.isEmpty()

                    if (nombre.value.isNotEmpty() &&
                        apellido.value.isNotEmpty() &&
                        celular.value.isNotEmpty() &&
                        isValidEmail(correo.value) &&
                        contrasena.value.isNotEmpty() &&
                        contrasena.value == confirmarContrasena.value) {

                        // Crear objeto Usuario con los valores del formulario
                        val usuario = Usuario(
                            nombre = nombre.value,
                            apellido = apellido.value,
                            correo = correo.value,
                            celular = celular.value,
                            fechaNacimiento = fechaNacimiento.value
                        )

                        // Registrar usuario en Firebase
                        AuthService(context).registerUser(correo.value, contrasena.value, usuario) { success, message ->
                            if (success) {
                                // Autenticación automática después del registro
                                AuthService(context).signInUser(correo.value, contrasena.value) { signInSuccess, signInMessage ->
                                    if (signInSuccess) {
                                        // Si el inicio de sesión es exitoso, redirigir al home
                                        onRegisterSuccess() // Puedes utilizar esto para otras acciones si es necesario
                                        navController.navigate("home")  // Reemplaza "home_route" con el nombre real de tu ruta
                                    } else {
                                        // Mostrar mensaje de error de autenticación si falla el inicio de sesión
                                        Toast.makeText(context, "Error al iniciar sesión: ${signInMessage ?: "Error desconocido"}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                // Mostrar mensaje de error
                                Toast.makeText(context, "Error: ${message ?: "Error desconocido"}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .padding(vertical = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(datePickerState: DatePickerState, onDismiss: () -> Unit, onDateSelected: (String) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Botón de confirmación
                Button(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val selectedDateFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(Date(selectedMillis))
                            onDateSelected(selectedDateFormatted) // Pasa la fecha seleccionada al input
                        }
                        onDismiss() // Cierra el DatePickerDialog
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirmar Fecha")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        placeholder = { Text("Ingresa tu contraseña", color = Color.LightGray) },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp)) // Bordes redondeados
            .padding(10.dp),
        leadingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 25.dp, end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock), // Ícono de candado
                    contentDescription = null,
                    tint = Color(0xFF0367A6),
                    modifier = Modifier.size(24.dp) // Ajusta el tamaño del ícono aquí
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        },
        trailingIcon = {
            IconButton(onClick = onTogglePasswordVisibility) {
                Icon(
                    painter = painterResource(
                        id = if (showPassword) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    ),
                    contentDescription = "Toggle Password Visibility"
                )
            }
        },
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF0367A6),
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.Gray,
            errorBorderColor = Color.Red,
            cursorColor = Color(0xFF0367A6)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaNacimientoInput(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        label = { Text("Fecha de Nacimiento", color = Color.Gray) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha",
                    tint = Color(0xFF0367A6) // Color azul para coherencia con el diseño
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .padding(10.dp)
            .clickable { showDatePicker = true },
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF0367A6),
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.Gray,
            errorBorderColor = Color.Red,
            cursorColor = Color(0xFF0367A6)
        )
    )

    // Mostrar el DatePicker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            datePickerState = datePickerState,
            onDismiss = { showDatePicker = false },
            onDateSelected = { selectedDateFormatted -> onDateSelected(selectedDateFormatted) }
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return emailRegex.matches(email)
}
