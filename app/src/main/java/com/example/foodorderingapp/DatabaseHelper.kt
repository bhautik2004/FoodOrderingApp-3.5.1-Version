package com.example.foodorderingapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.foodorderingapp.model.*

//@Suppress("UNREACHABLE_CODE")
class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null,
    DATABASE_VERSION
) {
    class UserAlreadyExistsException(message: String) : Exception(message)
    companion object {
        private const val DATABASE_NAME = "FoodOrderDB"
        private const val DATABASE_VERSION = 2
        private const val TABLE_USERS = "users"
        private const val TABLE_ADMIN = "admin"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        const val TABLE_FOOD = "food_items"
        const val TABLE_CART = "cart"
        const val ID_COLUMN = "id"
        const val USER_ID = "user_id"
        const val FOOD_ID_COLUMN = "food_id"
        const val FOOD_NAME_COLUMN = "food_name"
        const val FOOD_QUANTITY = "food_quantity"
        const val FOOD_PRICE_COLUMN = "food_price"
        const val FOOD_IMAGE_COLUMN = "food_image"
        const val FOOD_DESCRIPTION_COLUMN = "food_description"
        val ORDER_ID_COLUMN = "order_id"
        const val TABLE_ORDER = "orders"
        const val COLUMN_DATE = "order_date"
        const val COLUMN_TOTAL_AMOUNT = "order_total_amount"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_ADDRESS = "address"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        //users Table
        val UsersTableQuery = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_PASSWORD TEXT)")
        db?.execSQL(UsersTableQuery)

        // admin Table
        val adminTableQuery = ("CREATE TABLE $TABLE_ADMIN (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_PASSWORD TEXT)")
        db?.execSQL(adminTableQuery)

        // Food_Items Table
        val foodItemTableQuery = (" CREATE TABLE $TABLE_FOOD (" +
                "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$FOOD_NAME_COLUMN TEXT," +
                "$FOOD_PRICE_COLUMN REAL," +
                "$FOOD_IMAGE_COLUMN BLOB," +
                "$FOOD_DESCRIPTION_COLUMN TEXT)")

        db?.execSQL(foodItemTableQuery)
        val cartTableQuery = (
                "CREATE TABLE $TABLE_CART (" +
                        "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$USER_ID INTEGER," +
                        "$FOOD_ID_COLUMN INTEGER," +
                        "$FOOD_QUANTITY INTEGER," +
                        "$ORDER_ID_COLUMN INTEGER," +
                        "FOREIGN KEY($USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID)," +
                        "FOREIGN KEY($ORDER_ID_COLUMN) REFERENCES $TABLE_ORDER($COLUMN_ID)," +
                        "FOREIGN KEY($FOOD_ID_COLUMN) REFERENCES $TABLE_FOOD($ID_COLUMN))"
                )

        db?.execSQL(cartTableQuery)


        val CREATE_ORDER_TABLE =
            "CREATE TABLE $TABLE_ORDER (" +
                    "$ORDER_ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_EMAIL TEXT, " +
                    "$COLUMN_PHONE TEXT, " +
                    "$COLUMN_ADDRESS TEXT, " +
                    "$COLUMN_DATE TEXT, " +
                    "$COLUMN_TOTAL_AMOUNT TEXT)"

        db?.execSQL(CREATE_ORDER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
//        db?.execSQL(dropTableQuery)
//        onCreate(db)
    }

    fun placeorder(
        name: String,
        email: String,
        phone: String,
        address: String,
        date: String,
        totalamount: String
    ): Long {
        try {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_NAME, name)
                put(COLUMN_EMAIL, email)
                put(COLUMN_PHONE, phone)
                put(COLUMN_DATE, date)
                put(COLUMN_ADDRESS, address)
                put(COLUMN_TOTAL_AMOUNT, totalamount)
            }
            val orderId = db.insertOrThrow(TABLE_ORDER, null, values)
            db.close()
            return orderId
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }


    fun signup(name: String, email: String, password: String): Long {

        if (userExists(email)) {
            val db = readableDatabase
            throw UserAlreadyExistsException("User with email $email already exists.")
            return -1
            db.close()

        }
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }
        val db = this.writableDatabase
        return db.insert(TABLE_USERS, null, values)
    }

    private fun userExists(email: String): Boolean {
        val db = readableDatabase
        val columns = arrayOf(COLUMN_ID)
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor: Cursor =
            db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0

        cursor.close()
        db.close()

        return userExists
    }

    fun login(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)
        val isuser = cursor.count > 0
        cursor.close()
        return isuser
    }

    fun getUserIdByEmail(email: String): Long {
        val db = readableDatabase
        val columns = arrayOf(COLUMN_ID)
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor: Cursor =
            db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)

        var userId: Long = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
        }
        cursor.close()
        db.close()
        return userId
    }

    fun addadmin(name: String, email: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }
        val db = this.writableDatabase
        return db.insert(TABLE_ADMIN, null, values)
    }

    fun adminlogin(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(TABLE_ADMIN, null, selection, selectionArgs, null, null, null)
        val isuser = cursor.count > 0
        cursor.close()
        return isuser
    }

    fun insertFoodItem(
        foodName: String,
        foodPrice: Int,
        foodImage: ByteArray,
        foodDescription: String
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FOOD_NAME_COLUMN, foodName)
            put(FOOD_PRICE_COLUMN, foodPrice)
            put(FOOD_IMAGE_COLUMN, foodImage)
            put(FOOD_DESCRIPTION_COLUMN, foodDescription)
        }
        val id = db.insert(TABLE_FOOD, null, values)
        return id > 0
        db.close()

    }

    fun getUserNameByEmail(email: String): String? {
        val db = readableDatabase
        val columns = arrayOf(COLUMN_NAME)
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor: Cursor =
            db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)

        var userName: String? = null
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }
        cursor.close()
        db.close()
        return userName
    }

    fun getAdminNameByEmail(email: String): String? {
        val db = readableDatabase
        val columns = arrayOf(COLUMN_NAME)
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor: Cursor =
            db.query(TABLE_ADMIN, columns, selection, selectionArgs, null, null, null)

        var userName: String? = null
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }
        cursor.close()
        db.close()
        return userName
    }

    fun addAdminUser(name: String, email: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }
        val db = this.writableDatabase
        return db.insert(TABLE_ADMIN, null, values)
    }

    fun getUser(): Cursor? {
        val db = readableDatabase
        val cursor = db.rawQuery("select * from $TABLE_USERS", null)
        return cursor
    }

    fun getAllFoodItem(): ArrayList<FoodItem> {
        val itemList = ArrayList<FoodItem>()
        val selectQry = "SELECT * FROM $TABLE_FOOD"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQry, null)
        if (cursor.moveToFirst()) {
            do {
                val foodItemId = cursor.getString(cursor.getColumnIndex(ID_COLUMN))
                val foodName = cursor.getString(cursor.getColumnIndex(FOOD_NAME_COLUMN))
                val foodPrice = cursor.getString(cursor.getColumnIndex(FOOD_PRICE_COLUMN))
                val foodImageByteArray = cursor.getBlob(cursor.getColumnIndex(FOOD_IMAGE_COLUMN))
                val foodDescription =
                    cursor.getString(cursor.getColumnIndex(FOOD_DESCRIPTION_COLUMN))

                val foodItem =
                    FoodItem(foodItemId, foodName, foodPrice, foodImageByteArray, foodDescription)
                itemList.add(foodItem)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }

    fun updateFoodItem(
        id: String,
        foodName: String,
        foodPrice: Int,
        foodImage: ByteArray,
        foodDescription: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FOOD_NAME_COLUMN, foodName)
            put(FOOD_PRICE_COLUMN, foodPrice)
            put(FOOD_IMAGE_COLUMN, foodImage)
            put(FOOD_DESCRIPTION_COLUMN, foodDescription)
        }
        return db.update(TABLE_FOOD, values, ID_COLUMN + "=?", arrayOf(id)).toLong()
    }

    fun deleteFoodItem(id: String) {
        val db = writableDatabase
        db.delete(TABLE_FOOD, ID_COLUMN + "=?", arrayOf(id))
        db.close()
    }

    fun updateCartItemQuantity(userId: Long, cartItemId: String, newQuantity: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FOOD_QUANTITY, newQuantity)
        }
        db.update(
            TABLE_CART,
            values,
            "$ID_COLUMN = ? AND $USER_ID = ?",
            arrayOf(cartItemId, userId.toString())
        )
        db.close()
    }

    fun addToCart(userId: Long, foodId: Int, quantity: Int): Long {
        val db = this.writableDatabase

        // Check if the item is already in the cart
        val checkQuery =
            "SELECT $ID_COLUMN, $FOOD_QUANTITY FROM $TABLE_CART WHERE $USER_ID = ? AND $FOOD_ID_COLUMN = ? AND $ORDER_ID_COLUMN IS NULL"
        val checkArgs = arrayOf(userId.toString(), foodId.toString())
        val cursor = db.rawQuery(checkQuery, checkArgs)

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val existingItemId = cursor.getLong(cursor.getColumnIndex(ID_COLUMN))
            val existingQuantity = cursor.getInt(cursor.getColumnIndex(FOOD_QUANTITY))
            cursor.close()
            return -1
        }

        cursor.close()
        val values = ContentValues().apply {
            put(USER_ID, userId)
            put(FOOD_ID_COLUMN, foodId)
            put(FOOD_QUANTITY, quantity)
        }

        val id = db.insert(TABLE_CART, null, values)
        db.close()

        return id
    }

    fun getCartItems(userId: Long): ArrayList<CartItem> {
        val itemList = ArrayList<CartItem>()

        val selectQuery =
            "SELECT $TABLE_CART.$ID_COLUMN, " +
                    "$TABLE_CART.$FOOD_ID_COLUMN, " +
                    "$TABLE_FOOD.$FOOD_NAME_COLUMN, " +
                    "$TABLE_FOOD.$FOOD_PRICE_COLUMN, " +
                    "$TABLE_CART.$FOOD_QUANTITY, " +
                    "$TABLE_FOOD.$FOOD_IMAGE_COLUMN, " +
                    "$TABLE_FOOD.$FOOD_DESCRIPTION_COLUMN " +
                    "FROM $TABLE_CART " +
                    "JOIN $TABLE_FOOD ON $TABLE_CART.$FOOD_ID_COLUMN = $TABLE_FOOD.$ID_COLUMN " +
                    "WHERE $TABLE_CART.$USER_ID =? " +
                    "AND $TABLE_CART.$ORDER_ID_COLUMN IS NULL"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex(ID_COLUMN))
                val foodId = cursor.getString(cursor.getColumnIndex(FOOD_ID_COLUMN))
                val foodName = cursor.getString(cursor.getColumnIndex(FOOD_NAME_COLUMN))
                val foodPrice = cursor.getString(cursor.getColumnIndex(FOOD_PRICE_COLUMN))
                val foodQuantity = cursor.getInt(cursor.getColumnIndex(FOOD_QUANTITY))
                val foodImageByteArray = cursor.getBlob(cursor.getColumnIndex(FOOD_IMAGE_COLUMN))
                val foodDescription =
                    cursor.getString(cursor.getColumnIndex(FOOD_DESCRIPTION_COLUMN))

                val cartItem = CartItem(
                    id,
                    foodId,
                    foodName,
                    foodPrice,
                    foodQuantity,
                    foodImageByteArray,
                    foodDescription
                )
                itemList.add(cartItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return itemList
    }


    fun deleteCartItem(userId: Long, cartItemId: String) {
        val db = writableDatabase
        db.delete(
            TABLE_CART,
            "$ID_COLUMN = ? AND $USER_ID = ?",
            arrayOf(cartItemId, userId.toString())
        )
        db.close()
    }

    fun updateOrderIdInCartForUser(userId: Long, orderId: Long): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ORDER_ID_COLUMN, orderId)
        }

        val rowsAffected = db.update(
            TABLE_CART,
            values,
            "$USER_ID = ? AND $ORDER_ID_COLUMN IS NULL",
            arrayOf(userId.toString())
        )
        db.close()
        return rowsAffected > 0
    }

    fun getAllOrders(): ArrayList<Order> {
        val orderList = ArrayList<Order>()

        val selectQuery = "SELECT * FROM $TABLE_ORDER"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val orderId = cursor.getLong(cursor.getColumnIndex(ORDER_ID_COLUMN))
                val username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
                val totalAmount = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_AMOUNT))
                val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                val order = Order(orderId, username, totalAmount, date, email)
                orderList.add(order)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return orderList
    }

    fun getOrderDetails(orderId: Long): OrderDetails? {
        val db = readableDatabase
        val columns = arrayOf(
            ORDER_ID_COLUMN,
            COLUMN_NAME,
            COLUMN_EMAIL,
            COLUMN_PHONE,
            COLUMN_ADDRESS,
            COLUMN_DATE,
            COLUMN_TOTAL_AMOUNT
        )
        val selection = "$ORDER_ID_COLUMN = ?"
        val selectionArgs = arrayOf(orderId.toString())

        val cursor: Cursor = db.query(
            TABLE_ORDER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var orderDetails: OrderDetails? = null
        if (cursor.moveToFirst()) {
            val username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            val phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE))
            val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
            val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
            val totalAmount = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_AMOUNT))

            orderDetails = OrderDetails(
                orderId,
                username,
                email,
                phone,
                address,
                date,
                totalAmount
            )
        }
        cursor.close()
        db.close()
        return orderDetails
    }

    fun getOrderedItems(orderId: Long): List<CartItem> {
        val itemList = ArrayList<CartItem>()

        val selectQuery =
            "SELECT $TABLE_CART.$ID_COLUMN, " +
                    "$TABLE_CART.$FOOD_ID_COLUMN, " +
                    "$TABLE_FOOD.$FOOD_NAME_COLUMN, " +
                    "$TABLE_FOOD.$FOOD_PRICE_COLUMN, " +
                    "$TABLE_CART.$FOOD_QUANTITY, " +
                    "$TABLE_FOOD.$FOOD_IMAGE_COLUMN " +
                    "FROM $TABLE_CART " +
                    "JOIN $TABLE_FOOD ON $TABLE_CART.$FOOD_ID_COLUMN = $TABLE_FOOD.$ID_COLUMN " +
                    "WHERE $TABLE_CART.$ORDER_ID_COLUMN = ?"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(orderId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex("$TABLE_CART.$ID_COLUMN"))
                val foodId = cursor.getString(cursor.getColumnIndex("$TABLE_CART.$FOOD_ID_COLUMN"))
                val foodName =
                    cursor.getString(cursor.getColumnIndex("$TABLE_FOOD.$FOOD_NAME_COLUMN"))
                val foodPrice =
                    cursor.getString(cursor.getColumnIndex("$TABLE_FOOD.$FOOD_PRICE_COLUMN"))
                val foodQuantity =
                    cursor.getInt(cursor.getColumnIndex("$TABLE_CART.$FOOD_QUANTITY"))
                val foodImageByteArray =
                    cursor.getBlob(cursor.getColumnIndex("$TABLE_FOOD.$FOOD_IMAGE_COLUMN"))

                val cartItem =
                    CartItem(id, foodId, foodName, foodPrice, foodQuantity, foodImageByteArray, "")
                itemList.add(cartItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return itemList
    }

    fun deleteOrder(orderId: Long) {
        val db = writableDatabase

        // Delete cart items associated with the order
        deleteCartItemsForOrder(db, orderId)

        // Delete the order
        db.delete(TABLE_ORDER, "$ORDER_ID_COLUMN = ?", arrayOf(orderId.toString()))

        db.close()
    }

    private fun deleteCartItemsForOrder(db: SQLiteDatabase, orderId: Long) {
        db.delete(
            TABLE_CART,
            "$ORDER_ID_COLUMN = ?",
            arrayOf(orderId.toString())
        )
    }

    fun getFoodOrderDetailsForUser(email: String): ArrayList<MyOrderDetails> {
        val foodOrderDetailsList = ArrayList<MyOrderDetails>()

        val selectQuery =
            "SELECT " +
                    "$TABLE_FOOD.$FOOD_NAME_COLUMN, " +
                    "$TABLE_FOOD.$FOOD_PRICE_COLUMN, " +
                    "$TABLE_CART.$FOOD_QUANTITY, " +
                    "$TABLE_ORDER.$COLUMN_DATE, " +
                    "$TABLE_FOOD.$FOOD_IMAGE_COLUMN " +
                    "FROM $TABLE_ORDER " +
                    "JOIN $TABLE_CART ON $TABLE_ORDER.$ORDER_ID_COLUMN = $TABLE_CART.$ORDER_ID_COLUMN " +
                    "JOIN $TABLE_FOOD ON $TABLE_CART.$FOOD_ID_COLUMN = $TABLE_FOOD.$ID_COLUMN " +
                    "JOIN $TABLE_USERS ON $TABLE_ORDER.$COLUMN_EMAIL = $TABLE_USERS.$COLUMN_EMAIL " +
                    "WHERE $TABLE_USERS.$COLUMN_EMAIL = ?"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(email))

        if (cursor.moveToFirst()) {
            do {
                val foodName =
                    cursor.getString(cursor.getColumnIndex("$TABLE_FOOD.$FOOD_NAME_COLUMN"))
                val foodPrice =
                    cursor.getString(cursor.getColumnIndex("$TABLE_FOOD.$FOOD_PRICE_COLUMN"))
                val foodQuantity =
                    cursor.getInt(cursor.getColumnIndex("$TABLE_CART.$FOOD_QUANTITY"))
                val orderDateTime =
                    cursor.getString(cursor.getColumnIndex("$TABLE_ORDER.$COLUMN_DATE"))
                val foodImage =
                    cursor.getBlob(cursor.getColumnIndex("$TABLE_FOOD.$FOOD_IMAGE_COLUMN")) // Retrieve food image data

                val foodOrderDetails =
                    MyOrderDetails(foodName, foodPrice, foodQuantity, orderDateTime, foodImage)
                foodOrderDetailsList.add(foodOrderDetails)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return foodOrderDetailsList
    }
}
