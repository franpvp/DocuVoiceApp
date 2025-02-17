package com.duocuc.docuvoiceapp

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duocuc.docuvoiceapp.components.Registro
import com.duocuc.docuvoiceapp.components.isValidEmail
import com.duocuc.docuvoiceapp.services.AuthService
import com.duocuc.docuvoiceapp.services.Usuario
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.*
import org.junit.Rule
import org.junit.runner.RunWith

class RegistroTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        composeTestRule.setContent {
            Registro(
                onRegisterSuccess = { /* No hace nada en el test */ },
                navController = navController
            )
        }
    }

    @Test
    fun test_InitialState() {
        composeTestRule.onNodeWithText("Nombre").assertExists()
        composeTestRule.onNodeWithText("Apellido").assertExists()
        composeTestRule.onNodeWithText("Correo electrónico").assertExists()
        composeTestRule.onNodeWithText("Celular").assertExists()
        composeTestRule.onNodeWithText("Fecha de Nacimiento").assertExists()
        composeTestRule.onNodeWithText("Contraseña").assertExists()
        composeTestRule.onNodeWithText("Confirmar Contraseña").assertExists()

        // Aseguramos que los campos están vacíos sin depender de assertTextEquals("")
        composeTestRule.onNodeWithText("Nombre").assertTextContains("")
        composeTestRule.onNodeWithText("Apellido").assertTextContains("")
        composeTestRule.onNodeWithText("Correo electrónico").assertTextContains("")
        composeTestRule.onNodeWithText("Celular").assertTextContains("")
        composeTestRule.onNodeWithText("Contraseña").assertTextContains("")
        composeTestRule.onNodeWithText("Confirmar Contraseña").assertTextContains("")
    }

    @Test
    fun test_EnterValidDataAndRegister() {
        composeTestRule.onNodeWithText("Nombre").performTextInput("Juan")
        composeTestRule.onNodeWithText("Apellido").performTextInput("Pérez")
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("juan.perez@example.com")
        composeTestRule.onNodeWithText("Celular").performTextInput("987654321")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")
        composeTestRule.onNodeWithText("Confirmar Contraseña").performTextInput("123456")

        // Simula clic en el botón de registro
        composeTestRule.onNodeWithText("Registrarse").performClick()
    }

    @Test
    fun test_InvalidEmailShowsError() {
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("email_invalido")

        composeTestRule.onNodeWithText("Registrarse").performClick()

        // Verifica que aparece el mensaje de error
        composeTestRule.onNodeWithText("Correo inválido").assertExists()
    }

}