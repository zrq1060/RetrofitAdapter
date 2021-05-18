package com.zrq.retrofit.adapter.coroutines

import com.zrq.retrofit.adapter.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 15:42
 */
class CoroutinesResponseCallAdapter(private val resultType: Type, private val resultClass: Class<*>) :
    CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type {
        return resultType
    }

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return ApiResponseCallDelegate(call, resultClass)
    }
}