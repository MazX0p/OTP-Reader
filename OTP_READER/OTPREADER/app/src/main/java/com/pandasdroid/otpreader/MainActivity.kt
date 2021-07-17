package com.pandasdroid.otpreader

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.judemanutd.autostarter.AutoStartPermissionHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spref = getSharedPreferences("App", Context.MODE_PRIVATE);

        if (spref.getBoolean("FirstTime", true)){
            if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this@MainActivity)){
                AutoStartPermissionHelper.getInstance().getAutoStartPermission(this@MainActivity)
            }
            spref.edit().putBoolean("FirstTime", false).apply()
        }
    }
}