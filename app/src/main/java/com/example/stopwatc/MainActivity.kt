package com.example.stopwatc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stopwatc.ui.main.StopwatchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, StopwatchFragment.newInstance())
                    .commitNow()
        }
    }
}