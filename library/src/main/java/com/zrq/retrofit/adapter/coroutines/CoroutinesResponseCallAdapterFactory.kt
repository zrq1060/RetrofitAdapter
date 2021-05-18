package com.zrq.retrofit.adapter.coroutines

import com.zrq.retrofit.adapter.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 15:39
 */
class CoroutinesResponseCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return when (getRawType(returnType)) {
            Call::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                when (getRawType(callType)) {
                    ApiResponse::class.java -> {
                        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                        val resultClass = getRawType(resultType) // Class
                        CoroutinesResponseCallAdapter(resultType, resultClass)
                    }
                    else -> null
                }
            }
            else -> null
        }
    }

    companion object {

        @JvmStatic
        fun create() = CoroutinesResponseCallAdapterFactory()
    }
}