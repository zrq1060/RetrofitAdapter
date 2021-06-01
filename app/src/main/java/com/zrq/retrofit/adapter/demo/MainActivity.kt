package com.zrq.retrofit.adapter.demo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val viewModel: TestViewModel by viewModels()
    private val container by lazy { findViewById<ViewGroup>(R.id.container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // observe
        val hintTv = findViewById<TextView>(R.id.hintTv)
        viewModel.hintText.observe(this) { hintTv.text = it }
        // buttons
        addButton("清除") { viewModel.clearHint() }
        addButton("返回ApiResponse_命令式处理") { viewModel.getBaseResultList_ApiResponse_Imperative() }
        addButton("返回ApiResponse_声明式处理") { viewModel.getBaseResultList_ApiResponse_Declarative() }

        addButton("返回Result") { viewModel.getBaseResultList_Result() }
        addButton("返回Result_不为空") { viewModel.getBaseResultList_Result_NotNull() }
        addButton("返回Result_数据为BaseResult内result") { viewModel.getBaseResultList_Result_BaseResultOfResult() }

        addButton("返回ApiResponse_命令式处理_BaseData") { viewModel.getBaseDataData_ApiResponse_Imperative() }
        addButton("返回Result_数据为BaseData内data") { viewModel.getBaseDataData_Result_BaseDataOfData() }

        addButton("获取A、B线性执行的结果-简单") { viewModel.getABLinearSimple() }
        addButton("获取A、B线性执行的结果") { viewModel.getABLinear() }

        addButton("获取A、B、C并发执行的结果-简单") { viewModel.getABCAsyncSimple() }
        addButton("获取A、B、C并发执行的结果") { viewModel.getABCAsync() }
    }

    private fun addButton(content: String, click: View.OnClickListener) {
        val button = Button(applicationContext).apply {
            isAllCaps = false
            text = content
            setOnClickListener(click)
        }
        container.addView(button, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }
}