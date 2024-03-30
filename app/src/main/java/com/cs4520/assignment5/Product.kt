package com.cs4520.assignment5

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import retrofit2.http.DELETE


class ProductList: ArrayList<Product>()

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val type: String,
    @ColumnInfo val expiryDate: String?,
    @ColumnInfo val price: Double) {

}

@Dao
interface ProductDao {
    @Query("select * from product")
    fun getAll(): List<Product>

    @Insert
    fun insertAll(vararg products: Product)

    @Query("delete from product")
    fun deleteAll()
}

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}

//data class Equipment(val name: String, val price: Int): Product()
//data class Food(val name: String, val expiry: String, val price: Int): Product()