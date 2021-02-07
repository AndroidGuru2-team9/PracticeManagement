package com.example.practicemanagement

import android.app.ActionBar
import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toolbar
import java.util.*

class WriteCertificate : AppCompatActivity() {

    lateinit var certificate: CertifiCateManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var btn_writeC_certificate: Button
    lateinit var btn_writeC_prize: Button
    lateinit var edt_writeC_name: EditText
    lateinit var btn_writeC_selectDate: Button
    lateinit var calendarTextView: TextView
    lateinit var edt_writeC_selectPeriod: EditText
    lateinit var edt_writeC_etc: EditText
    lateinit var btn_writeC_picture: Button
    lateinit var btn_writeC_file: Button
    lateinit var btn_writeC_complete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_certificate)

        btn_writeC_certificate = findViewById(R.id.btn_writeC_certificate)
        btn_writeC_prize = findViewById(R.id.btn_writeC_prize)
        edt_writeC_name = findViewById(R.id.edt_writeC_name)
        btn_writeC_selectDate = findViewById(R.id.btn_writeC_selectDate)
        calendarTextView = findViewById(R.id.calendarTextView)
        edt_writeC_selectPeriod = findViewById(R.id.edt_writeC_selectPeriod)
        edt_writeC_etc = findViewById(R.id.edt_writeC_etc)
        btn_writeC_picture = findViewById(R.id.btn_writeC_picture)
        btn_writeC_file = findViewById(R.id.btn_writeC_file)
        btn_writeC_complete = findViewById(R.id.btn_writeC_complete)

        //뒤로가기 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)

        //날짜선택 클릭했을때
        btn_writeC_selectDate.setOnClickListener{
            var today = Calendar.getInstance()
            var year: Int = today.get(Calendar.YEAR)
            var month: Int = today.get(Calendar.MONTH)
            var date: Int = today.get(Calendar.DATE)

            var listener = DatePickerDialog.OnDateSetListener{datePicker,year,month,date ->
                calendarTextView.text = "${year}년 ${month + 1}월 ${date}일"
            }
            var picker = DatePickerDialog(this,listener,year,month,date)
            picker.show()
        }

        //DB 연동
        certificate= CertifiCateManager(this,"certificate",null,1)

        //작성완료 클릭했을때때
        btn_writeC_complete.setOnClickListener{
            var str_name: String = edt_writeC_name.text.toString()
            //var str_date: String = calendarTextView.text.toString()
            var str_date: String =" "
            var str_period: String = edt_writeC_selectPeriod.text.toString()
            var str_etc: String = edt_writeC_etc.text.toString()

            if(calendarTextView.text !==null){
                str_date = calendarTextView.text.toString()
            }

                sqlitedb = certificate.writableDatabase
                sqlitedb.execSQL("INSERT INTO certificate VALUES ('" + str_name +"','"
                        + str_date + "',"+"'"+str_period+"'"+",'"+str_etc+"');")
                sqlitedb.close()

        val intent = Intent(this,CertificateView::class.java)
        intent.putExtra("intent_name",str_name)
        startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                val intent = Intent(this,CertificateView::class.java)
                startActivity(intent)
                return true
            }
            else->{
                return super.onOptionsItemSelected(item)
            }
        }
    }
}