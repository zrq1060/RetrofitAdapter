package com.zrq.retrofit.adapter

import retrofit2.Response

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 19:01
 */
object ApiResponseCallHandle {
    private val handles = arrayListOf<ApiResponseCallHandler>()

    fun add(handler: ApiResponseCallHandler) {
        if (!handles.contains(handler)) {
            handles.add(handler)
        }
        // 增加的时候排序，以优化使用时排序
        handles.sortBy { it.priority() }
    }

    fun remove(handler: ApiResponseCallHandler) {
        if (handles.contains(handler)) {
            handles.remove(handler)
        }
    }

    fun clear() {
        if (handles.isNotEmpty()) {
            handles.clear()
        }
    }

    fun <T> handleOnResponse(response: Response<T>, resultClass: Class<*>): ApiResponse<T> {
        val handler = handles.firstOrNull { it.isHandle(resultClass) }
        // 使用handler处理，如果没有，使用默认（网络）处理。
        return handler?.handleOnResponse(response) ?: if (response.isSuccessful) {
            // 网络成功
            ApiResponse.success(response.body())
        } else {
            // 网络失败
            ApiResponse.exception(ResponseCodeErrorException(response.code()))
        }
    }

    fun <T> handleOnFailure(t: Throwable, resultClass: Class<*>): ApiResponse<T> {
        val handler = handles.firstOrNull { it.isHandle(resultClass) }
        // 使用handler处理，如果没有，使用默认（网络）处理。
        return handler?.handleOnFailure(t) ?: ApiResponse.exception(t)
    }
}