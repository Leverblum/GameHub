package com.example.gamehub.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns // <-- NUEVO IMPORT para la validación de email
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamehub.R
import com.example.gamehub.models.User
import com.example.gamehub.repository.PrefsRepository

class RegisterActivity : AppCompatActivity() {

    // 1. Declarar las vistas y el repositorio
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvGoToLogin: TextView
    private lateinit var prefsRepository: PrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 2. Inicializar vistas y repositorio
        initializeViews()
        prefsRepository = PrefsRepository(this)

        // 3. Configurar listeners
        setupListeners()
    }

    private fun initializeViews() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvGoToLogin = findViewById(R.id.tvGoToLogin)
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            handleRegistration()
        }

        tvGoToLogin.setOnClickListener {
            // Volver a LoginActivity
            finish()
        }
    }

    private fun handleRegistration() {
        // 4. Obtener los datos de los EditText
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim().lowercase()
        val password = etPassword.text.toString().trim()

        // --- INICIO DE LA NUEVA LÓGICA DE VALIDACIÓN ---

        // Validar campos vacíos
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "El formato del correo no es válido"
            Toast.makeText(this, "Por favor, introduce un correo electrónico válido.", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar longitud de la contraseña
        val minPasswordLength = 6
        if (password.length < minPasswordLength) {
            etPassword.error = "La contraseña debe tener al menos $minPasswordLength caracteres"
            Toast.makeText(this, "La contraseña es demasiado corta (mínimo $minPasswordLength caracteres).", Toast.LENGTH_LONG).show()
            return
        }

        // --- FIN DE LA NUEVA LÓGICA DE VALIDACIÓN ---

        // 5. Lógica de registro usando el repositorio
        val users = prefsRepository.getUsers()
        val userExists = users.any { it.email.equals(email, ignoreCase = true) }

        if (userExists) {
            Toast.makeText(this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show()
        } else {
            // Crear el nuevo usuario y añadirlo a la lista
            val newUser = User(id = email, name = name, email = email, password = password)
            users.add(newUser)

            // Guardar la lista de usuarios actualizada en SharedPreferences
            prefsRepository.saveUsers(users)

            Toast.makeText(this, "Registro exitoso. Ahora inicia sesión.", Toast.LENGTH_LONG).show()

            // Redirigir a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Cierra RegisterActivity
        }
    }
}
