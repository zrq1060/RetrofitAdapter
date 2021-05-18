package com.zrq.retrofit.adapter.coroutines

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.ApiResponseCallHandle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 15:47
 */
class ApiResponseCallDelegate<T>(proxy: Call<T>, private val resultClass: Class<*>) :
    CallDelegate<T, ApiResponse<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<ApiResponse<T>>) = proxy.enqueue(
        object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@ApiResponseCallDelegate,
                    Response.success(ApiResponseCallHandle.handleOnResponse(response, resultClass))
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@ApiResponseCallDelegate,
                    Response.success(ApiResponseCallHandle.handleOnFailure(t, resultClass))
                )
            }
        }
    )

    override fun cloneImpl() = ApiResponseCallDelegate(proxy.clone(), resultClass)
}