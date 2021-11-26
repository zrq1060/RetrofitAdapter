package com.zrq.retrofit.adapter.demo.handler

import com.zrq.retrofit.adapter.*
import com.zrq.retrofit.adapter.exception.ResponseCodeErrorException
import retrofit2.Response

/**
 * 描述：网络逻辑处理类，网络成功，代表成功
 *
 * @author zhangrq
 * createTime 2021/5/17 15:15
 */
class NetworkApiResponseHandler : ApiResponseAnnotationHandler {

    override fun <T> handleOnResponse(response: Response<T>): ApiResponse<T> {
        return if (response.isSuccessful) {
            // 网络成功
            ApiResponse.success(response.body())
        } else {
            // 网络失败
            ApiResponse.exception(ResponseCodeErrorException(response.code()))
        }
    }

    override fun <T> handleOnFailure(t: Throwable): ApiResponse<T> {
        return ApiResponse.exception(t)
    }
}