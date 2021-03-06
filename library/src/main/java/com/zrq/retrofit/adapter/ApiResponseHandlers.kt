package com.zrq.retrofit.adapter

import retrofit2.Response

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 11:54
 */
interface ApiResponseResultHandler : ApiResponseResultShouldHandle, ApiResponseHandle
interface ApiResponseAnnotationHandler : ApiResponseHandle

interface ApiResponseResultShouldHandle {
    /**
     * 优先级，值越大优先级越高，值相同按先添加的先判断
     */
    fun priority(): Int

    /**
     * 是否要处理，[resultClass]为[ApiResponse]的泛型类型
     */
    fun shouldHandle(resultClass: Class<*>): Boolean
}

interface ApiResponseHandle {
    /**
     * 处理onResponse，返回ApiResponse
     */
    fun <T> handleOnResponse(response: Response<T>): ApiResponse<T>

    /**
     * 处理onFailure，返回ApiResponse
     */
    fun <T> handleOnFailure(t: Throwable): ApiResponse<T>
}