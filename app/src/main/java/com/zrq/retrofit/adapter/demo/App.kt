package com.zrq.retrofit.adapter.demo

import androidx.multidex.MultiDexApplication
import com.zrq.retrofit.adapter.ApiResponseHandlerManager
import com.zrq.retrofit.adapter.demo.handler.ApiOpenBaseResultApiResponseResultHandler
import com.zrq.retrofit.adapter.demo.handler.WanAndroidBaseDataApiResponseResultHandler

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 14:22
 */
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        // 增加自定义规则
        ApiResponseHandlerManager
            .add(WanAndroidBaseDataApiResponseResultHandler()) // -WanAndroid的网络请求规则
            .add(ApiOpenBaseResultApiResponseResultHandler()) // -ApiOpen的网络请求规则
    }
}