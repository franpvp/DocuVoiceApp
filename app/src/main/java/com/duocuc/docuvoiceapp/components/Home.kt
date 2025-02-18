package com.duocuc.docuvoiceapp.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.duocuc.docuvoiceapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.speech.tts.TextToSpeech
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import java.io.InputStream
import java.util.Locale

@Composable
fun Home(navController: NavController) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var textInput by remember { mutableStateOf("") }
    val sharedPreferences = context.getSharedPreferences("MisMensajesPrefs", Context.MODE_PRIVATE)
    val sharedPreferencesFonts = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
    var isDialogVisible by remember { mutableStateOf(false) }
    var isSpeechDialogVisible by remember { mutableStateOf(false) }
    var speechText by remember { mutableStateOf("") }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val isListening = remember { mutableStateOf(false) }
    var fontSize by remember {
        mutableStateOf(sharedPreferencesFonts.getFloat("fontSize", 20f))
    }
    val fileName = "profile_image.jpg"
    var profileImage by remember { mutableStateOf<Bitmap?>(loadImageFromStorage(context, fileName)) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    // Inicializar TextToSpeech
    var textToSpeech by remember {
        mutableStateOf<TextToSpeech?>(null)
    }
    var pdfText by remember { mutableStateOf("") }
    var isPdfDialogVisible by remember { mutableStateOf(false) }

    var isHighContrast by remember { mutableStateOf(false) }
    val originalBackgroundColor = Color(0xFF13678A)
    val highContrastBackgroundColor = Color(0xFF000000)

    // Establecer el esquema de colores en función del estado isHighContrast
    val colors = if (isHighContrast) {
        darkColorScheme() // Alterna a un esquema de colores oscuro de alto contraste
    } else {
        lightColorScheme() // Mantén el esquema claro por defecto
    }

    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
            isGranted ->
        if (isGranted) {
            speechRecognizer.startListening(recognizerIntent)
        } else {
            Toast.makeText(context, "Permiso de reconocimiento de voz denegado", Toast.LENGTH_SHORT).show()
        }
    }
    val recognizerListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Log.d("SpeechRecognizer", "Listo para escuchar")
            isListening.value = true
            speechText = "Escuchando..."
        }

        override fun onBeginningOfSpeech() {
            Log.d("SpeechRecognizer", "Escuchando...")
        }

        override fun onRmsChanged(p0: Float) {
        }

        override fun onBufferReceived(p0: ByteArray?) {
        }

        override fun onEndOfSpeech() {
            Log.d("SpeechRecognizer", "Fin del discurso")
            isListening.value = false
        }

        override fun onError(error: Int) {
            Log.d("SpeechRecognizer", "Error al escuchar: $error")
            isListening.value = false
            speechText = "Error en el reconocimiento. Intente de nuevo."
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                speechText = matches[0] // Captura el primer resultado
            } else {
                speechText = "No se detectó voz. Intente de nuevo."
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                speechText = matches[0] // Muestra el texto parcial mientras habla
            }
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
        }

    }

    LaunchedEffect(speechRecognizer) {
        speechRecognizer.setRecognitionListener(recognizerListener)
    }

    fun extractTextFromPdf(context: Context, uri: Uri): String? {
        try {
            val contentResolver = context.contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(uri)

            // Crea el PdfDocument con el inputStream
            val pdfDocument = PdfDocument(PdfReader(inputStream))

            val pdfText = StringBuilder()

            // Recorre las páginas del PDF (usando pdfDocument.numberOfPages)
            for (i in 1..pdfDocument.numberOfPages) {
                val page = pdfDocument.getPage(i)
                pdfText.append(PdfTextExtractor.getTextFromPage(page)).append("\n")
            }

            pdfDocument.close()

            return pdfText.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                val extractedText = extractTextFromPdf(context, uri)
                if (extractedText != null) {
                    pdfText = extractedText
                    isPdfDialogVisible = true // Mostrar el texto extraído en un Dialog
                } else {
                    Toast.makeText(context, "No se pudo extraer el texto del PDF", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    fun saveFontSize(size: Float) {
        sharedPreferences.edit().putFloat("fontSize", size).apply()
    }

    fun reproducirSonido(context: Context) {
        if (mediaPlayer?.isPlaying == true) {
            // Si el sonido está reproduciéndose, detenerlo
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        } else {
            // Si el sonido no se está reproduciendo, iniciarlo
            mediaPlayer = MediaPlayer.create(context, R.raw.sonido_emergencia).apply {
                setOnCompletionListener { mp ->
                    mp.release()  // Libera el MediaPlayer cuando termina
                    mediaPlayer = null // Evita referencias innecesarias
                }
                start()
            }
        }
    }

    // Función para guardar mensaje localmente
    fun guardarMensaje(context: Context, message: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MisMensajesPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("mensaje_guardado", message).apply()
    }

    val userNameState = remember { mutableStateOf("") }
    // Obtener referencia a la base de datos
    val database = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Obtener el nombre del usuario desde Firebase Database
    LaunchedEffect(Unit) {
        currentUser?.uid?.let { uid ->
            database.child("usuarios").child(uid).child("nombre")
                .get()
                .addOnSuccessListener { snapshot ->
                    val nombre = snapshot.value as? String
                    if (nombre != null) {
                        userNameState.value = nombre
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al obtener el nombre", Toast.LENGTH_SHORT).show()
                }
        }
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



    MaterialTheme(colorScheme = colors) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
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
                    .background(if (isHighContrast) highContrastBackgroundColor else originalBackgroundColor)
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
                    horizontalArrangement = Arrangement.SpaceBetween, // Alineación hacia la izquierda
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Imagen de perfil
                    if (profileImage != null) {
                        Image(
                            bitmap = profileImage!!.asImageBitmap(),
                            contentDescription = "Imagen de perfil seleccionada",
                            modifier = Modifier
                                .size(55.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable {
                                    // Navegar al perfil para cambiar la foto
                                    navController.navigate("perfil")
                                }
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
                                .clickable {
                                    navController.navigate("perfil")
                                }
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp)) // Espacio entre la imagen y el texto

                    Text(
                        text = "Hola, ${userNameState.value}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth()
                    ) {
                        IconButton(
                            onClick = {
                                if (fontSize < 25f) {
                                    fontSize += 2f
                                    saveFontSize(fontSize)
                                }
                            },
                            modifier = Modifier
                                .size(48.dp) // Tamaño del botón
                                .background(Color.White, CircleShape) // Fondo circular
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.aumentar_fuente),
                                contentDescription = "Aumentar letra",
                                modifier = Modifier.size(24.dp) // Tamaño del ícono
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp)) // Espacio entre botones

                        IconButton(
                            onClick = {
                                if (fontSize > 15f) {
                                    fontSize -= 2f
                                    saveFontSize(fontSize)
                                }
                            },
                            modifier = Modifier
                                .size(48.dp) // Tamaño del botón
                                .background(Color.White, CircleShape) // Fondo circular
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.disminuir_fuente),
                                contentDescription = "Disminuir letra",
                                modifier = Modifier.size(24.dp) // Tamaño del ícono
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .zIndex(1f)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .offset(y = 100.dp)

                ) {
                    items(3) { index ->
                        val (animationResId, cardText) = when (index) {
                            0 -> Pair(R.raw.text, "Escribir Texto")
                            1 -> Pair(R.raw.voice, "Conversión de Voz a Texto")
                            2 -> Pair(R.raw.translation, "Traducción Automática")
                            else -> Pair(R.raw.loading, "Tarjeta Default") // Animación por defecto
                        }
                        Card(
                            modifier = Modifier
                                .width(300.dp) // Tamaño más manejable
                                .padding(8.dp)
                                .clickable {
                                    if (index == 0) {
                                        isDialogVisible = true
                                    }
                                    if (index == 1) {
                                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        isSpeechDialogVisible = true
                                    }
                                },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Mejora visual
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF1F2024))
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Cargar animación Lottie
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
                                    modifier = Modifier.size(120.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = cardText,
                                    color = Color.White,
                                    fontSize = fontSize.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))
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
                    imageResId = R.drawable.file,
                    fontSize = fontSize,
                    onClick = { launcher.launch("application/pdf") } // Abre el selector de archivo PDF
                )
                FuncionalidadCard(
                    title = "Modificar Contraste Interfaz",
                    description = "Cambia el contraste de la interfaz de la aplicación",
                    imageResId = R.drawable.interfaz, // Asegúrate de tener esta imagen en tus recursos
                    fontSize = fontSize, // Tamaño de fuente
                    onClick = {
                        // Cambiar el estado para alternar el contraste
                        isHighContrast = !isHighContrast
                    }
                )
                FuncionalidadCard(
                    title = "Pedir Ayuda",
                    description = "Genera un sonido para solicitar ayuda",
                    imageResId = R.raw.help,
                    fontSize = fontSize,
                    onClick = { reproducirSonido(context) },
                )
                Spacer(modifier = Modifier.height(64.dp))
            }
            if (isPdfDialogVisible) {
                Dialog(onDismissRequest = { isPdfDialogVisible = false }) {
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = pdfText,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 50.dp), // Añadir padding para separar el texto del botón
                                fontSize = fontSize.sp
                            )

                            // Espaciador para empujar el botón a la parte inferior
                            Spacer(modifier = Modifier.weight(1f))

                            Button(
                                onClick = {
                                    textToSpeech?.speak(pdfText, TextToSpeech.QUEUE_FLUSH, null, null) // Reproducir texto en audio
                                    // Aquí no se cierra el Dialog
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow, // Usamos el ícono predeterminado de Jetpack Compose
                                    contentDescription = "Reproducir",
                                    tint = MaterialTheme.colorScheme.tertiaryContainer
                                )
                            }
                        }
                    }
                }
            }
            // Tab en la parte inferior
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
            // Diálogo
            if (isDialogVisible) {
                Dialog(onDismissRequest = { isDialogVisible = false }) {
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(30.dp)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Spacer(modifier = Modifier.height(16.dp))

                            // Input field
                            TextField(
                                value = textInput,
                                onValueChange = { textInput = it },
                                placeholder = { Text("Escribe algo...") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Botón para confirmar y reproducir texto
                            Button(
                                onClick = {
                                    // Guardar en SharedPreferences
                                    val editor = sharedPreferences.edit()
                                    val existingMessages = sharedPreferences.getStringSet("mensajes", mutableSetOf()) ?: mutableSetOf()
                                    existingMessages.add(textInput)
                                    editor.putStringSet("mensajes", existingMessages)
                                    editor.apply()

                                    // Reproducir el texto ingresado
                                    textToSpeech?.speak(textInput, TextToSpeech.QUEUE_FLUSH, null, null)

                                    isDialogVisible = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Reproducir")
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
            if(isSpeechDialogVisible) {
                Dialog(onDismissRequest = { isSpeechDialogVisible = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = speechText,
                                fontSize = fontSize.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
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
    fontSize: Float,
    onClick: () -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF1F2024))
    ) {
        Column( // Ahora la tarjeta crece dinámicamente
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {
                // Imagen o animación Lottie
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(5.dp)
                ) {
                    if (imageResId in listOf(R.raw.text, R.raw.translation, R.raw.voice, R.raw.help)) {
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
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Contenedor del texto con peso para expandirse según el contenido
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = title,
                        fontSize = fontSize.sp,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        fontSize = (fontSize - 2).sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun MinimalDialog(
    text: String,
) {
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}



