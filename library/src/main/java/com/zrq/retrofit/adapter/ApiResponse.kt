package com.zrq.retrofit.adapter

/**
 * 描述：API响应类。
 *
 * @author zhangrq
 * createTime 2021/5/17 15:47
 */
sealed class ApiResponse<T> {
    data class Success<T>(val data: T?) : ApiResponse<T>()

    sealed class Failure<T> : ApiResponse<T>() {
        data class Error<T>(val code: Int, val message: String) : Failure<T>()

        data class Exception<T>(val throwable: Throwable) : Failure<T>()
    }

    companion object {

        fun <T> success(result: T?) = Success(result)

        fun <T> error(code: Int, message: String) = Failure.Error<T>(code, message)

        fun <T> exception(throwable: Throwable) = Failure.Exception<T>(throwable)
    }
}