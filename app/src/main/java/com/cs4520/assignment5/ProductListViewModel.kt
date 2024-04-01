package com.cs4520.assignment5

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.room.Room
import androidx.work.WorkManager

class ProductListViewModel(private val context: Context): ViewModel() {
    var productList: MutableState<ProductList?> = mutableStateOf(null)
    var fetchDone: MutableState<Boolean> = mutableStateOf(false)

    init {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "product"
        ).build()
        val workManager = WorkManager.getInstance(context)
        val vm = DataViewModel(db, workManager)

        // Observe when API call has completed
        vm.products.observeForever { res ->
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

        vm.getProducts()
    }
}