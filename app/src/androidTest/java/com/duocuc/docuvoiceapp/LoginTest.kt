package com.duocuc.docuvoiceapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duocuc.docuvoiceapp.components.Login
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun test_LoginWithValidCredentials() {
        // Establecer el contenido en la prueba
        composeTestRule.setContent {
            Login(
                onLoginClick = { /* Lógica para verificar la navegación después de login */ },
                onRegisterClick = { /* Lógica para navegar a registro */ },
                onForgotPasswordClick = { /* Lógica para navegar a recuperar contraseña */ }
            )
        }

        // Interactuar con los campos: correo y contraseña
        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextInput("test@example.com")

        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("123456")

        // Clic en el botón "Iniciar Sesión"
        composeTestRule.onNodeWithTag("login_button")
            .assertIsDisplayed()  // Asegura que el botón sea visible
            .performClick()

        // Espera un poco para asegurar que la animación se muestra antes de verificar
        composeTestRule.waitForIdle()  // Asegura que todas las operaciones anteriores han terminado

        // Verifica que la animación de carga se está mostrando usando testTag
        composeTestRule.onNodeWithTag("loading_animation")
            .assertIsDisplayed() // Asegura que la animación está visible

        // Espera un tiempo para simular que la animación se haya ejecutado
        composeTestRule.mainClock.advanceTimeBy(3000) // Avanza el reloj principal para simular el tiempo de espera (ajusta si es necesario)

        // Verifica que la animación de carga desaparezca después de un tiempo
        composeTestRule.onNodeWithTag("loading_animation")
            .assertDoesNotExist() // Asegura que la animación ya no esté visible

        // Puedes verificar que después de un tiempo, el onLoginClick se ejecuta
        composeTestRule.waitForIdle()
    }

    @Test
    fun test_LoginWithEmptyFieldsShowsError() {
        // Establecer el contenido en la prueba
        composeTestRule.setContent {
            Login(
                onLoginClick = { /* Simula el éxito de login */ },
                onRegisterClick = { /* Lógica para navegar a registro */ },
                onForgotPasswordClick = { /* Lógica para navegar a recuperar contraseña */ }
            )
        }

        // Esperar a que el contenido se renderice
        composeTestRule.waitForIdle()

        // Clic en el botón con el testTag
        composeTestRule.onNodeWithTag("login_button")
            .assertIsDisplayed()  // Asegura que el botón sea visible
            .performClick()       // Realiza el clic en el botón

        // Verificar que el mensaje de error se muestra
        composeTestRule.onNodeWithText("Correo o contraseña incorrectos.")
            .assertIsDisplayed()  // Asegura que el error sea visible
    }

    @Test
    fun test_NavigateToRegisterScreen() {
        // Establecer el contenido en la prueba
        composeTestRule.setContent {
            Login(
                onLoginClick = { /* Lógica para verificar la navegación después de login */ },
                onRegisterClick = { /* Lógica para navegar a registro */ },
                onForgotPasswordClick = { /* Lógica para navegar a recuperar contraseña */ }
            )
        }

        // Clic en el texto "¿No tienes cuenta? Regístrate"
        composeTestRule.onNodeWithText("¿No tienes cuenta? Regístrate").performClick()

        // Verificar que la pantalla de registro se muestra
        composeTestRule.onNodeWithTag("registro")
            .assertIsDisplayed()  // Asegura que el botón sea visible
            .performClick()
    }

    @Test
    fun test_NavigateToForgotPasswordScreen() {
        // Establecer el contenido en la prueba
        composeTestRule.setContent {
            Login(
                onLoginClick = { /* Lógica para verificar la navegación después de login */ },
                onRegisterClick = { /* Lógica para navegar a registro */ },
                onForgotPasswordClick = { /* Lógica para navegar a recuperar contraseña */ }
            )
        }

        // Clic en el texto "¿Olvidaste tu contraseña?"
        composeTestRule.onNodeWithText("¿Olvidaste tu contraseña?").performClick()

        // Espera a que la navegación se complete y el contenido se cargue
        composeTestRule.waitForIdle()

        // Verificar que la pantalla de recuperación de contraseña se muestra
        // Aquí puedes usar el texto o el identificador adecuado de la pantalla de recuperación de contraseña
        composeTestRule.onNodeWithText("¿Olvidaste tu contraseña?").assertIsDisplayed()
    }

    @Test
    fun test_ShowLoadingAnimationWhenLoggingIn() {
        // Establecer el contenido en la prueba
        composeTestRule.setContent {
            Login(
                onLoginClick = { /* Simula el éxito de login */ },
                onRegisterClick = { /* Lógica para navegar a registro */ },
                onForgotPasswordClick = { /* Lógica para navegar a recuperar contraseña */ }
            )
        }

        // Simular un clic en "Iniciar sesión" con datos válidos
        composeTestRule.onNodeWithTag("login_button").performClick()

        // Interactuar con el campo de correo electrónico
        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextInput("test@example.com")

        // Interactuar con el campo de contraseña
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("123456")

        // Hacer clic en el botón de iniciar sesión
        composeTestRule.onNodeWithTag("login_button").performClick()

        // Verificar que la animación de carga se muestra mientras se realiza la acción
        composeTestRule.onNodeWithTag("loading_animation").assertIsDisplayed()

        // Esperar a que la animación de carga desaparezca (después de 3 segundos o el callback de login)
        composeTestRule.waitForIdle()
    }
}