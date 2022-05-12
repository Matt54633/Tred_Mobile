package com.example.tredmobile

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // Create DB for User
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
/*
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                FNAME_COl + " TEXT," +
                LNAME_COl + " TEXT," +
                AGE_COL + " TEXT," +
                EMAIL_COL + "TEXT," +
                TBUCKS_COL + "INT" +")")

 */
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                FNAME_COl + " TEXT," +
                LNAME_COl + " TEXT" +")")

        //exec query with SQLite
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVer: Int, newVer: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Adds user data to DB
    fun addUserData(fName : String, lName : String, age : String, email : String, tBucks : Int ){

        //create content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        /*
        values.put(FNAME_COl, fName)
        values.put(LNAME_COl, lName)
        values.put(AGE_COL, age)
        values.put(EMAIL_COL, email)
        values.put(TBUCKS_COL, tBucks)

         */
        values.put(FNAME_COl, fName)
        values.put(LNAME_COl, lName)


        //create writable version of DB
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        //close db
        db.close()
    }

    //get all data from Table
    fun getAll(): List<userDataModel> {
        val results = arrayListOf<userDataModel>()

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase //MAYBE WRITEABLE?

        // below code returns a cursor to
        // read data from the database
        val selectQuery = ("SELECT * FROM $TABLE_NAME")
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val qResults = userDataModel()
                    qResults.id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ID_COL)))
                    qResults.fName = cursor.getString(cursor.getColumnIndexOrThrow(FNAME_COl))
                    qResults.lName = cursor.getString(cursor.getColumnIndexOrThrow(LNAME_COl))
                    results.add(qResults)
                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        return results


    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private const val DATABASE_NAME = "Tred_Mobile"

        // below is the variable for database version
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "User"

        const val ID_COL = "id"

        const val FNAME_COl = "first_name"

        const val LNAME_COl = "last_name"

        const val AGE_COL = "age"

        const val TBUCKS_COL = "tred_bucks"

        const val EMAIL_COL = "email"

    }
}
