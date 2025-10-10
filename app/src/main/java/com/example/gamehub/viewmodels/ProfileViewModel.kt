package com.example.gamehub.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gamehub.models.User
import com.example.gamehub.repository.PrefsRepository

// Se usa AndroidViewModel para poder tener el 'context' y así usar PrefsRepository
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepository = PrefsRepository(application)

    // LiveData para exponer los datos del usuario activo
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    // LiveData para notificar que se ha cerrado la sesión
    private val _logoutEvent = MutableLiveData<Boolean>()
    val logoutEvent: LiveData<Boolean> = _logoutEvent

    init {
        loadCurrentUser()
    }

    /**
     * Carga los datos del usuario que tiene la sesión activa.
     */
    private fun loadCurrentUser() {
        // 1. Obtiene el email del usuario activo guardado en SharedPreferences
        val activeUserEmail = prefsRepository.getActiveUserEmail()
        if (activeUserEmail != null) {
            // 2. Obtiene la lista completa de usuarios registrados
            val allUsers = prefsRepository.getUsers()
            // 3. Busca al usuario por su email
            _user.value = allUsers.find { it.email == activeUserEmail }
        } else {
            _user.value = null
        }
    }

    /**
     * Cierra la sesión del usuario.
     */
    fun logout() {
        // Limpia el email del usuario activo en SharedPreferences
        prefsRepository.clearActiveUser()
        // Notifica a la vista que el cierre de sesión ocurrió para que pueda navegar
        _logoutEvent.value = true
    }

    /**
     * Resetea el evento de logout para que no se dispare de nuevo (ej. al rotar la pantalla).
     */
    fun onLogoutEventHandled() {
        _logoutEvent.value = false
    }

    fun updateUserProfile(
        newName: String,
        newPhone: String,
        newAddress: String,
        newDob: String
    ) {
        // 1. Obtener el email del usuario activo (que es la clave única)
        val userEmail = _user.value?.email ?: return

        // 2. Cargar la lista completa de usuarios
        val allUsers = prefsRepository.getUsers()

        // 3. Encontrar al usuario actual y crear una copia actualizada con los nuevos datos
        val updatedUser = allUsers.find { it.email == userEmail }?.copy(
            name = newName,
            phone = newPhone,
            address = newAddress,
            dateOfBirth = newDob
            // La actualización del avatar se manejaría en otra función
        )

        // 4. Si el usuario se encontró, se procede a guardar
        if (updatedUser != null) {
            // Creamos una nueva lista reemplazando el usuario antiguo por el actualizado
            val updatedList = allUsers.map {
                if (it.email == userEmail) updatedUser else it
            }
            // Guardamos la lista completa en las preferencias
            prefsRepository.saveUsers(updatedList)

            // 5. Actualizamos el LiveData para que la UI reaccione inmediatamente
            _user.value = updatedUser
        }
    }

    fun changePassword(currentPass: String, newPass: String, confirmPass: String): String? {
        // 1. Validaciones de los campos de entrada
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            return "Todos los campos son obligatorios."
        }
        if (newPass != confirmPass) {
            return "La nueva contraseña no coincide con la confirmación."
        }
        if (newPass.length < 6) {
            return "La nueva contraseña debe tener al menos 6 caracteres."
        }

        // 2. Obtener el usuario activo
        val currentUser = _user.value ?: return "No se pudo encontrar el usuario activo."
        val allUsers = prefsRepository.getUsers().toMutableList()
        val userInList = allUsers.find { it.email == currentUser.email } ?: return "Error al cargar datos del usuario."

        // 3. Verificar que la contraseña actual sea correcta
        // NOTA: En una app real, la contraseña estaría encriptada (hashed).
        // Aquí comparamos el texto plano como lo has estado manejando en el registro.
        if (userInList.password != currentPass) {
            return "La contraseña actual es incorrecta."
        }

        if (userInList.password == newPass) {
            return "La nueva contraseña no puede ser igual a la actual."
        }

        // 4. Si todo es correcto, actualizar la contraseña
        val updatedUser = userInList.copy(password = newPass)
        val updatedList = allUsers.map { if (it.email == currentUser.email) updatedUser else it }
        prefsRepository.saveUsers(updatedList)

        // 5. Devolver null para indicar que la operación fue exitosa
        return null
    }
}
