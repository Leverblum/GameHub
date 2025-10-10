package com.example.gamehub.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gamehub.R
import com.example.gamehub.viewmodels.ProfileViewModel

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    // Usamos activityViewModels para acceder a los datos del usuario actual
    private val profileViewModel: ProfileViewModel by activityViewModels()

    // Vistas del layout
    private lateinit var etCurrentPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnUpdatePassword: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vinculamos las vistas desde el layout
        etCurrentPassword = view.findViewById(R.id.etCurrentPassword)
        etNewPassword = view.findViewById(R.id.etNewPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword)

        setupListeners()
    }

    private fun setupListeners() {
        btnUpdatePassword.setOnClickListener {
            handleChangePassword()
        }
    }

    private fun handleChangePassword() {
        // Recogemos los datos de los campos
        val currentPass = etCurrentPassword.text.toString()
        val newPass = etNewPassword.text.toString()
        val confirmPass = etConfirmPassword.text.toString()

        // Llamamos a la lógica del ViewModel
        val errorMessage = profileViewModel.changePassword(currentPass, newPass, confirmPass)

        if (errorMessage == null) {
            // Si no hay error, el cambio fue exitoso
            Toast.makeText(context, "Contraseña actualizada correctamente.", Toast.LENGTH_LONG).show()
            // Volvemos a la pantalla de perfil
            parentFragmentManager.popBackStack()
        } else {
            // Si hay un mensaje de error, lo mostramos
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
