package com.example.practicemanagement

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import org.w3c.dom.Text

class CertificateView : AppCompatActivity() {

    lateinit var certificate: CertifiCateManager
    lateinit var prize: PrizeManager
    lateinit var certificatesqlitedb: SQLiteDatabase        //certificatedb연동
    lateinit var prizesqlitedb: SQLiteDatabase              //certificatedb연동
    lateinit var layout: LinearLayout                       //certificate layout
    lateinit var prizelayout: LinearLayout                  //prize layout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate_view)

        //뒤로가기 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)

        //certificate 보여주기
        certificate = CertifiCateManager(this,"certificate",null,1)
        certificatesqlitedb = certificate.readableDatabase

        layout = findViewById(R.id.certificate)

        var cursor : Cursor
        cursor = certificatesqlitedb.rawQuery("SELECT * FROM certificate;",null)

        var num:Int=0

        while(cursor.moveToNext()){
            var str_name = cursor.getString(cursor.getColumnIndex("name")).toString()
            var str_date = cursor.getString(cursor.getColumnIndex("date")).toString()
            var str_period = cursor.getString(cursor.getColumnIndex("period")).toString()
            var str_etc = cursor.getString(cursor.getColumnIndex("etc")).toString()

            var layout_item: LinearLayout = LinearLayout(this)
            layout_item.orientation = LinearLayout.VERTICAL
            layout_item.id=num

            //우선 제목만 보여주고
            var tvName:TextView = TextView(this)
            tvName.text = str_name
            tvName.textSize = 30f
            layout_item.addView(tvName)

            //제목 클릭하면 펼치기
            layout_item.setOnClickListener(){

                var tvDate:TextView = TextView(this)
                tvDate.text = "취득일: "+ str_date
                layout_item.addView(tvDate)

                var tvPeriod:TextView = TextView(this)
                tvPeriod.text = "유효 기간:" +str_period
                layout_item.addView(tvPeriod)

                var tvEtc:TextView = TextView(this)
                tvEtc.text ="비고"+str_etc
                layout_item.addView(tvEtc)

            }
            layout_item.setOnClickListener{
                val intent = Intent(this,CertificateList::class.java)
                intent.putExtra("intent_name",str_name)
                startActivity(intent)
            }

            layout.addView(layout_item)
            num++
        }

        cursor.close()
        certificatesqlitedb.close()
        certificate.close()


        //prize 보여주기
        prize = PrizeManager(this,"prize",null,1)
        prizesqlitedb = prize.readableDatabase

        prizelayout = findViewById(R.id.prize)

        var prizecursor : Cursor
        prizecursor = prizesqlitedb.rawQuery("SELECT * FROM prize;",null)

        var prizenum:Int=0

        while(prizecursor.moveToNext()){
            var str_contestname = prizecursor.getString(prizecursor.getColumnIndex("name")).toString()
            var str_prizename = prizecursor.getString(prizecursor.getColumnIndex("prizename")).toString()
            var str_date = prizecursor.getString(prizecursor.getColumnIndex("date")).toString()
            var str_contents = prizecursor.getString(prizecursor.getColumnIndex("contents")).toString()
            var str_etc = prizecursor.getString(prizecursor.getColumnIndex("etc")).toString()

            var layout_item: LinearLayout = LinearLayout(this)
            layout_item.orientation = LinearLayout.VERTICAL
            layout_item.id=num

            //우선 제목만 보여주고
            var tvName:TextView = TextView(this)
            tvName.text = str_contestname
            tvName.textSize = 30f
            layout_item.addView(tvName)

            //제목 클릭하면 펼치기
            layout_item.setOnClickListener(){
                var tvPrizeName:TextView = TextView(this)
                tvPrizeName.text = "취득일: "+ str_prizename
                layout_item.addView(tvPrizeName)

                var tvDate:TextView = TextView(this)
                tvDate.text = "취득일: "+ str_date
                layout_item.addView(tvDate)

                var tvContents:TextView = TextView(this)
                tvContents.text = "유효 기간:" +str_contents
                layout_item.addView(tvContents)

                var tvEtc:TextView = TextView(this)
                tvEtc.text ="비고: "+str_etc
                layout_item.addView(tvEtc)
            }

            prizelayout.addView(layout_item)
            prizenum++
        }

        prizecursor.close()
        prizesqlitedb.close()
        prize.close()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_certificate,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_write_certificate -> {
                val intent = Intent(this,WriteCertificate::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_write_prize-> {
                val intent = Intent(this,WritePrize::class.java)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}