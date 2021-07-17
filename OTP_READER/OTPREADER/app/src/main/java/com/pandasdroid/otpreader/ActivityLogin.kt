package com.pandasdroid.otpreader

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.judemanutd.autostarter.AutoStartPermissionHelper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ActivityLogin : AppCompatActivity(), View.OnClickListener {
    private var et_username: EditText? = null

    //private var et_password: EditText? = null
    private var btn_login: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initview()
        btn_login!!.setOnClickListener(this)

        val spref = getSharedPreferences(Constants.Key_Pref, Context.MODE_PRIVATE);

        if (AutoStartPermissionHelper.getInstance()
                .isAutoStartPermissionAvailable(this@ActivityLogin)
        ) {
            AutoStartPermissionHelper.getInstance().getAutoStartPermission(this@ActivityLogin)
        }

        getReceiveSMSPermission()
        getReadSMSPermission()
    }

    private fun startserv() {
        val serviceIntent = Intent(this@ActivityLogin, MyService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    fun getReadSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkReadSMSPermission()) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_SMS), 100
                )
                return
            }
        }
    }


    private fun getReceiveSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkReadSMSPermission()) {
                requestPermissions(
                    arrayOf(Manifest.permission.RECEIVE_SMS), 100
                )
                return
            }
        }
    }

    private fun checkReadSMSPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun initview() {
        et_username = findViewById(R.id.et_username)
        //et_password = findViewById(R.id.et_password)
        btn_login = findViewById(R.id.btn_login)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_login) {
            if (et_username!!.text.toString().length == 0){
                Toast.makeText(this@ActivityLogin, "Enter Username", Toast.LENGTH_SHORT).show()
                return
            }
            val retrofit = Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson())).build()
            val api = retrofit.create(
                Api::class.java
            )
            val call = api.Login(et_username!!.text.toString())
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    if (response.body() != null) {
                        val str = response.body()!!.string()
                        Log.wtf("Response",str)
                        if (str.contains("Login Success")) {
                            getSharedPreferences("App", MODE_PRIVATE).edit().putString("key", et_username!!.text.toString()).apply()
                            startserv()
                            startActivity(Intent(this@ActivityLogin,ActivityUsers :: class.java))
                        } else {
                            Toast.makeText(this@ActivityLogin, "Login Failed!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@ActivityLogin, "Login Failed!", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {

                    Toast.makeText(
                        this@ActivityLogin,
                        "Failed to get Messages"+t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(this, "Invalid UserName or Password", Toast.LENGTH_SHORT).show()
        }
    }
}