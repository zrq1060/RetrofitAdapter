package com.zrq.retrofit.adapter.demo

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding

class MainActivity : AppCompatActivity() {
    private val viewModel: TestViewModel by viewModels()
    private val container by lazy { findViewById<ViewGroup>(R.id.container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // findViewById
        val hintTv = findViewById<TextView>(R.id.hintTv)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        // observe
        viewModel.hintText.observe(this) { hintTv.text = it }
        viewModel.loading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        // buttons
        addButton("清除") { viewModel.clearHint() }
        setMargin()
        addButton("ApiOpen接口_原始_Call") { viewModel.getApiOpenList_Call() }
        addButton("ApiOpen接口_原始_Suspend") { viewModel.getApiOpenList_Suspend() }
        setMargin()
        addButton("ApiOpen接口_返回ApiResponse_命令式处理") { viewModel.getApiOpenList_ApiResponse_Imperative() }
        addButton("ApiOpen接口_返回ApiResponse_声明式处理") { viewModel.getApiOpenList_ApiResponse_Declarative() }
        setMargin()
        addButton("ApiOpen接口_返回ApiResponse_指定规则") { viewModel.getApiOpenList_ApiResponse_ApiResponseHandler() }
        addButton("ApiOpen接口_返回ApiResponse_map转化处理") { viewModel.getApiOpenList_ApiResponse_Map() }
        setMargin()
        addButton("ApiOpen接口_返回Result") { viewModel.getApiOpenList_Result() }
        addButton("ApiOpen接口_返回Result_不为空") { viewModel.getApiOpenList_Result_NotNull() }
        addButton("ApiOpen接口_返回Result_指定返回值") { viewModel.getApiOpenList_Result_ApiOpen() }
        setMargin()
        addButton("WanAndroid接口_返回ApiResponse_命令式处理") { viewModel.getWanAndroidList_ApiResponse_Imperative() }
        addButton("WanAndroid接口_返回Result_指定返回值") { viewModel.getWanAndroidList_Result_WanAndroid() }
        setMargin()
        addButton("获取A、B线性执行的结果-简单") { viewModel.getABLinearSimple() }
        addButton("获取A、B线性执行的结果") { viewModel.getABLinear() }
        setMargin()
        addButton("获取A、B、C并发执行的结果") { viewModel.getABCAsync() }
    }

    private fun addButton(content: String, click: View.OnClickListener) {
        val button = Button(applicationContext).apply {
            isAllCaps = false
            text = content
            gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
            updatePadding(70)
            setOnClickListener(click)
        }
        container.addView(button, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    private fun setMargin() {
        if (container.childCount > 0) {
            val lastView = container.getChildAt(container.childCount - 1)
            (lastView.layoutParams as ViewGroup.MarginLayoutParams).setMargins(0, 0, 0, 40)
        }
    }
}