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
import com.example.gamehub.models.User
import com.example.gamehub.repository.PrefsRepository
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

        // Crear usuario admin si no existe
        createDefaultAdminIfNeeded()

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

    private fun createDefaultAdminIfNeeded() {
        val users = prefsRepository.getUsers()
        val adminExists = users.any { it.role.equals("admin", ignoreCase = true) }

        if (!adminExists) {
            val adminUser = User(
                id = 1,
                name = "Administrador",
                email = "admin@gamehub.com",
                password = "admin123",
                role = "admin"
            )
            prefsRepository.saveUser(adminUser)
            Toast.makeText(this, "Usuario administrador creado automáticamente", Toast.LENGTH_SHORT).show()
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
            prefsRepository.setActiveUserEmail(foundUser.email)

            if (foundUser.role.equals("admin", ignoreCase = true)) {
                Toast.makeText(this, "Bienvenido, Admin ${foundUser.name}!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "¡Bienvenido de nuevo, ${foundUser.name}!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            finish()
        } else {
            Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}
