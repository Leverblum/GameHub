package com.example.gamehub.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gamehub.R
import com.example.gamehub.ui.auth.LoginActivity
import com.example.gamehub.ui.orders.OrderHistoryFragment
import com.example.gamehub.ui.payment.PaymentMethodsFragment
import com.example.gamehub.ui.stores.StoresFragment
import com.example.gamehub.viewmodels.ProfileViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    // Obtenemos la instancia del ViewModel compartida a nivel de actividad
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Vinculación de Vistas ---
        val tvProfileName: TextView = view.findViewById(R.id.tvProfileName)
        val tvProfileEmail: TextView = view.findViewById(R.id.tvProfileEmail)

        val optEditProfile: LinearLayout = view.findViewById(R.id.option_edit_profile)
        val optOrderHistory: LinearLayout = view.findViewById(R.id.option_order_history)
        val optPaymentMethods: LinearLayout = view.findViewById(R.id.option_payment_methods)
        val optStores: LinearLayout = view.findViewById(R.id.option_stores)
        val optChangePassword: LinearLayout = view.findViewById(R.id.option_change_password)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)

        // --- Observadores del ViewModel ---

        // Observador para los datos del usuario. Se dispara cada vez que los datos cambian.
        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Si el usuario no es nulo, actualizamos la UI del perfil
                tvProfileName.text = it.name
                tvProfileEmail.text = it.email
            }
        }

        // Observador para el evento de cierre de sesión.
        profileViewModel.logoutEvent.observe(viewLifecycleOwner) { hasLoggedOut ->
            if (hasLoggedOut) {
                // Navegar a la pantalla de Login y limpiar la pila de actividades
                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                // Reseteamos el evento para que no se vuelva a disparar
                profileViewModel.onLogoutEventHandled()
            }
        }

        // --- Lógica de Clics ---

        btnLogout.setOnClickListener {
            profileViewModel.logout()
        }

        optEditProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditProfileFragment()) // Navega a EditProfileFragment
                .addToBackStack(null) // Permite volver atrás al perfil
                .commit()
        }

        optOrderHistory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, OrderHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        optPaymentMethods.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PaymentMethodsFragment())
                .addToBackStack(null)
                .commit()
        }

        optStores.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StoresFragment()) // Navega a StoresFragment
                .addToBackStack(null)
                .commit()
        }
        optChangePassword.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ChangePasswordFragment())
                .addToBackStack(null)
                .commit()
        }

    }
}
