package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CandleDao {

    // Saved Candles
    @Query("SELECT * FROM saved_candles ORDER BY createdAt DESC")
    fun getAllSavedCandles(): Flow<List<SavedCandleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedCandle(candle: SavedCandleEntity)

    @Delete
    suspend fun deleteSavedCandle(candle: SavedCandleEntity)

    @Query("DELETE FROM saved_candles WHERE id = :id")
    suspend fun deleteSavedCandleById(id: String)

    // Cart Items
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: String, quantity: Int)

    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItemById(id: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // Orders
    @Query("SELECT * FROM orders ORDER BY placedAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("UPDATE orders SET statusName = :statusName, estimatedArrival = :estimatedArrival WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, statusName: String, estimatedArrival: String)
}
