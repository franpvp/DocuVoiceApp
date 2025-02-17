package com.duocuc.docuvoiceapp

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duocuc.docuvoiceapp.components.Home
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun test_HomeScreen_displaysUserName() {
        composeTestRule.setContent {
            Home(navController = rememberNavController())
        }

        // Verifica que el texto de saludo está presente
        composeTestRule.onNodeWithText("Hola", substring = true).assertExists()
    }

    @Test
    fun test_ClickingOnFirstCard_opensDialog() {
        composeTestRule.setContent {
            Home(navController = rememberNavController())
        }

        // Hacer clic en la primera tarjeta (Escribir Texto)
        composeTestRule.onNodeWithText("Escribir Texto").performClick()

        // Verifica que el cuadro de diálogo se muestra
        composeTestRule.onNodeWithText("Escribe algo...").assertExists()
    }

    @Test
    fun test_ClickingOnVoiceCard_opensSpeechDialog() {
        composeTestRule.setContent {
            Home(navController = rememberNavController())
        }

        // Clic en la tarjeta de Conversión de Voz a Texto
        composeTestRule.onNodeWithText("Conversión de Voz a Texto")
            .assertExists("No se encontró el nodo con el texto 'Conversión de Voz a Texto'.")

        // Verifica que el cuadro de diálogo de reconocimiento de voz se muestra
        composeTestRule.waitForIdle() // Puedes intentar esto si el cuadro de diálogo tiene un retardo
        composeTestRule.onNodeWithText("Escuchando...").assertExists("El cuadro de diálogo no apareció.")
    }

    @Test
    fun test_Navigation_to_MensajesScreen() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        composeTestRule.setContent {
            Home(navController = navController)
        }

        composeTestRule.waitForIdle() // Esperar a que la UI termine de renderizar

        // Clic en el ícono de Mensajes
        composeTestRule.onNodeWithContentDescription("Mensajes").performClick()

        // Verifica si se ha navegado a la pantalla correcta
        assertEquals("mensajes", navController.currentDestination?.route)
    }



    @Test
    fun test_SavingMessageLocally() {
        composeTestRule.setContent {
            Home(navController = rememberNavController())
        }

        val message = "Test message"

        // Hacer clic en la tarjeta "Escribir Texto"
        composeTestRule.onNodeWithText("Escribir Texto").performClick()

        // Escribir un mensaje en el campo de texto
        composeTestRule.onNodeWithText("Escribe algo...").performTextInput(message)

        // Hacer clic en el botón para guardar
        composeTestRule.onNodeWithText("Reproducir").performClick()

        // Verifica que el mensaje esté guardado en SharedPreferences
        val sharedPreferences = ApplicationProvider.getApplicationContext<Context>().getSharedPreferences("MisMensajesPrefs", Context.MODE_PRIVATE)
        assertTrue(sharedPreferences.getStringSet("mensajes", mutableSetOf())?.contains(message) == true)
    }

    @Test
    fun test_PlayingSound() {
        composeTestRule.setContent {
            Home(navController = rememberNavController())
        }

        // Hacer clic en el botón de "Pedir Ayuda"
        composeTestRule.onNodeWithText("Pedir Ayuda").performClick()

        // Verifica que el sonido se ha reproducido (deberías comprobar que `MediaPlayer` está siendo usado)
        // Aquí se puede usar un mock de MediaPlayer o verificar si la acción ha sido ejecutada
    }
}