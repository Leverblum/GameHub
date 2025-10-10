package com.example.gamehub.ui.admin.catalog

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gamehub.R
import java.math.BigDecimal

// La clase hereda de Fragment y usa tu layout como vista
class AdminProductFormFragment : Fragment(R.layout.fragment_admin_product_form) {

    // Usamos activityViewModels para compartir la instancia del ViewModel con la lista de productos
    private val viewModel: AdminCatalogViewModel by activityViewModels()

    // Declaramos todas las vistas de tu formulario
    private lateinit var etName: EditText
    private lateinit var etCategory: EditText
    private lateinit var etPrice: EditText
    private lateinit var etStock: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSelectImage: Button
    private lateinit var btnSave: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vinculamos las vistas usando los IDs de tu layout
        etName = view.findViewById(R.id.etProductName)
        etCategory = view.findViewById(R.id.etProductCategory)
        etPrice = view.findViewById(R.id.etProductPrice)
        etStock = view.findViewById(R.id.etProductStock)
        etDescription = view.findViewById(R.id.etProductDescription)
        btnSelectImage = view.findViewById(R.id.btnSelectImage)
        btnSave = view.findViewById(R.id.btnSaveProduct)

        // Configuramos los listeners para los botones
        setupListeners()
    }

    private fun setupListeners() {
        btnSave.setOnClickListener {
            saveProduct()
        }

        btnSelectImage.setOnClickListener {
            // La lógica para abrir la galería de imágenes es más avanzada.
            // Por ahora, mostraremos un Toast como placeholder.
            Toast.makeText(context, "Función para seleccionar imagen no implementada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProduct() {
        // Recolectamos los datos de los EditText
        val name = etName.text.toString().trim()
        val category = etCategory.text.toString().trim()
        val priceStr = etPrice.text.toString().trim()
        val stockStr = etStock.text.toString().trim()
        val description = etDescription.text.toString().trim()

        // Validación para asegurar que ningún campo esté vacío
        if (name.isEmpty() || category.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Usamos un bloque try-catch para manejar errores si el precio o el stock no son números válidos
        try {
            val price = BigDecimal(priceStr)
            val stock = stockStr.toInt()

            // Llamamos a la función del ViewModel para añadir el producto
            // En el futuro, aquí también pasaríamos la URL de la imagen seleccionada
            viewModel.addProduct(name, description, price, stock, category)

            // Mostramos un mensaje de éxito
            Toast.makeText(context, "Producto guardado con éxito", Toast.LENGTH_SHORT).show()

            // Cerramos el formulario y volvemos a la pantalla anterior (la lista de productos)
            parentFragmentManager.popBackStack()

        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Por favor, introduce un precio y stock válidos", Toast.LENGTH_SHORT).show()
        }
    }
}
