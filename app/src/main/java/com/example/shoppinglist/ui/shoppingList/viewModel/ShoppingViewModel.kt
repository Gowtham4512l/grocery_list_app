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
    enum class SortOrder { NAME_ASC, NAME_DESC, QUANTITY_ASC, QUANTITY_DESC }

    private val _currentFilter = MutableLiveData<String>("")

    val currentFilter: LiveData<String> = _currentFilter
    private val _currentSortOrder = MutableLiveData<SortOrder>(SortOrder.NAME_ASC)

    val currentSortOrder: LiveData<SortOrder> = _currentSortOrder
    private val _originalItems = repository.getAllItems()

    private val _filteredItems = MutableLiveData<List<ShoppingItem>>()
    val filteredItems: LiveData<List<ShoppingItem>> = _filteredItems

    init {
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
            var result = items
            val filter = _currentFilter.value ?: ""

            if (filter.isNotEmpty()) {
                result = result.filter {
                    it.name.contains(filter, ignoreCase = true)
                }
            }

            result = when (_currentSortOrder.value) {
                SortOrder.NAME_ASC -> result.sortedBy { it.name }
                SortOrder.NAME_DESC -> result.sortedByDescending { it.name }
                SortOrder.QUANTITY_ASC -> result.sortedBy { it.quantity }
                SortOrder.QUANTITY_DESC -> result.sortedByDescending { it.quantity }
                else -> result.sortedBy { it.name }
            }

            _filteredItems.postValue(result)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _originalItems.observeForever { }
    }
}