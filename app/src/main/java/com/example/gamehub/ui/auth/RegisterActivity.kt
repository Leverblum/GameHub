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

class RegisterActivity : AppCompatActivity() {

    // --- Vistas obligatorias ---
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    // --- Vistas nuevas y opcionales ---
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etDob: EditText

    private lateinit var btnRegister: Button
    private lateinit var tvGoToLogin: TextView
    private lateinit var prefsRepository: PrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        prefsRepository = PrefsRepository(this)
        setupListeners()
    }

    private fun initializeViews() {
        // --- Campos Obligatorios ---
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        etDob = findViewById(R.id.etDob)

        btnRegister = findViewById(R.id.btnRegister)
        tvGoToLogin = findViewById(R.id.tvGoToLogin)
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            handleRegistration()
        }

        tvGoToLogin.setOnClickListener {
            // Regresa a la actividad anterior (LoginActivity)
            finish()
        }
    }

    private fun handleRegistration() {
        // --- Lectura de campos obligatorios ---
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim().lowercase()
        val password = etPassword.text.toString().trim()

        // --- Lectura de campos opcionales ---
        val phone = etPhone.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val dob = etDob.text.toString().trim()

        // --- Validaciones (solo para campos obligatorios) ---
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nombre, email y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "El formato del correo no es válido"
            Toast.makeText(this, "Por favor, introduce un correo electrónico válido.", Toast.LENGTH_SHORT).show()
            return
        }

        val minPasswordLength = 6
        if (password.length < minPasswordLength) {
            etPassword.error = "La contraseña debe tener al menos $minPasswordLength caracteres"
            Toast.makeText(this, "La contraseña es demasiado corta.", Toast.LENGTH_LONG).show()
            return
        }

        val users = prefsRepository.getUsers().toMutableList()
        val userExists = users.any { it.email.equals(email, ignoreCase = true) }

        if (userExists) {
            Toast.makeText(this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show()
        } else {

            val newUser = User(
                id = (users.maxOfOrNull { it.id } ?: 0) + 1, // Forma segura de generar ID
                name = name,
                email = email,
                password = password, // Recuerda encriptar esto en una app real
                role = "user",
                // Asignamos los valores de los nuevos campos (serán "" si están vacíos)
                phone = phone,
                address = address,
                dateOfBirth = dob,
                avatarUrl = null // El avatar se puede añadir/editar después
            )

            users.add(newUser)
            prefsRepository.saveUsers(users)
            prefsRepository.setActiveUserEmail(email)

            Toast.makeText(this, "¡Bienvenido, $name!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
