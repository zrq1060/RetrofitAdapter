package com.zrq.retrofit.adapter.demo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val viewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // observe
        val hintTv = findViewById<TextView>(R.id.hintTv)
        viewModel.hintText.observe(this) { hintTv.text = it }
        // buttons click
        findViewById<View>(R.id.button0).setOnClickListener { viewModel.clearHint() }
        findViewById<View>(R.id.button1).setOnClickListener { viewModel.getOneNetRequestData1() }
        findViewById<View>(R.id.button2).setOnClickListener { viewModel.getOneNetRequestData2() }
    }
}