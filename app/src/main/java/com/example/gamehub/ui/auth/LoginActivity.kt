package com.example.gamehub.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamehub.MainActivity
import com.example.gamehub.R
import com.example.gamehub.repository.PrefsRepository

class LoginActivity : AppCompatActivity() {

    // 1. Declarar las vistas y el repositorio
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvGoToRegister: TextView
    private lateinit var prefsRepository: PrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 2. Inicializar vistas y repositorio
        initializeViews()
        prefsRepository = PrefsRepository(this)

        // 3. Configurar listeners
        setupListeners()
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvGoToRegister = findViewById(R.id.tvGoToRegister)
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            handleLogin()
        }

        tvGoToRegister.setOnClickListener {
            // Ir a la actividad de registro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin() {
        // 4. Obtener datos de los EditText
        val email = etEmail.text.toString().trim().lowercase()
        val password = etPassword.text.toString().trim()

        // Validar campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "El formato del correo no es válido"
            Toast.makeText(this, "Por favor, introduce un correo electrónico válido.", Toast.LENGTH_SHORT).show()
            return
        }

        // 5. Lógica de login usando el repositorio
        val users = prefsRepository.getUsers()
        val foundUser = users.find { it.email.equals(email, ignoreCase = true) && it.password == password }

        if (foundUser != null) {
            // Usuario y contraseña correctos
            Toast.makeText(this, "¡Bienvenido de nuevo, ${foundUser.name}!", Toast.LENGTH_SHORT).show()

            // ================== INICIO DE LA CORRECCIÓN ==================
            // Usamos el nombre de método correcto: setActiveUserEmail
            prefsRepository.setActiveUserEmail(foundUser.email)
            // =================== FIN DE LA CORRECCIÓN ====================

            // Ir a MainActivity y limpiar la pila de navegación
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish() // Cierra LoginActivity para que el usuario no pueda volver
        } else {
            // Credenciales incorrectas
            Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}
