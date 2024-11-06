package com.example.animalmanager.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.animalmanager.db.DbHelperAnimal
import com.example.animalmanager.models.Animal

class AnimalDAO(context: Context) {

    companion object {
        private const val TABLE_ANIMAL = "animal"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_SEXE = "sexe"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_NOM = "nom"
        private const val COLUMN_IS_FED = "isFed"

        const val CREATE_REQUEST = """
            CREATE TABLE $TABLE_ANIMAL (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TYPE TEXT NOT NULL,
                $COLUMN_SEXE TEXT NOT NULL,
                $COLUMN_AGE TEXT NOT NULL,
                $COLUMN_NOM TEXT NOT NULL,
                $COLUMN_IS_FED INTEGER NOT NULL
            )
        """

        const val UPGRADE_REQUEST = "DROP TABLE IF EXISTS $TABLE_ANIMAL"
    }

    private var db: SQLiteDatabase? = null
    private val dbHelper: DbHelperAnimal = DbHelperAnimal(context)

    fun openWritable(): AnimalDAO {
        db = dbHelper.writableDatabase
        return this@AnimalDAO
    }

    fun openReadable(): AnimalDAO {
        db = dbHelper.readableDatabase // Mode lecture seule
        return this@AnimalDAO
    }

    fun close() {
        db?.close()
        dbHelper.close()
    }

    fun insert(animal: Animal): Long {
        val values = ContentValues().apply {
            put(COLUMN_TYPE, animal.type)
            put(COLUMN_SEXE, animal.sexe)
            put(COLUMN_AGE, animal.age)
            put(COLUMN_NOM, animal.nom)
            put(COLUMN_IS_FED, if (animal.isFed) 1 else 0) // Conversion en int
        }
        return db!!.insert(TABLE_ANIMAL, null, values)
    }

    fun update(animal: Animal): Int {
        val values = ContentValues().apply {
            put(COLUMN_TYPE, animal.type)
            put(COLUMN_SEXE, animal.sexe)
            put(COLUMN_AGE, animal.age)
            put(COLUMN_NOM, animal.nom)
            put(COLUMN_IS_FED, if (animal.isFed) 1 else 0) // Conversion en int
        }
        // id de l'animal
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(animal.id.toString())
        return db!!.update(TABLE_ANIMAL, values, selection, selectionArgs)
    }

    fun deleteAnimal(id: Int) {
        db!!.delete(TABLE_ANIMAL, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // retrouver animal avec ID
    fun getById(id: Long): Animal? {
        val cursor = db?.query(
            TABLE_ANIMAL,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()), null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val animal = fromCursor(cursor)
            cursor.close()
            animal

        } else {
            cursor?.close()
            null
        }
    }

    private fun fromCursor(cursor: Cursor): Animal {
        return Animal(
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEXE)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOM)),
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FED)) == 1 // Conversion en booléen
        )
    }

    fun getAll(): List<Animal> {
        val animalList = mutableListOf<Animal>()
        val cursor = db?.query(
            TABLE_ANIMAL, null, null, null, null, null, null
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                animalList.add(fromCursor(cursor))
            } while (cursor.moveToNext())
        }

        cursor?.close()
        return animalList
    }

    fun animalFav(): String? {
        val animals = getAll()
        return animals
            .groupingBy { animal -> animal.type }
            .eachCount()
            .maxByOrNull { entry -> entry.value }
            ?.key
    }

    fun nbAnimalNourris(): Int {
        val animals = getAll()
        return animals.count { animal -> animal.isFed }
    }

}
