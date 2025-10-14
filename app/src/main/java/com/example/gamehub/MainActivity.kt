package com.example.gamehub

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gamehub.models.Product
import com.example.gamehub.repository.PrefsRepository
import com.example.gamehub.ui.cart.CartFragment
import com.example.gamehub.ui.catalog.CatalogFragment
import com.example.gamehub.ui.catalog.ProductDetailFragment // <-- NUEVO IMPORT
import com.example.gamehub.ui.home.HomeFragment
import com.example.gamehub.ui.profile.ProfileFragment
import com.example.gamehub.utils.NavigationListener // <-- NUEVO IMPORT
import com.google.android.material.bottomnavigation.BottomNavigationView

// 1. AÑADIMOS LA INTERFAZ A LA DECLARACIÓN DE LA CLASE
class MainActivity : AppCompatActivity(), NavigationListener {

    private companion object {
        const val TAG_HOME = "home_fragment"
        const val TAG_CATALOG = "catalog_fragment"
        const val TAG_CART = "cart_fragment"
        const val TAG_PROFILE = "profile_fragment"
        const val ACTIVE_TAG_KEY = "active_tag"
    }

    private lateinit var bottomNav: BottomNavigationView
    private var activeTag: String = TAG_HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavigation)

        if (savedInstanceState == null) {
            setupInitialFragments()
        }
        activeTag = savedInstanceState?.getString(ACTIVE_TAG_KEY) ?: TAG_HOME

        setupBottomNavListener()
        setupOnBackPressed()
    }

    private fun getFragmentForTag(tag: String): Fragment {
        val fragment: Fragment = when (tag) {
            TAG_HOME -> HomeFragment()
            TAG_CATALOG -> CatalogFragment()
            TAG_CART -> CartFragment()
            TAG_PROFILE -> ProfileFragment()
            else -> throw IllegalStateException("Tag de fragment desconocido: $tag")
        }
        return fragment
    }

    private fun setupInitialFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, getFragmentForTag(TAG_HOME), TAG_HOME)
            .commit()
        activeTag = TAG_HOME
    }

    private fun setupBottomNavListener() {
        bottomNav.setOnItemSelectedListener { item ->
            val selectedTag = when (item.itemId) {
                R.id.nav_home -> TAG_HOME
                R.id.nav_catalog -> TAG_CATALOG
                R.id.nav_cart -> TAG_CART
                R.id.nav_profile -> TAG_PROFILE
                else -> null
            }

            if (selectedTag != null && selectedTag != activeTag) {
                switchTo(selectedTag)
            }
            true
        }
        updateBottomNavSelection()
    }

    private fun switchTo(newTag: String) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        val currentFragment = fm.findFragmentByTag(activeTag)
        currentFragment?.let { transaction.hide(it) }

        var nextFragment = fm.findFragmentByTag(newTag)
        if (nextFragment == null) {
            nextFragment = getFragmentForTag(newTag)
            transaction.add(R.id.fragment_container, nextFragment, newTag)
        } else {
            transaction.show(nextFragment)
        }

        transaction.commit()
        activeTag = newTag
    }

    private fun updateBottomNavSelection() {
        val itemId = when (activeTag) {
            TAG_HOME -> R.id.nav_home
            TAG_CATALOG -> R.id.nav_catalog
            TAG_CART -> R.id.nav_cart
            TAG_PROFILE -> R.id.nav_profile
            else -> R.id.nav_home
        }
        if (bottomNav.selectedItemId != itemId) {
            bottomNav.selectedItemId = itemId
        }
    }

    private fun setupOnBackPressed() {
        // Esta lógica de back press ahora es más compleja. Cuando estemos en un
        // fragment de detalle, el back press debe llevarnos al fragment anterior (catálogo).
        // El `addToBackStack` que usamos se encarga de esto automáticamente.
        // El dispatcher se encargará del resto.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Si el FragmentManager puede hacer "pop" (volver atrás), lo hará.
                // Si no (porque estamos en un fragment principal), aplicamos nuestra lógica.
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else if (activeTag != TAG_HOME) {
                    bottomNav.selectedItemId = R.id.nav_home
                } else {
                    finish()
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ACTIVE_TAG_KEY, activeTag)
    }

    // 2. IMPLEMENTACIÓN DE LA FUNCIÓN DE LA INTERFAZ
    override fun onNavigateToProductDetail(product: Product) {
        // Creamos una instancia del fragment de detalle
        val detailFragment = ProductDetailFragment()

        // Usamos un Bundle para pasarle el ID del producto al fragment.
        val bundle = Bundle()
        bundle.putInt("PRODUCT_ID", product.id)
        detailFragment.arguments = bundle

        // Realizamos la transacción de fragments
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            // IMPORTANTE: Añadimos a la pila para poder volver al catálogo
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        val prefsRepository = PrefsRepository(this)
        prefsRepository.logout()
    }

    override fun onStop() {
        super.onStop()
        val prefsRepository = PrefsRepository(this)
        prefsRepository.logout()
    }

}
