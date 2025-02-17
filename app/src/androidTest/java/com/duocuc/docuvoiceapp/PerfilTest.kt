package com.duocuc.docuvoiceapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duocuc.docuvoiceapp.components.Perfil
import com.duocuc.docuvoiceapp.components.loadImageFromStorage
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PerfilTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun test_PerfilDisplay() {
        // Establecer el contenido en la prueba
        composeTestRule.setContent {
            Perfil(navController = rememberNavController())
        }

        // Verificar que los campos de texto (Nombre, Apellido, etc.) son visibles
        composeTestRule.onNodeWithText("Nombre").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apellido").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Fecha de nacimiento").assertIsDisplayed()

    }

    @Test
    fun test_ProfilePictureChange() {
        // Establecer el contenido en la prueba
        composeTestRule.setContent {
            Perfil(navController = rememberNavController())
        }

        // Verificar que la solicitud para tomar una foto se activó
        // Puedes verificar si el launcher está ejecutando el código correspondiente
        composeTestRule.onNodeWithContentDescription("Foto de perfil").performClick()
    }

    @Test
    fun test_CameraPermissionRequest() {
        // Establecer el contenido en la prueba
        composeTestRule.setContent {
            Perfil(navController = rememberNavController())
        }

        // Aquí verificamos que el mensaje de solicitud de permisos se muestra
        // Usamos el testTag que agregamos previamente
        composeTestRule.onNodeWithTag("permission_request").assertIsDisplayed()
    }

    @Test
    fun test_SaveProfilePicture() {
        // Establecer el contenido en la prueba
        composeTestRule.setContent {
            Perfil(navController = rememberNavController())
        }

        // Simula que la cámara ha tomado una imagen
        val testBitmap = BitmapFactory.decodeResource(composeTestRule.activity.resources, R.drawable.ic_user)

        // Guarda la imagen usando una función mock de almacenamiento
        val fileName = "profile_image.jpg"
        saveImageToStorage(composeTestRule.activity, testBitmap, fileName)

        // Verificar si la imagen se ha guardado en el almacenamiento (simulado)
        val loadedImage = loadImageFromStorage(composeTestRule.activity, fileName)
        assert(loadedImage != null)  // Asegura que la imagen se ha cargado
    }

    // Función para simular la acción de guardar la imagen (en este caso no se guarda realmente en almacenamiento)
    private fun saveImageToStorage(context: Context, bitmap: Bitmap, fileName: String) {
        try {
            // En lugar de guardar en almacenamiento real, guardamos la imagen en memoria para la prueba
            val file = File(context.cacheDir, fileName)  // Usamos el directorio cache en vez del almacenamiento interno real
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            Log.e("LocalStorage", "Error al guardar la imagen", e)
        }
    }

    // Función para cargar la imagen desde almacenamiento simulado (usando el cache en lugar de almacenamiento real)
    fun loadImageFromStorage(context: Context, fileName: String): Bitmap? {
        return try {
            val file = File(context.cacheDir, fileName)  // Cargar desde cacheDir en lugar de almacenamiento real
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
}