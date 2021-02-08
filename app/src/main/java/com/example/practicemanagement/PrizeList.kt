package com.example.practicemanagement

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class PrizeList : AppCompatActivity() {

    lateinit var prize: PrizeManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var tvName: TextView
    lateinit var tvPrizeName: TextView
    lateinit var tvDate: TextView
    lateinit var tvContents: TextView
    lateinit var tvEtc: TextView

    lateinit var str_contestname: String
    lateinit var str_prizename: String
    lateinit var str_date: String
    lateinit var str_contents: String
    lateinit var str_etc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize_list)

        //뒤로가기 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)


        tvName = findViewById(R.id.contestname)
        tvPrizeName = findViewById(R.id.prizename)
        tvDate = findViewById(R.id.date)
        tvContents = findViewById(R.id.contents)
        tvEtc = findViewById(R.id.etc)

        val intent = intent
        str_contestname = intent.getStringExtra("intent_name").toString()

        prize = PrizeManager(this, "prize", null, 1)
        sqlitedb = prize.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM prize;", null)

        if (cursor.moveToNext()) {
            str_prizename = cursor.getString((cursor.getColumnIndex("prizename"))).toString()
            str_date = cursor.getString((cursor.getColumnIndex("date"))).toString()
            str_contents = cursor.getInt(cursor.getColumnIndex("contents")).toString()
            str_etc = cursor.getString((cursor.getColumnIndex("etc"))).toString()
        }

        cursor.close()
        sqlitedb.close()
        prize.close()

        tvName.text = str_contestname
        tvPrizeName.text = str_prizename
        tvDate.text = str_date
        tvContents.text = str_contents
        tvEtc.text = str_etc
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_prize_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_prize_delete -> {
                prize = PrizeManager(this, "prize", null, 1)
                sqlitedb = prize.readableDatabase

                sqlitedb.execSQL("DELETE FROM prize WHERE name='" + str_contestname + "';")
                sqlitedb.close()
                prize.close()

                val intent = Intent(this, CertificateView::class.java)
                startActivity(intent)
                return true
            }
            android.R.id.home -> {
                val intent = Intent(this, CertificateView::class.java)
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}