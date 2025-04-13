package com.example.shoppinglist.ui.shoppingList

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.data.db.ShoppingDatabase
import com.example.shoppinglist.data.db.entities.ShoppingItem
import com.example.shoppinglist.data.repository.ShoppingRepository
import com.example.shoppinglist.ui.shoppingList.adapter.ShoppingItemAdapter
import com.example.shoppinglist.ui.shoppingList.viewModel.ShoppingViewModel
import com.example.shoppinglist.ui.shoppingList.viewModel.ShoppingViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingActivity : AppCompatActivity() {
    private lateinit var database: ShoppingDatabase
    private lateinit var repository: ShoppingRepository
    private lateinit var factory: ShoppingViewModelFactory
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var adapter: ShoppingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shopping)

        database = ShoppingDatabase(this)
        repository = ShoppingRepository(database)
        factory = ShoppingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ShoppingViewModel::class.java]

        setUpRecyclerView()
        observeShoppingItems()
        setUpFab()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setUpRecyclerView() {
        adapter = ShoppingItemAdapter(listOf(), viewModel)

        val recyclerView = findViewById<RecyclerView>(R.id.rvShoppingItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun observeShoppingItems() {
        viewModel.allItems.observe(this) { items ->
            adapter.updateList(items)
        }
    }

    private fun setUpFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fabAddItem)
        fab.setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun showAddItemDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_shoping_item, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnSave).setOnClickListener {
            val name = dialogView.findViewById<EditText>(R.id.etItemName).text.toString()
            val quantityStr = dialogView.findViewById<EditText>(R.id.etItemQuantity).text.toString()
            if (name.isNotBlank() && quantityStr.isNotBlank()) {
                val quantity = quantityStr.toInt()
                val item = ShoppingItem(id = 0, name = name, quantity = quantity)
                viewModel.upsert(item)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}