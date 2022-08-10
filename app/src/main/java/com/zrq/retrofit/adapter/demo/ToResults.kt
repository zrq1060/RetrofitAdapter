package com.zrq.retrofit.adapter.demo

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.demo.entity.wanandroid.WanAndroidBaseModel
import com.zrq.retrofit.adapter.demo.entity.apiopen.ApiOpenBaseModel
import com.zrq.retrofit.adapter.demo.handler.WanAndroidApiResponseResultHandler
import com.zrq.retrofit.adapter.demo.handler.ApiOpenApiResponseResultHandler

/**
 * 描述：转换成Result
 *
 * @author zhangrq
 * createTime 2021/5/28 16:35
 */

/**
 * toResult，把[ApiResponse]转成[Result]
 */
fun <T> ApiResponse<T>.toResult() = toResultTransform(successTransform = {
    it
})

/**
 * toResult，把[ApiResponse]转成非空[Result]
 */
fun <T> ApiResponse<T>.toResultNotNull() = toResultTransform(successTransform = {
    it!!
})

/**
 * toResult，在[WanAndroidApiResponseResultHandler]规则下，把[WanAndroidBaseModel]的转换为[WanAndroidBaseModel.data]
 */
fun <T> ApiResponse<WanAndroidBaseModel<T>>.toResultWanAndroid() = toResultTransform(successTransform = {
    it!!.data!! // 此成功，已经判断了data不为空。
})

/**
 * toResult，在[ApiOpenApiResponseResultHandler]规则下，把[ApiOpenBaseModel]的转换为[ApiOpenBaseModel.result]
 */
fun <T> ApiResponse<ApiOpenBaseModel<T>>.toResultApiOpen() =
    toResultTransform(successTransform = {
        it!!.result!! // 此成功，已经判断了result不为空。
    })

private fun <T, R> ApiResponse<T>.toResultTransform(successTransform: (T?) -> R) =
    when (this) {
        is ApiResponse.Success -> {
            // 网络成功，并且规则成功
            Result.success(successTransform(data))
        }
        is ApiResponse.Failure.Error -> {
            // 网络成功，并且规则失败
            Result.failure(true, code, message)
        }
        is ApiResponse.Failure.Exception -> {
            // 网络失败，异常
            val (code, message) = this.throwable.toCodeMessage()
            Result.failure(false, code, message)
        }
    }