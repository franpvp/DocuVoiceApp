package com.example.semana01.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.semana01.R

@Composable
fun Notificaciones() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .verticalScroll(rememberScrollState())
            .zIndex(-1f)
    ) {
        Spacer(modifier = Modifier.height(400.dp))

        // Tarjetas con funcionalidades
        CardNotificacion(
            title = "Reconocimiento de Texto (OCR)",
            description = "Extrae texto de documentos como PDFs e imágenes para convertirlo en audio o texto accesible.",
            imageResId = R.drawable.ic_ocr,  // Agregar imagen relevante
            onClick = { /* Acción al hacer clic, por ejemplo, navegar a una pantalla de OCR */ }
        )
        CardNotificacion(
            title = "Clasificación Automática",
            description = "Organiza documentos en categorías como contratos, recibos y más, para facilitar su gestión.",
            imageResId = R.drawable.ic_classification,  // Imagen para clasificación
            onClick = { /* Acción al hacer clic */ }
        )
        CardNotificacion(
            title = "Accesibilidad Total",
            description = "Interfaz optimizada para lectores de pantalla y comandos de voz, garantizando la independencia del usuario.",
            imageResId = R.drawable.ic_accessibility,  // Imagen de accesibilidad
            onClick = { /* Acción al hacer clic */ }
        )
        CardNotificacion(
            title = "Traducción Automática",
            description = "Convierte documentos en otros idiomas a tu idioma preferido para una comprensión más fácil.",
            imageResId = R.drawable.ic_translation,  // Imagen para traducción
            onClick = { /* Acción al hacer clic */ }
        )
        CardNotificacion(
            title = "Validación de Firmas Digitales",
            description = "Permite verificar la autenticidad de documentos con firmas digitales.",
            imageResId = R.drawable.ic_signature,  // Imagen de firma digital
            onClick = { /* Acción al hacer clic */ }
        )
        CardNotificacion(
            title = "Seguridad y Privacidad",
            description = "Encriptación de datos y autenticación segura para proteger tu información personal.",
            imageResId = R.drawable.ic_security,  // Imagen de seguridad
            onClick = { /* Acción al hacer clic */ }

        )
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun CardNotificacion(
    title: String,
    description: String,
    imageResId: Int,
    onClick: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(imageResId))
    val progress by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),

        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Animación Lottie en lugar de la imagen
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(50.dp) // Puedes ajustar el tamaño de la animación
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Título y descripción
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}