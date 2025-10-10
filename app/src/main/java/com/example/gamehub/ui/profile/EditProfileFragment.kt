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

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    // Usamos activityViewModels para compartir la instancia del ViewModel con ProfileFragment
    private val profileViewModel: ProfileViewModel by activityViewModels()

    // Vistas del layout (usando tus IDs)
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etDob: EditText
    private lateinit var btnSaveProfile: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vinculamos todas las vistas de tu layout
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhone = view.findViewById(R.id.etPhone)
        etAddress = view.findViewById(R.id.etAddress)
        etDob = view.findViewById(R.id.etDob)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)

        // El campo de email no se debe poder editar
        etEmail.isEnabled = false

        // Configuramos los observadores y listeners
        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        // Observamos los datos del usuario para rellenar el formulario con la info actual
        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                etName.setText(it.name)
                etEmail.setText(it.email)
                etPhone.setText(it.phone ?: "")
                etAddress.setText(it.address ?: "")
                etDob.setText(it.dateOfBirth ?: "")
            }
        }
    }

    private fun setupListeners() {
        // Listener para el botón de guardar
        btnSaveProfile.setOnClickListener {
            saveProfileChanges()
        }
    }

    private fun saveProfileChanges() {
        // Recogemos los datos de los campos
        val newName = etName.text.toString().trim()
        val newPhone = etPhone.text.toString().trim()
        val newAddress = etAddress.text.toString().trim()
        val newDob = etDob.text.toString().trim()

        // Validación simple
        if (newName.isEmpty()) {
            etName.error = "El nombre no puede estar vacío"
            return
        }

        // Llamamos a la nueva función del ViewModel para guardar todo
        profileViewModel.updateUserProfile(newName, newPhone, newAddress, newDob)

        Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()

        // Volvemos a la pantalla de perfil
        parentFragmentManager.popBackStack()
    }
}

