package com.example.animalmanager.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.animalmanager.dao.UserDAO

class DbHelperUser(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "user.db"
        const val DATABASE_VERSION = 2
        const val UPGRADE_REQUEST = "DROP TABLE IF EXISTS ${UserDAO.TABLE_USER}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UserDAO.CREATE_REQUEST) // Crée la table
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UPGRADE_REQUEST)
        onCreate(db)
    }
}
