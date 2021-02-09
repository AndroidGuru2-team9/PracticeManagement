package com.example.practicemanagement

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class CertificateList : AppCompatActivity() {

    lateinit var certificate: CertifiCateManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var tvName: TextView
    lateinit var tvDate: TextView
    lateinit var tvPeriod: TextView
    lateinit var tvEtc: TextView
    lateinit var tvImg: ImageView

    lateinit var str_name: String
    lateinit var str_date: String
    lateinit var str_period: String
    lateinit var str_etc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate_list)

        //뒤로가기 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)

        tvName = findViewById(R.id.name)
        tvDate = findViewById(R.id.date)
        tvPeriod = findViewById(R.id.period)
        tvEtc = findViewById(R.id.etc)

        val intent = intent
        str_name = intent.getStringExtra("intent_name").toString()

        certificate = CertifiCateManager(this, "certificate", null, 1)
        sqlitedb = certificate.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM certificate;", null)

        if (cursor.moveToNext()) {
            str_date = cursor.getString((cursor.getColumnIndex("date"))).toString()
            str_period = cursor.getInt(cursor.getColumnIndex("period")).toString()
            str_etc = cursor.getString((cursor.getColumnIndex("etc"))).toString()
        }

        cursor.close()
        sqlitedb.close()
        certificate.close()

        tvName.text = str_name
        tvDate.text = str_date
        tvPeriod.text = str_period
        tvEtc.text = str_etc

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_certificate_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_certificate_delete -> {
                certificate = CertifiCateManager(this, "certificate", null, 1)
                sqlitedb = certificate.readableDatabase

                sqlitedb.execSQL("DELETE FROM certificate WHERE name='" + str_name + "';")
                sqlitedb.close()
                certificate.close()

                val intent = Intent(this, CertificateView::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_certificate_revise ->{
                val intent = Intent(this,ReviseCertificate::class.java)
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