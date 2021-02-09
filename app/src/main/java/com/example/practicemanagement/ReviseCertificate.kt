package com.example.practicemanagement

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class ReviseCertificate : AppCompatActivity() {

    lateinit var certificate: CertifiCateManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var edt_writeC_name: EditText
    lateinit var btn_writeC_selectDate: Button
    lateinit var calendarTextView: TextView
    lateinit var edt_writeC_selectPeriod: EditText
    lateinit var edt_writeC_etc: EditText
    lateinit var btn_writeC_picture: Button
    lateinit var img: ImageView
    lateinit var btn_writeC_file: Button
    lateinit var btn_writeC_revise: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revise_certificate)

        edt_writeC_name = findViewById(R.id.edt_writeC_name)
        btn_writeC_selectDate = findViewById(R.id.btn_writeC_selectDate)
        calendarTextView = findViewById(R.id.calendarTextView)
        edt_writeC_selectPeriod = findViewById(R.id.edt_writeC_selectPeriod)
        edt_writeC_etc = findViewById(R.id.edt_writeC_etc)
        btn_writeC_picture = findViewById(R.id.btn_writeC_picture)
        img = findViewById(R.id.img)
        btn_writeC_file = findViewById(R.id.btn_writeC_file)
        btn_writeC_revise = findViewById(R.id.btn_writeC_revise)

        //뒤로가기 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)

        //날짜선택 클릭했을때
        btn_writeC_selectDate.setOnClickListener{
            var today = Calendar.getInstance()
            var year: Int = today.get(Calendar.YEAR)
            var month: Int = today.get(Calendar.MONTH)
            var date: Int = today.get(Calendar.DATE)

            var listener = DatePickerDialog.OnDateSetListener{ datePicker, year, month, date ->
                calendarTextView.text = "${year}년 ${month + 1}월 ${date}일"
            }
            var picker = DatePickerDialog(this,listener,year,month,date)
            picker.show()
        }

        //DB 연동
        certificate= CertifiCateManager(this,"certificate",null,1)

        //사진첨부 클릭했을때
        btn_writeC_picture.setOnClickListener{
            openGallery()               //openGallery함수 호출
        }

        //수정완료 클릭했을때
        btn_writeC_revise.setOnClickListener{
            var str_name: String = edt_writeC_name.text.toString()
            //var str_date: String = calendarTextView.text.toString()
            var str_date: String =" "
            var str_period: String = edt_writeC_selectPeriod.text.toString()
            var str_etc: String = edt_writeC_etc.text.toString()

            if(calendarTextView.text !==null){
                str_date = calendarTextView.text.toString()
            }

            sqlitedb = certificate.readableDatabase
            sqlitedb.execSQL("UPDATE certificate SET date='"+str_date +"', period='"
                    +str_period+"',etc='"+str_etc
                    +"' WHERE name = '"+str_name+"';")
            sqlitedb.close()

            val intent = Intent(this,CertificateView::class.java)
            intent.putExtra("intent_name",str_name)
            startActivity(intent)
        }
    }
    private val OPEN_GALLERY = 1

    private fun openGallery(){
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent,OPEN_GALLERY)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK){
            if(requestCode==OPEN_GALLERY){
                var currentImageUrl : Uri? = data?.data

                try {
                    val bitmap= MediaStore.Images.Media.getBitmap(contentResolver,currentImageUrl)
                    img.setImageBitmap(bitmap)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }else{
            Log.d("ActivityResult","sth wrong")
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