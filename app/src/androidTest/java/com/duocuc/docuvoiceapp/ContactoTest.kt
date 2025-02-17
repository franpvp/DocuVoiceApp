package com.duocuc.docuvoiceapp

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duocuc.docuvoiceapp.components.Contacto
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ContactoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun test_FormVisibility() {
        composeTestRule.setContent {
            Contacto(navController = rememberNavController(), onContactSubmit = {})
        }

        // Verificar que los campos del formulario sean visibles
        composeTestRule.onNodeWithText("Nombre").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo Electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mensaje").assertIsDisplayed()
        composeTestRule.onNodeWithText("Enviar").assertIsDisplayed()
    }

    @Test
    fun test_ShowErrorWhenFieldsAreEmpty() {
        composeTestRule.setContent {
            Contacto(navController = rememberNavController(), onContactSubmit = {})
        }

        // Hacer clic en el botón "Enviar" sin completar los campos
        composeTestRule.onNodeWithText("Enviar").performClick()

        // Verificar que el mensaje de error se muestra
        composeTestRule.onNodeWithText("Por favor, completa todos los campos correctamente.").assertIsDisplayed()
    }

    @Test
    fun test_LottieAnimationDisplaysDuringSubmission() {
        composeTestRule.setContent {
            Contacto(navController = rememberNavController(), onContactSubmit = {})
        }

        // Completar los campos del formulario
        composeTestRule.onNodeWithText("Nombre").performTextInput("Juan Pérez")
        composeTestRule.onNodeWithText("Correo Electrónico").performTextInput("juan@example.com")
        composeTestRule.onNodeWithText("Mensaje").performTextInput("Este es un mensaje de prueba")

        // Hacer clic en el botón "Enviar"
        composeTestRule.onNodeWithText("Enviar").performClick()

        // Esperar un momento para asegurarse de que la animación se haya mostrado
        composeTestRule.waitForIdle()

        // Verificar que la animación de Lottie esté visible
        composeTestRule.onNodeWithTag("LottieAnimation").assertIsDisplayed() // Verifica con el TestTag
    }

}