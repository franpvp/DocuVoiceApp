package com.duocuc.docuvoiceapp

import RecuperarContrasenaForm
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RecuperarContrasenaTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_InitialState() {
        // Verifica que el título de la pantalla aparece
        onView(withText("Recuperar Contraseña")).check(matches(isDisplayed()))

        // Verifica que el campo de correo existe
        onView(withHint("Correo Electrónico")).check(matches(isDisplayed()))

        // Verifica que el botón de confirmar está presente
        onView(withText("Confirmar")).check(matches(isDisplayed()))
    }

    @Test
    fun test_ValidEmailSubmission() {
        // Llamada a setContent dentro del test
        composeTestRule.setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "recuperarContrasena") {
                composable("recuperarContrasena") {
                    RecuperarContrasenaForm(navController = navController)
                }
                composable("login") {
                    // Pantalla de login de prueba
                    Text("Login Screen", Modifier.testTag("loginScreen"))
                }
            }
        }

        // Esperar que Compose haya renderizado completamente la UI
        composeTestRule.waitForIdle()

        // Asegurarse de que el campo de texto esté disponible y darle foco
        composeTestRule.onNodeWithText("Correo Electrónico", useUnmergedTree = true)
            .assertIsDisplayed()   // Verifica que el campo de texto está visible
            .performClick()        // Haz clic en el campo para darle foco

        // Ingresar el correo electrónico
        composeTestRule.onNodeWithText("Correo Electrónico", useUnmergedTree = true)
            .performTextInput("test@example.com") // Ingresa el texto

        // Verificar que el texto se haya ingresado correctamente
        composeTestRule.onNodeWithText("test@example.com", useUnmergedTree = true)
            .assertExists()  // Verifica que el texto ingresado existe en el campo de texto

        // Clic en el botón Confirmar
        composeTestRule.onNodeWithText("Confirmar").performClick()

        // Esperar un momento para asegurarse de que la navegación ha ocurrido
        composeTestRule.waitForIdle()

        // Verificar que la pantalla de login se muestra
        composeTestRule.onNodeWithTag("loginScreen").assertIsDisplayed()
    }

    @Test
    fun test_EmptyEmailShowsError() {
        // Llama a setContent solo una vez
        composeTestRule.setContent {
            // Crea el NavController y pasa a tu Composable
            val navController = rememberNavController()
            RecuperarContrasenaForm(navController = navController)
        }

        // Clic en el botón Confirmar
        composeTestRule.onNodeWithText("Confirmar").performClick()

        // Verifica que el mensaje de error se muestra
        composeTestRule.onNodeWithText("Ingresa un correo válido.")
            .assertIsDisplayed()
    }

    @Test
    fun test_BackButtonNavigatesBack() {
        // Clic en el botón de volver
        onView(withContentDescription("Volver")).perform(click())

        // Verificar que la pantalla anterior se carga (podría necesitar ajuste según navegación)
        onView(withText("Login")).check(matches(isDisplayed()))
    }
}