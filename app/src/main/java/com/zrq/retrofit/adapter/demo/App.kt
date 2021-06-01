package com.zrq.retrofit.adapter.demo

import android.app.Application
import com.zrq.retrofit.adapter.ApiResponseCallHandlerManager
import com.zrq.retrofit.adapter.demo.handler.BaseDataApiResponseCallHandler
import com.zrq.retrofit.adapter.demo.handler.BaseResultApiResponseCallHandler

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 14:22
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // 增加自定义规则
        ApiResponseCallHandlerManager.add(BaseDataApiResponseCallHandler())
        ApiResponseCallHandlerManager.add(BaseResultApiResponseCallHandler())
    }
}