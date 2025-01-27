package com.example.semana01.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.semana01.R

@Composable
fun Home(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                imageUri = uri
            }
        }
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)) {

        Box(
            modifier = Modifier
                .requiredWidth(710.dp)
                .fillMaxHeight(0.4f)
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 500.dp,
                        bottomEnd = 500.dp
                    )
                )
                .background(Color(0xFF13678A))
                .align(Alignment.TopCenter)
                .offset(y = 100.dp)
                .zIndex(1f)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 35.dp, y = 40.dp)
                .zIndex(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, // Centra verticalmente
                horizontalArrangement = Arrangement.Start, // Alineación hacia la izquierda
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Imagen de perfil
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUri),
                        contentDescription = "Imagen de perfil seleccionada",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { launcher.launch("image/*") }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "Imagen de perfil predeterminada",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(10.dp)
                            .clickable { launcher.launch("image/*") }
                    )
                }

                Spacer(modifier = Modifier.width(12.dp)) // Espacio entre la imagen y el texto

                // Texto "Hola, User"
                Text(
                    text = "Hola, User", // Puedes reemplazar "User" por el nombre dinámicamente
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge, // Ajusta el estilo de texto según desees
                    modifier = Modifier
                        .padding(start = 10.dp) // Ajusta el espacio entre la imagen y el texto
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(Color.Transparent)
                .zIndex(1f)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 50.dp)
                    .offset(y = 100.dp)

            ) {
                items(3) { index ->
                    val (animationResId, cardText) = when (index) {
                        0 -> Pair(R.raw.text, "Escribir Texto")
                        1 -> Pair(R.raw.translation, "Traducción Automática")
                        2 -> Pair(R.raw.voice, "Conversión de Voz a Texto")
                        else -> Pair(R.raw.loading, "Tarjeta Default") // Animación por defecto
                    }
                    Card(
                        modifier = Modifier
                            .width(300.dp) // Estableciendo el ancho del Card
                            .height(220.dp) // Estableciendo la altura del Card
                            .padding(8.dp)  // Padding de la tarjeta
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF1F2024)) // Fondo solo en el área de contenido
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)  // Padding interno de la tarjeta
                            ) {
                                // Cargar una animación diferente para cada card
                                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(animationResId))
                                val progress by rememberInfiniteTransition().animateFloat(
                                    initialValue = 0f,
                                    targetValue = 1f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(durationMillis = 5000, easing = LinearEasing),
                                        repeatMode = RepeatMode.Restart
                                    )
                                )

                                LottieAnimation(
                                    composition = composition,
                                    progress = { progress },
                                    modifier = Modifier
                                        .size(130.dp)
                                        .align(Alignment.CenterHorizontally)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Cambiar el texto dependiendo de la animación
                                Text(
                                    text = cardText,
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(Color.Transparent)
                .verticalScroll(rememberScrollState())
                .zIndex(-1f)
        ) {
            Spacer(modifier = Modifier.height(400.dp))

            // Tarjetas con funcionalidades
            FuncionalidadCard(
                title = "Reconocimiento de Texto (OCR)",
                description = "Extrae texto de documentos como PDFs e imágenes para convertirlo en audio o texto accesible.",
                imageResId = R.drawable.file,  // Agregar imagen relevante
                onClick = {  }
            )
            FuncionalidadCard(
                title = "Modificar Contraste Interfaz",
                description = "Extrae texto de documentos como PDFs e imágenes para convertirlo en audio o texto accesible.",
                imageResId = R.drawable.interfaz,  // Agregar imagen relevante
                onClick = {  }
            )
            FuncionalidadCard(
                title = "Pedir Ayuda",
                description = "Genera un sonido para solicitar ayuda",
                imageResId = R.raw.help,
                onClick = { /* Acción al hacer clic */ }
            )
            Spacer(modifier = Modifier.height(64.dp))
        }
        // Tab en la parte inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Lo posiciona en la parte inferior del `Box` principal
                .padding(horizontal = 6.dp) // Espaciado alrededor
                .clip(RoundedCornerShape(24.dp)) // Bordes redondeados
                .background(Color.Black)
                .padding(vertical = 6.dp) // Espaciado interno del tab
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

// Composable para la tarjeta de funcionalidad con imagen
@Composable
fun FuncionalidadCard(
    title: String,
    description: String,
    imageResId: Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick) // Permite que toda la tarjeta sea clickeable
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)  // Ajuste de padding para mejorar la presentación
        ) {
            // Animación Lottie o imagen estática
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(5.dp)
            ) {
                if (imageResId in listOf(R.raw.text, R.raw.translation, R.raw.voice, R.raw.help)) {
                    // Animación Lottie
                    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(imageResId))
                    val progress by rememberInfiniteTransition().animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 1500, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Imagen estática
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Texto de la tarjeta
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}