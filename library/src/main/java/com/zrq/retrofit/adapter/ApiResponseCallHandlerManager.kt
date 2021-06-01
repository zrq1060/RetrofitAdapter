package com.zrq.retrofit.adapter

import retrofit2.Response

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 19:01
 */
object ApiResponseCallHandlerManager {
    private val handlers = arrayListOf<ApiResponseCallHandler>()

    fun add(handler: ApiResponseCallHandler) {
        if (!handlers.contains(handler)) {
            handlers.add(handler)
        }
        // 增加的时候排序，以优化使用时排序
        handlers.sortBy { it.priority() }
    }

    fun remove(handler: ApiResponseCallHandler) {
        if (handlers.contains(handler)) {
            handlers.remove(handler)
        }
    }

    fun clear() {
        if (handlers.isNotEmpty()) {
            handlers.clear()
        }
    }

    internal fun <T> handleOnResponse(response: Response<T>, resultClass: Class<*>): ApiResponse<T> {
        val handler = handlers.firstOrNull { it.isHandle(resultClass) }
        // 使用handler处理，如果没有，使用默认（网络）处理。
        return handler?.handleOnResponse(response) ?: if (response.isSuccessful) {
            // 网络成功
            ApiResponse.success(response.body())
        } else {
            // 网络失败
            ApiResponse.exception(ResponseCodeErrorException(response.code()))
        }
    }

    internal fun <T> handleOnFailure(t: Throwable, resultClass: Class<*>): ApiResponse<T> {
        val handler = handlers.firstOrNull { it.isHandle(resultClass) }
        // 使用handler处理，如果没有，使用默认（网络）处理。
        return handler?.handleOnFailure(t) ?: ApiResponse.exception(t)
    }
}