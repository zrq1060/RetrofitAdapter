package com.zrq.retrofit.adapter

import com.zrq.retrofit.adapter.exception.ResponseCodeErrorException
import retrofit2.Response
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 19:01
 */
object ApiResponseHandlerManager {
    private val apiResponseResultHandlers = arrayListOf<ApiResponseResultHandler>()
    private val annotationHandlers = mutableMapOf<KClass<*>, ApiResponseAnnotationHandler>()

    fun add(handler: ApiResponseResultHandler) = apply {
        if (!apiResponseResultHandlers.contains(handler)) {
            apiResponseResultHandlers.add(handler)
        }
        // 增加的时候排序，以优化使用时排序
        apiResponseResultHandlers.sortByDescending { it.priority() }
    }

    fun remove(handler: ApiResponseResultHandler) = apply {
        if (apiResponseResultHandlers.contains(handler)) {
            apiResponseResultHandlers.remove(handler)
        }
    }

    fun clear() = apply {
        if (apiResponseResultHandlers.isNotEmpty()) {
            apiResponseResultHandlers.clear()
        }
    }

    internal fun <T> handleOnResponse(
        response: Response<T>,
        resultClass: Class<*>,
        annotationValue: KClass<*>?
    ): ApiResponse<T> {
        val handler = getHandler(resultClass, annotationValue)
        // 使用handler处理，如果没有，使用默认（网络）处理。
        return handler?.handleOnResponse(response) ?: if (response.isSuccessful) {
            // 网络成功
            ApiResponse.success(response.body())
        } else {
            // 网络失败
            ApiResponse.exception(ResponseCodeErrorException(response.code()))
        }
    }

    internal fun <T> handleOnFailure(
        t: Throwable,
        resultClass: Class<*>,
        annotationValue: KClass<*>?
    ): ApiResponse<T> {
        val handler = getHandler(resultClass, annotationValue)
        // 使用handler处理，如果没有，使用默认（网络）处理。
        return handler?.handleOnFailure(t) ?: ApiResponse.exception(t)
    }

    private fun getHandler(resultClass: Class<*>, annotationValue: KClass<*>?) =
        if (annotationValue == null) {
            // 用其它规则，获取符合规则的第一个
            apiResponseResultHandlers.firstOrNull { it.shouldHandle(resultClass) }
        } else {
            // 用指定规则
            if (annotationHandlers.containsKey(annotationValue)) {
                annotationHandlers[annotationValue]
            } else {
                val handler = annotationValue.createInstance() as ApiResponseAnnotationHandler
                annotationHandlers[annotationValue] = handler
                handler
            }
        }
}