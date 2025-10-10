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
// Importamos AdminActivity, que será el contenedor del admin
import com.example.gamehub.ui.admin.AdminActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvGoToRegister: TextView
    private lateinit var prefsRepository: PrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        prefsRepository = PrefsRepository(this)
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
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin() {
        val email = etEmail.text.toString().trim().lowercase()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "El formato del correo no es válido"
            Toast.makeText(this, "Por favor, introduce un correo electrónico válido.", Toast.LENGTH_SHORT).show()
            return
        }

        val users = prefsRepository.getUsers()
        val foundUser = users.find { it.email.equals(email, ignoreCase = true) && it.password == password }

        if (foundUser != null) {
            // Usuario y contraseña correctos. Primero guardamos su sesión.
            prefsRepository.setActiveUserEmail(foundUser.email)

            // ================== INICIO DEL CAMBIO ==================
            // VERIFICACIÓN DE ROL PARA REDIRECCIÓN
            if (foundUser.role.equals("admin", ignoreCase = true)) {
                // Si el usuario es 'admin', lo enviamos a AdminActivity
                Toast.makeText(this, "Bienvenido, Admin ${foundUser.name}!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)

            } else {
                // Si es un usuario normal, lo enviamos a MainActivity
                Toast.makeText(this, "¡Bienvenido de nuevo, ${foundUser.name}!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            // Cierra LoginActivity para que el usuario no pueda volver atrás
            finish()
            // =================== FIN DEL CAMBIO ====================

        } else {
            // Credenciales incorrectas
            Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}
