package com.zrq.retrofit.adapter.demo

/**
 * 描述：Result结果类。
 *
 * @author zhangrq
 * createTime 2021/5/28 17:36
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val isError: Boolean, val code: Int, val message: String) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failure -> "Error[isError=$isError,code=$code,message=$message]"
        }
    }

    companion object {

        fun <T> success(data: T) = Success(data)

        fun failure(isError: Boolean, code: Int, message: String) = Failure(isError, code, message)
    }
}