package com.example.shoppinglist.ui.shoppingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.ShoppingApp
import com.example.shoppinglist.data.db.entities.ShoppingItem
import com.example.shoppinglist.ui.shoppingList.adapter.ShoppingItemAdapter
import com.example.shoppinglist.ui.shoppingList.viewModel.ShoppingViewModel
import com.example.shoppinglist.ui.shoppingList.viewModel.ShoppingViewModel.SortOrder
import com.example.shoppinglist.ui.shoppingList.viewModel.ShoppingViewModelFactory
import com.example.shoppinglist.utils.ThemeUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingActivity : AppCompatActivity() {
    private lateinit var factory: ShoppingViewModelFactory
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var adapter: ShoppingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make sure status bar doesn't overlap content
        WindowCompat.setDecorFitsSystemWindows(window, true)

        enableEdgeToEdge()
        setContentView(R.layout.activity_shopping)

        // Set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize ViewModel with application repository
        factory = ShoppingViewModelFactory(ShoppingApp.repository)
        viewModel = ViewModelProvider(this, factory)[ShoppingViewModel::class.java]

        setUpRecyclerView()
        observeShoppingItems()
        setUpFab()

        // Set proper insets for edge-to-edge display - removing excessive top padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val displayCutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())

            // Only apply left, right and bottom padding
            val leftPadding = maxOf(systemBars.left, displayCutout.left)
            val rightPadding = maxOf(systemBars.right, displayCutout.right)
            val bottomPadding = maxOf(systemBars.bottom, displayCutout.bottom)

            // Set zero top padding - let the AppBarLayout handle top insets
            v.setPadding(leftPadding, 0, rightPadding, bottomPadding)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_shopping, menu)

        // Set up search functionality
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setFilter(newText ?: "")
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort -> {
                showSortMenu()
                true
            }

            R.id.action_theme -> {
                showThemeMenu()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpRecyclerView() {
        adapter = ShoppingItemAdapter(listOf(), viewModel)

        val recyclerView = findViewById<RecyclerView>(R.id.rvShoppingItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun observeShoppingItems() {
        viewModel.filteredItems.observe(this) { items ->
            adapter.updateList(items)
        }
    }

    private fun setUpFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fabAddItem)
        fab.setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun showSortMenu() {
        val view = findViewById<Toolbar>(R.id.toolbar)
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_sort, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort_name_asc -> {
                    viewModel.setSortOrder(SortOrder.NAME_ASC)
                    true
                }

                R.id.sort_name_desc -> {
                    viewModel.setSortOrder(SortOrder.NAME_DESC)
                    true
                }

                R.id.sort_quantity_asc -> {
                    viewModel.setSortOrder(SortOrder.QUANTITY_ASC)
                    true
                }

                R.id.sort_quantity_desc -> {
                    viewModel.setSortOrder(SortOrder.QUANTITY_DESC)
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showAddItemDialog() {
        // Use BottomSheetDialog instead of AlertDialog for better UX
        val bottomSheetDialog = createAddItemBottomSheet()
        bottomSheetDialog.show()
    }

    private fun createAddItemBottomSheet(): BottomSheetDialog {
        val dialog = BottomSheetDialog(this, R.style.Theme_ShoppingList_BottomSheetDialog)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_shoping_item, null)
        dialog.setContentView(dialogView)

        // Make dialog modal to prevent interaction with items behind it
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

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

        return dialog
    }

    private fun showThemeMenu() {
        val view = findViewById<Toolbar>(R.id.toolbar)
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_theme, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.theme_system -> {
                    ThemeUtil.setThemeMode(this, ThemeUtil.MODE_SYSTEM)
                    true
                }

                R.id.theme_light -> {
                    ThemeUtil.setThemeMode(this, ThemeUtil.MODE_LIGHT)
                    true
                }

                R.id.theme_dark -> {
                    ThemeUtil.setThemeMode(this, ThemeUtil.MODE_DARK)
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }
}