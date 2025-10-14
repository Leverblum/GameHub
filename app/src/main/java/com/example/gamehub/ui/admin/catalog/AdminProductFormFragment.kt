package com.example.gamehub.ui.admin.catalog

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gamehub.R
import com.example.gamehub.models.Product
import java.math.BigDecimal

class AdminProductFormFragment : Fragment(R.layout.fragment_admin_product_form) {

    private val viewModel: AdminCatalogViewModel by activityViewModels()

    // Vistas del formulario
    private lateinit var tvFormTitle: TextView
    private lateinit var etName: EditText
    private lateinit var etCategory: EditText
    private lateinit var etPrice: EditText
    private lateinit var etStock: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var btnSelectImage: Button

    private var editingProductId: Int? = null // Variable para saber si estamos editando

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recogemos el ID del producto si se pasó en los argumentos
        arguments?.let {
            if (it.containsKey(ARG_PRODUCT_ID)) {
                editingProductId = it.getInt(ARG_PRODUCT_ID)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews(view)
        setupListeners()

        // Si estamos en modo "Editar", cargamos los datos del producto
        editingProductId?.let { id ->
            loadProductData(id)
        }
    }

    private fun bindViews(view: View) {
        etName = view.findViewById(R.id.etProductName)
        etCategory = view.findViewById(R.id.etProductCategory)
        etPrice = view.findViewById(R.id.etProductPrice)
        etStock = view.findViewById(R.id.etProductStock)
        etDescription = view.findViewById(R.id.etProductDescription)
        btnSave = view.findViewById(R.id.btnSaveProduct)
        btnSelectImage = view.findViewById(R.id.btnSelectImage)
    }

    /**
     * Carga los datos de un producto existente en los campos del formulario.
     */
    private fun loadProductData(productId: Int) {
        val product = viewModel.getProductById(productId)
        product?.let {
            tvFormTitle.text = "Editar Producto"
            btnSave.text = "Actualizar Producto"
            etName.setText(it.name)
            etCategory.setText(it.category)
            etPrice.setText(it.price.toString())
            etStock.setText(it.stock.toString())
            etDescription.setText(it.description)
        }
    }

    private fun setupListeners() {
        btnSave.setOnClickListener {
            saveProduct()
        }

        btnSelectImage.setOnClickListener {
            Toast.makeText(context, "Función para seleccionar imagen no implementada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProduct() {
        val name = etName.text.toString().trim()
        val category = etCategory.text.toString().trim()
        val priceStr = etPrice.text.toString().trim()
        val stockStr = etStock.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (name.isEmpty() || category.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val price = BigDecimal(priceStr)
            val stock = stockStr.toInt()

            // Decidimos si llamar a 'update' o 'add' basándonos en si tenemos un ID para editar
            if (editingProductId != null) {
                // Modo Editar
                viewModel.updateProduct(editingProductId!!, name, description, price, stock, category)
                Toast.makeText(context, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show()
            } else {
                // Modo Crear
                viewModel.addProduct(name, description, price, stock, category)
                Toast.makeText(context, "Producto añadido con éxito", Toast.LENGTH_SHORT).show()
            }
            parentFragmentManager.popBackStack() // Volvemos a la lista

        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Por favor, introduce un precio y stock válidos", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Companion object para crear instancias del fragmento de forma limpia y segura.
     */
    companion object {
        private const val ARG_PRODUCT_ID = "product_id"

        // Fábrica para crear una instancia en modo "Crear"
        fun newInstance() = AdminProductFormFragment()

        // Fábrica para crear una instancia en modo "Editar", pasando el ID del producto
        fun newInstance(productId: Int): AdminProductFormFragment {
            val fragment = AdminProductFormFragment()
            val args = Bundle()
            args.putInt(ARG_PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
    }
}
