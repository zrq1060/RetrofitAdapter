package com.zrq.retrofit.adapter.coroutines

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.ApiResponseHandlerManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.KClass

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 15:47
 */
class ApiResponseCallDelegate<T>(
    proxy: Call<T>,
    private val resultClass: Class<*>,
    private val annotationValue: KClass<*>?
) : CallDelegate<T, ApiResponse<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<ApiResponse<T>>) = proxy.enqueue(
        object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val result = ApiResponseHandlerManager.handleOnResponse(
                    response,
                    resultClass,
                    annotationValue
                )
                callback.onResponse(this@ApiResponseCallDelegate, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result = ApiResponseHandlerManager.handleOnFailure<T>(
                    t,
                    resultClass,
                    annotationValue
                )
                callback.onResponse(this@ApiResponseCallDelegate, Response.success(result))
            }
        }
    )

    override fun cloneImpl() = ApiResponseCallDelegate(proxy.clone(), resultClass, annotationValue)
}