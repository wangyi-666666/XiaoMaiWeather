package com.example.xiaomaiweather

import android.Manifest
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_place.*

class MainActivity : AppCompatActivity() {

    @get:TargetApi(23)
    private val permissions: Unit
        private get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Dexter.withActivity(this)
                        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(CompositeMultiplePermissionsListener())
                        .check()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissions



        setContentView(R.layout.activity_main)


    }
}