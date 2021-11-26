package com.zrq.retrofit.adapter.demo

import com.zrq.retrofit.adapter.ApiResponse
import com.zrq.retrofit.adapter.demo.entity.wanandroid.WanAndroidBaseData
import com.zrq.retrofit.adapter.demo.entity.apiopen.ApiOpenBaseResult
import com.zrq.retrofit.adapter.demo.handler.WanAndroidBaseDataApiResponseResultHandler
import com.zrq.retrofit.adapter.demo.handler.ApiOpenBaseResultApiResponseResultHandler

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
 * toResult，在[WanAndroidBaseDataApiResponseResultHandler]规则下，把[WanAndroidBaseData]的转换为[WanAndroidBaseData.data]
 */
fun <T> ApiResponse<WanAndroidBaseData<T>>.toResultBaseDataOfData() = toResultTransform(successTransform = {
    it!!.data!! // 此成功，已经判断了data不为空
})

/**
 * toResult，在[ApiOpenBaseResultApiResponseResultHandler]规则下，把[ApiOpenBaseResult]的转换为[ApiOpenBaseResult.result]
 */
fun <T> ApiResponse<ApiOpenBaseResult<T>>.toResultBaseResultOfResult() =
    toResultTransform(successTransform = {
        it!!.result!! // 此成功，已经判断了result不为空
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