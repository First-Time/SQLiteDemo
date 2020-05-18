package com.lyf.sqlitedemo.advanced

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    private val TAG = "SQLite"
    private val CREATE_TABLE_SQL =
        "create table user_dict(_id integer primary key autoincrement, user_name, user_age)"

    constructor(context: Context?, name: String?, version: Int) : this(
        context,
        name,
        null,
        version
    )

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        println("$TAG onUpgrade oldVersion: $oldVersion newVersion: $newVersion")
    }
}