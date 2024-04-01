package com.cs4520.assignment5

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.room.Room

class ProductListViewModel(private val context: Context): ViewModel() {
    var productList: MutableState<ProductList?> = mutableStateOf(null)
    var fetchDone: MutableState<Boolean> = mutableStateOf(false)

    private lateinit var viewModel: DataViewModel

    init {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "product"
        ).build()
        val vm = DataViewModel(db)
        viewModel = vm

        // Observe when API call has completed
        viewModel.products.observeForever { res ->
            fetchDone.value = false
            var productSet: ProductList = ProductList()
            if (res != null) {
                for (p in res.distinct()) {
                    productSet.add(p)
                }
            }
            productList.value = if (productSet.size > 0) productSet else res
            fetchDone.value = true
        }

        viewModel.getProducts()
    }
}