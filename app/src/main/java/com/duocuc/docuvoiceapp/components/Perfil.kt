package com.duocuc.docuvoiceapp.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duocuc.docuvoiceapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun Perfil(navController: NavController) {
    var selectedTab by remember { mutableStateOf(2) }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = LocalContext.current as? Activity

    val fileName = "profile_image.jpg"
    var savedImage by remember { mutableStateOf<Bitmap?>(loadImageFromStorage(context, fileName)) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            savedImage = bitmap
            saveImageToStorage(context, bitmap, fileName)
            Toast.makeText(context, "Foto guardada localmente", Toast.LENGTH_SHORT).show()
        }
    }
    var hasCameraPermission by remember { mutableStateOf(false) }
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }


    val userNameState = remember { mutableStateOf("") }
    val userLastNameState = remember { mutableStateOf("") }
    val userEmailState = remember { mutableStateOf("") }
    val fechaNacimientoState = remember { mutableStateOf("") }
    var isEditable by remember { mutableStateOf(false) }

    // Obtener referencia a la base de datos
    val database = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Obtener el nombre del usuario desde Firebase Database
    LaunchedEffect(Unit) {
        currentUser?.uid?.let { uid ->
            database.child("usuarios").child(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val nombre = snapshot.child("nombre").value as? String
                    val apellido = snapshot.child("apellido").value as? String
                    val correo = snapshot.child("correo").value as? String
                    val fechaNacimiento = snapshot.child("fechaNacimiento").value as? String

                    if (!nombre.isNullOrEmpty()) {
                        userNameState.value = nombre
                    }
                    if (!apellido.isNullOrEmpty()) {
                        userLastNameState.value = apellido
                    }
                    if (!correo.isNullOrEmpty()) {
                        userEmailState.value = correo
                    }
                    if (!fechaNacimiento.isNullOrEmpty()) {
                        fechaNacimientoState.value = fechaNacimiento
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Fondo claro
            .verticalScroll(rememberScrollState()) // Scroll general
            .testTag("permission_request")
            .clickable {
                if (hasCameraPermission) {
                    launcher.launch()
                } else {
                    permissionsLauncher.launch(Manifest.permission.CAMERA)
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp), // Margen superior
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado con flecha de retroceso y título
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(30.dp)
                        .testTag("back_button")
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 30.sp,
                    modifier = Modifier.testTag("profile_title")
                )

                Spacer(modifier = Modifier.size(30.dp))
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Foto de perfil
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.Gray, CircleShape)
                    .clickable {
                        if (hasCameraPermission) {
                            launcher.launch()
                        } else {
                            permissionsLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (savedImage != null) {
                    Image(
                        bitmap = savedImage!!.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(80.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Campos de texto no editables
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = userNameState.value,
                    onValueChange = { if (isEditable) userNameState.value = it },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("name_field"),

                    readOnly = !isEditable
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = userLastNameState.value,
                    onValueChange = { if (isEditable) userLastNameState.value = it },
                    label = { Text("Apellido") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("last_name_field"),
                    readOnly = !isEditable
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = userEmailState.value,
                    onValueChange = { if (isEditable) userEmailState.value = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("email_field"),
                    readOnly = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fechaNacimientoState.value,
                    onValueChange = { if (isEditable) fechaNacimientoState.value = it },
                    label = { Text("Fecha de nacimiento") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dob_field"),
                    readOnly = !isEditable
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            if (isEditable) {
                Button(
                    onClick = {
                        // Guardar cambios en la base de datos de Firebase
                        currentUser?.uid?.let { uid ->
                            val updatedUser = mapOf(
                                "nombre" to userNameState.value,
                                "apellido" to userLastNameState.value,
                                "fechaNacimiento" to fechaNacimientoState.value
                            )
                            database.child("usuarios").child(uid)
                                .updateChildren(updatedUser)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Guardar cambios")
                }
            }

            // Botón para habilitar la edición
            if (!isEditable) {
                Button(
                    onClick = { isEditable = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Editar")
                }
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

    }

}

// Función para guardar la imagen localmente
private fun saveImageToStorage(context: Context, bitmap: Bitmap, fileName: String) {
    try {
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Log.d("LocalStorage", "Imagen guardada en: ${file.absolutePath}")
    } catch (e: IOException) {
        Log.e("LocalStorage", "Error al guardar la imagen", e)
    }
}

// Función para cargar la imagen localmente
fun loadImageFromStorage(context: Context, fileName: String): Bitmap? {
    return try {
        val file = File(context.filesDir, fileName)
        if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("LocalStorage", "Error al cargar la imagen", e)
        null
    }
}