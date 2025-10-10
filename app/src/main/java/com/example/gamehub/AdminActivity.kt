package com.example.gamehub.ui.admin

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.gamehub.R
import com.example.gamehub.ui.admin.catalog.AdminCatalogFragment
import com.example.gamehub.ui.admin.reports.AdminReportsFragment
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
        val optReports: LinearLayout = findViewById(R.id.optReports)

        // 3. Configuramos los listeners para la navegación.
        optManageProducts.setOnClickListener {
            // Reemplazamos la vista de la actividad ENTERA por el nuevo fragmento.
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AdminCatalogFragment())
                .addToBackStack(null) // Permite volver al dashboard.
                .commit()
        }

        optManageUsers.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AdminUsersFragment())
                .addToBackStack(null)
                .commit()
        }

        optReports.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AdminReportsFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
