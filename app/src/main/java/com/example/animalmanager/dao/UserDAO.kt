package com.example.animalmanager.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.animalmanager.db.DbHelperUser
import com.example.animalmanager.models.User

class UserDAO(context: Context) {
    companion object {
        const val TABLE_USER = "user"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PHOTO = "photo"
        private const val COLUMN_NOM = "lastname"
        private const val COLUMN_PRENOM = "firstname"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ROLE = "role"
        private const val COLUMN_PASSWORD = "password"

        const val CREATE_REQUEST = """
            CREATE TABLE $TABLE_USER(
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PHOTO TEXT NOT NULL,
                $COLUMN_NOM TEXT NOT NULL,
                $COLUMN_PRENOM TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_ROLE TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """

        const val UPGRADE_REQUEST = "DROP TABLE IF EXISTS $TABLE_USER"
    }

    private var db: SQLiteDatabase? = null
    private val dbHelper: DbHelperUser = DbHelperUser(context)

    fun openWritable(): UserDAO {
        db = dbHelper.writableDatabase
        return this
    }

    fun openReadable(): UserDAO {
        db = dbHelper.readableDatabase
        return this
    }

    fun close() {
        db?.close()
        dbHelper.close()
    }

    fun insert(user: User): Long {
        val values = ContentValues().apply {
            put(COLUMN_NOM, user.nom)
            put(COLUMN_PRENOM, user.prenom)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_ROLE, user.role)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_PHOTO, user.photo)
        }
        return db!!.insert(TABLE_USER, null, values)
    }

    fun deleteUser(id: Long) {
        db!!.delete(TABLE_USER, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun getAll(): List<User> {
        val userList = mutableListOf<User>()
        val cursor = db?.query(TABLE_USER, null, null, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                userList.add(fromCursor(cursor))
            } while (cursor.moveToNext())
        }

        cursor?.close()
        return userList
    }

    private fun fromCursor(cursor: Cursor): User {
        return User(
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOM)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRENOM)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
        )
    }
}
