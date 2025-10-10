package com.example.gamehub.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.gamehub.R
import com.example.gamehub.ui.auth.LoginActivity
import com.example.gamehub.ui.orders.OrderHistoryFragment
import com.example.gamehub.ui.settings.SettingsFragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Encontrar todas las vistas en las que se puede hacer clic
        val btnEditProfileShort: Button = view.findViewById(R.id.btnEditProfileShort)
        val optEditProfile: View = view.findViewById(R.id.opt_edit_profile) // El include se trata como una View
        val optOrderHistory: LinearLayout = view.findViewById(R.id.opt_order_history)
        // val optPaymentMethods: LinearLayout = view.findViewById(R.id.opt_payment_methods) // Descomentar cuando exista
        // val optStores: LinearLayout = view.findViewById(R.id.opt_stores) // Descomentar cuando exista
        val optSettings: LinearLayout = view.findViewById(R.id.opt_settings)
        val optChangePassword: LinearLayout = view.findViewById(R.id.opt_change_password)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)

        // Configurar los listeners
        btnEditProfileShort.setOnClickListener { navigateTo(EditProfileFragment()) }
        optEditProfile.setOnClickListener { navigateTo(EditProfileFragment()) }
        optOrderHistory.setOnClickListener { navigateTo(OrderHistoryFragment()) }
        optSettings.setOnClickListener { navigateTo(SettingsFragment()) }
        optChangePassword.setOnClickListener { navigateTo(ChangePasswordFragment()) }

        // Listeners para opciones futuras (actualmente comentados)
        // optPaymentMethods.setOnClickListener { ... }
        // optStores.setOnClickListener { ... }

        btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        return view
    }

    /**
     * Función reutilizable para navegar a otro fragment.
     * @param fragment La instancia del fragment al que se desea navegar.
     */
    private fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Permite volver al perfil con el botón "Atrás"
            .commit()
    }

    /**
     * Muestra un diálogo de alerta para confirmar el cierre de sesión.
     */
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí, cerrar sesión") { dialog, _ ->
                // Lógica de Logout
                Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()

                // Creamos un intent para ir a LoginActivity
                val intent = Intent(activity, LoginActivity::class.java)
                // Limpiamos la pila de actividades para que el usuario no pueda volver
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
