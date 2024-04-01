package com.cs4520.assignment5

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.util.concurrent.TimeUnit

class DataViewModel(private val productDb: AppDatabase,
                    private val workManager: WorkManager,
) : ViewModel(){
    var products = MutableLiveData<ProductList?>()

    val productDao = productDb.productDao()

    init {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val intervalRequest= PeriodicWorkRequestBuilder<CoroutineApiCall>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "coroutineApiCall",
            ExistingPeriodicWorkPolicy.REPLACE,
            intervalRequest
        )


        val workInfoLiveData = workManager.getWorkInfoByIdLiveData(intervalRequest.id)

        workInfoLiveData.observeForever { workInfo ->
            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                val outputData = workInfo.outputData
                val productListByteArray = outputData.getByteArray("productList")
                val productListFromWorker = deserializeProductList(productListByteArray)

                products.value = productListFromWorker
            }
        }
    }

    private fun deserializeProductList(byteArray: ByteArray?): ProductList? {
        if (byteArray == null) return null
        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val productList = objectInputStream.readObject() as ProductList
        objectInputStream.close()
        return productList
    }

    fun getProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("Entered try")
                val productsResult = ProductApi.retrofitService.getProducts().body()
                println("Result in VM: " + productsResult)
                products.postValue(productsResult)
                if (productsResult != null) {
                    productDao.deleteAll() // clear the db before we store new data
                    productDao.insertAll(*productsResult.toTypedArray())
                }
            } catch (e: Exception) {
                val storedProducts = productDao.getAll()
                println("Inside error: " + e + " W/ Products: " + storedProducts)
                val prodList: ProductList = ProductList()
                for (product in storedProducts) {
                    prodList.add(product)
                }
                products.postValue(prodList)
            }
        }
    }
}