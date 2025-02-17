package com.duocuc.docuvoiceapp.components

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duocuc.docuvoiceapp.R
import java.util.Locale

@Composable
fun MisMensajes(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(1) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MisMensajesPrefs", Context.MODE_PRIVATE)
    var mensajesGuardados by remember {
        mutableStateOf(sharedPreferences.getStringSet("mensajes", mutableSetOf())?.toList() ?: listOf())
    }

    // Inicializar TextToSpeech
    var textToSpeech by remember {
        mutableStateOf<TextToSpeech?>(null)
    }

    LaunchedEffect(Unit) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale("es", "ES") // Configura el idioma a español
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // Encabezado con el botón de retroceder y el título centrado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp) // Espaciado debajo del encabezado
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Botón de retroceder
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Título centrado
                    Text(
                        text = "Mis Mensajes",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                    )

                    // Espaciador invisible para mantener el título centrado
                    Spacer(modifier = Modifier.width(40.dp)) // Igual al tamaño del botón
                }
            }
        }

        // Lista de mensajes guardados
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp)
                .verticalScroll(rememberScrollState())

        ) {
            mensajesGuardados.forEachIndexed { index, mensaje ->
                CardMensajes(
                    title = "Mensaje ${index + 1}",
                    description = mensaje,
                    onTextChange = { nuevoTexto ->
                        val nuevaLista = mensajesGuardados.toMutableList()
                        nuevaLista[index] = nuevoTexto
                        mensajesGuardados = nuevaLista
                        // Guardar cambios
                        sharedPreferences.edit().putStringSet("mensajes", nuevaLista.toSet()).apply()
                    },
                    onDelete = {
                        val nuevaLista = mensajesGuardados.toMutableList()
                        nuevaLista.removeAt(index)
                        mensajesGuardados = nuevaLista
                        // Guardar cambios
                        sharedPreferences.edit().putStringSet("mensajes", nuevaLista.toSet()).apply()
                    },
                    onClick = {
                        textToSpeech?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                )
            }
        }

        // Tab en la parte inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                //.padding(horizontal = 6.dp)
                //.clip(RoundedCornerShape(24.dp))
                .background(Color.Black)
                //.padding(vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    tint = if (selectedTab == 0) Color.White else Color.Gray,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            selectedTab = 0
                            navController.navigate("home")
                        }
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


@Composable
fun CardMensajes(
    title: String,
    description: String,
    onTextChange: (String) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    var textoEditable by remember { mutableStateOf(description) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF1F2024))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(id = R.drawable.borrar),
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = textoEditable,
                    onValueChange = { nuevoTexto ->
                        textoEditable = nuevoTexto
                        onTextChange(nuevoTexto) // Guardar cambios
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onClick,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "Reproducir")
                }
            }
        }
    }
}