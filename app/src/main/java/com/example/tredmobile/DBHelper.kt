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
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                FNAME_COl + " TEXT," +
                LNAME_COl + " TEXT," +
                AGE_COL + " TEXT," +
                EMAIL_COL + "TEXT," +
                TBUCKS_COL + "INT," +")")

        //exec query with SQLite
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
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
        values.put(FNAME_COl, fName)
        values.put(LNAME_COl, lName)
        values.put(AGE_COL, age)
        values.put(EMAIL_COL, email)
        values.put(TBUCKS_COL, tBucks)



        //create writable version of DB
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        //close db
        db.close()
    }

    //get all data from Table
    fun getAll(): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)

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
