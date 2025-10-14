package com.example.gamehub.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamehub.MainActivity // <-- NUEVO IMPORT
import com.example.gamehub.R
import com.example.gamehub.repository.PrefsRepository // <-- NUEVO IMPORT
import com.example.gamehub.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {

    // Duración de la pantalla de Splash en milisegundos
    private val SPLASH_DELAY: Long = 2000 // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        createDefaultAdminUserIfNeeded()

        // Inicializamos el repositorio de preferencias
        val prefsRepository = PrefsRepository(this)

        val logoImageView: ImageView? = findViewById(R.id.logoImage)

        // Aplicamos una animación de aparición (fade-in) al logo
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1500
        logoImageView?.startAnimation(fadeIn)

        // Usamos un Handler para esperar y luego decidir a qué actividad ir
        Handler(Looper.getMainLooper()).postDelayed({

            // --- LÓGICA DE VERIFICACIÓN DE SESIÓN ---
            val activeUserEmail = prefsRepository.getActiveUserEmail()

            val destinationActivity = if (activeUserEmail != null) {
                // Si hay un usuario activo, vamos a MainActivity
                MainActivity::class.java
            } else {
                // Si no hay usuario activo, vamos a LoginActivity
                LoginActivity::class.java
            }

            // Creamos un Intent para ir a la actividad de destino
            val intent = Intent(this, destinationActivity)
            startActivity(intent)

            // Finalizamos SplashActivity para que el usuario no pueda volver a ella
            finish()

        }, SPLASH_DELAY)
    }

    private fun createDefaultAdminUserIfNeeded() {
        val sharedPref = getSharedPreferences("UserDatabase", MODE_PRIVATE)
        val adminExists = sharedPref.getBoolean("admin_created", false)

        if (!adminExists) {
            val editor = sharedPref.edit()
            editor.putString("admin_email", "admin@gamehub.com")
            editor.putString("admin_password", "admin123")
            editor.putString("admin_role", "admin")
            editor.putBoolean("admin_created", true)
            editor.apply()

            Toast.makeText(this, "Usuario administrador creado por defecto", Toast.LENGTH_SHORT).show()
        }
    }
}
