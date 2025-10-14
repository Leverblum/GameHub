package com.example.gamehub.ui.admin

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.gamehub.R
import com.example.gamehub.repository.PrefsRepository
import com.example.gamehub.ui.admin.catalog.AdminProductsFragment
import com.example.gamehub.ui.admin.users.AdminUsersFragment

// Esta actividad controla directamente el layout del dashboard.
class AdminActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Inflamos el layout que es el propio dashboard.
        setContentView(R.layout.activity_admin)

        // 2. Vinculamos las opciones del menú directamente desde la actividad.
        val optManageProducts: LinearLayout = findViewById(R.id.optManageProducts)
        val optManageUsers: LinearLayout = findViewById(R.id.optManageUsers)

        // 3. Configuramos los listeners para la navegación.
        optManageProducts.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminProductsFragment())
                .addToBackStack(null)
                .commit()
        }

        optManageUsers.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminUsersFragment())
                .addToBackStack(null)
                .commit()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        val prefsRepository = PrefsRepository(this)
        prefsRepository.clearActiveUser()
    }

}