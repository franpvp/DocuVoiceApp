import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.semana01.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Obtener referencias a los elementos de la interfaz
        val emailEditText: EditText = findViewById(R.id.editTextEmail)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val loginButton: Button = findViewById(R.id.btnLogin)

        // Configurar el comportamiento del botón de login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validar y hacer login
            if (validateUser(email, password)) {
                // Proceder con el inicio de sesión
            } else {
                // Mostrar error
            }
        }
    }

    // Función de validación (puedes implementar la lógica que necesites)
    private fun validateUser(email: String, password: String): Boolean {
        // Implementar la validación aquí
        return email == "usuario@example.com" && password == "contraseña"
    }
}