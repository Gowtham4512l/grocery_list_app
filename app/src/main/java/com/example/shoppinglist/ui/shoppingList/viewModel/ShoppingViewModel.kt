package com.example.shoppinglist.ui.shoppingList.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.db.entities.ShoppingItem
import com.example.shoppinglist.data.repository.ShoppingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    // Sort order states
    enum class SortOrder { NAME_ASC, NAME_DESC, QUANTITY_ASC, QUANTITY_DESC }

    // Filter state
    private val _currentFilter = MutableLiveData<String>("")
    val currentFilter: LiveData<String> = _currentFilter

    // Sort state
    private val _currentSortOrder = MutableLiveData<SortOrder>(SortOrder.NAME_ASC)
    val currentSortOrder: LiveData<SortOrder> = _currentSortOrder

    // Original items from repository
    private val _originalItems = repository.getAllItems()

    // Filtered and sorted items
    private val _filteredItems = MutableLiveData<List<ShoppingItem>>()
    val filteredItems: LiveData<List<ShoppingItem>> = _filteredItems

    init {
        // Observe original items and apply filter/sort when they change
        _originalItems.observeForever { items ->
            applyFilterAndSort(items)
        }
    }

    fun upsert(item: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsert(item)
        }
    }

    fun delete(item: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(item)
        }
    }

    fun setFilter(filter: String) {
        _currentFilter.value = filter
        applyFilterAndSort(_originalItems.value ?: emptyList())
    }

    fun setSortOrder(sortOrder: SortOrder) {
        _currentSortOrder.value = sortOrder
        applyFilterAndSort(_originalItems.value ?: emptyList())
    }

    private fun applyFilterAndSort(items: List<ShoppingItem>) {
        viewModelScope.launch(Dispatchers.Default) {
            // Apply filter
            var result = items
            val filter = _currentFilter.value ?: ""

            if (filter.isNotEmpty()) {
                result = result.filter {
                    it.name.contains(filter, ignoreCase = true)
                }
            }

            // Apply sort
            result = when (_currentSortOrder.value) {
                SortOrder.NAME_ASC -> result.sortedBy { it.name }
                SortOrder.NAME_DESC -> result.sortedByDescending { it.name }
                SortOrder.QUANTITY_ASC -> result.sortedBy { it.quantity }
                SortOrder.QUANTITY_DESC -> result.sortedByDescending { it.quantity }
                else -> result.sortedBy { it.name }
            }

            // Update filtered items
            _filteredItems.postValue(result)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Remove observer when ViewModel is cleared
        _originalItems.observeForever {  }
    }
}