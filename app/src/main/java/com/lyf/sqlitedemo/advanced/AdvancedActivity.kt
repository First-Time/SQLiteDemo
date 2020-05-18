package com.lyf.sqlitedemo.advanced

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lyf.sqlitedemo.BasicAdapter
import com.lyf.sqlitedemo.InfoModel
import com.lyf.sqlitedemo.R
import kotlinx.android.synthetic.main.activity_advanced.*
import kotlinx.android.synthetic.main.activity_basic.btnAdd
import kotlinx.android.synthetic.main.activity_basic.etAge
import kotlinx.android.synthetic.main.activity_basic.etName
import kotlinx.android.synthetic.main.activity_basic.recyclerView

private const val tableName = "user_dict"
private const val nameColumn = "user_name"
private const val ageColumn = "user_age"

class AdvancedActivity : AppCompatActivity() {
    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: BasicAdapter
    private var dataList: MutableList<InfoModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)
        //创建MyDatabaseHelper对象，指定数据库版本为1，此处使用相对路径即可
        //数据库文件自动保存在程序的数据文件夹的databases目录下
//        dbHelper = MyDatabaseHelper(this, "userDict.db3", 1)
        dbHelper = MyDatabaseHelper(this, "userDict.db3", 2)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        mAdapter = BasicAdapter(dataList)
        recyclerView.adapter = mAdapter

        initListener()
    }

    private fun initListener() {
        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val age = etAge.text.toString().toInt()

            val insertValue = insertUser(dbHelper.writableDatabase, name, age)
            if (insertValue != -1L) {
                Toast.makeText(this, "添加用户信息成功", Toast.LENGTH_SHORT)
            }
        }

        btnSearch.setOnClickListener {
            val searchKey = etSearch.text.toString()
            inflateData(queryCursor(searchKey))
        }
    }

    private fun queryCursor(key: String): Cursor? {
        var cursor: Cursor? = null
        try {
            cursor =
                dbHelper.readableDatabase.query(
                    tableName,
                    arrayOf(nameColumn, ageColumn),
                    "$nameColumn like ? or $ageColumn like ?",
                    arrayOf("%$key%", "%$key%"),
                    null,
                    null,
                    null
                )
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
            while (cursor.moveToNext()) {
                val name = cursor.getString(0)
                val age = cursor.getInt(1)
                dataList.add(InfoModel(name, age))
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 退出程序时关闭MyDatabaseHelper里的SQLiteDatabase
        if (dbHelper != null) {
            dbHelper.close()
        }
    }
}
