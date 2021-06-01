package com.zrq.retrofit.adapter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * 描述：网络结果的扩展
 *
 * @author zhangrq
 * createTime 2021/4/29 14:49
 */

fun ApiResponse<*>.isSuccess() = this is ApiResponse.Success

fun ApiResponse<*>.isFailure() = this is ApiResponse.Failure

/**
 * 成功通知
 */
inline fun <T> ApiResponse<T>.onSuccess(onResult: ApiResponse.Success<T>.() -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Success) {
        onResult(this)
    }
    return this
}

/**
 * 失败通知
 */
inline fun <T> ApiResponse<T>.onFailure(crossinline onResult: ApiResponse.Failure<T>.() -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Failure) {
        onResult(this)
    }
    return this
}


/**
 * 失败-错误，通知
 */
inline fun <T> ApiResponse<T>.onError(crossinline onResult: ApiResponse.Failure.Error<T>.() -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Failure.Error) {
        onResult(this)
    }
    return this
}

/**
 * 失败-异常，通知
 */
inline fun <T> ApiResponse<T>.onException(crossinline onResult: ApiResponse.Failure.Exception<T>.() -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Failure.Exception) {
        onResult(this)
    }
    return this
}


/**
 * 线性执行，当前成功后，执行[next]
 */
suspend fun <T, R> ApiResponse<T>.linear(next: suspend T?.() -> ApiResponse<R>): ApiResponse<R> {
    return when (this) {
        is ApiResponse.Success -> {
            // 当前成功，执行下一个
            next(data)
        }
        is ApiResponse.Failure.Error -> {
            // 失败-Error
            ApiResponse.error(code, message)
        }
        is ApiResponse.Failure.Exception -> {
            // 失败-Exception
            ApiResponse.exception(throwable)
        }
    }
}

/**
 * 并发执行，可执行多个，[onSuccess] 返回成功的值
 */
suspend fun <R> asyncAll(
    vararg asyncArray: suspend CoroutineScope.() -> ApiResponse<*>,
    onSuccess: (Map<Int, Any?>) -> ApiResponse<R>
): ApiResponse<R> = coroutineScope {
    val channel = Channel<Pair<Int, ApiResponse<*>>>()
    val runningJobs = mutableMapOf<Int, Job>()
    val successValues = mutableMapOf<Int, Any?>()
    var returnResult: ApiResponse<R>? = null

    // 发送
    asyncArray.forEachIndexed { index, async ->
        val job = launch {
            val element = async()
            channel.send(Pair(index, element))// 发送Pair以记录哪个
        }
        runningJobs[index] = job
    }

    // 接收
    for ((index, value) in channel) {
        when (value) {
            is ApiResponse.Success -> {
                // 网络成功，并且规则成功，保留此值，判断是否完成
                // -移除此项
                runningJobs.remove(index)
                // -保存此值
                successValues[index] = value.data
                // -判断是否完成
                if (runningJobs.isEmpty()) {
                    // 全部移除完成，标记成功
                    returnResult = onSuccess(successValues)
                    // -关闭
                    channel.close()
                }
            }
            is ApiResponse.Failure -> {
                // 返回此错误信息，取消其它运行中Jobs，关闭channel
                // -通知失败
                returnResult = if (value is ApiResponse.Failure.Error)
                    ApiResponse.error(value.code, value.message)
                else
                    ApiResponse.exception((value as ApiResponse.Failure.Exception).throwable)
                // -取消其它
                runningJobs.forEach { it.value.cancel() }
                // -关闭
                channel.close()
            }
        }
    }
    returnResult!!
}