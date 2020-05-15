package com.lyf.sqlitedemo.basic

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lyf.sqlitedemo.R
import kotlinx.android.synthetic.main.activity_basic.*

private const val tableName = "user"
private const val nameColumn = "user_name"
private const val ageColumn = "user_age"

class BasicActivity : AppCompatActivity() {
    private lateinit var db: SQLiteDatabase
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: BasicAdapter
    private var dataList: MutableList<InfoModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)

        db = SQLiteDatabase.openOrCreateDatabase("$filesDir/my.db3", null)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        mAdapter = BasicAdapter(dataList)
        recyclerView.adapter = mAdapter

        initListener()

        inflateData(queryCursor())
    }

    private fun initListener() {
        btnAdd.setOnClickListener {

            val name = etName.text.toString()
            val age = etAge.text.toString().toInt()

            val insertValue = insertUser(db, name, age)
            if (insertValue == -1L) {
                db.execSQL(
                    "create table user(_id integer primary key autoincrement, $nameColumn varchar(50), $ageColumn integer)"
                )
                insertUser(db, name, age)
            }

            inflateData(queryCursor())
        }
    }

    private fun queryCursor(): Cursor? {
        var cursor: Cursor? = null
        try {
            cursor =
                db.query(tableName, arrayOf(nameColumn, ageColumn), null, null, null, null, null)
        } catch (e: Exception) {
        }
        return cursor
    }

    private fun insertUser(db: SQLiteDatabase, name: String, age: Int): Long {
        val contentValues = ContentValues()
        contentValues.put(nameColumn, name)
        contentValues.put(ageColumn, age)
        return db.insert(tableName, null, contentValues)
    }

    private fun inflateData(cursor: Cursor?) {
        if (cursor != null) {
            dataList.clear()
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(0)
                    val age = cursor.getInt(1)
                    dataList.add(InfoModel(name, age))
                } while (cursor.moveToNext())
            }
            mAdapter.notifyDataSetChanged()
        }
    }
}
