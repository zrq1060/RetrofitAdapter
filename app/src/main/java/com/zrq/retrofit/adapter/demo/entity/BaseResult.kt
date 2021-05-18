package com.zrq.retrofit.adapter.demo.entity

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 18:39
 */
data class BaseResult<T>(val code: Int?, val message: String?, val result: T?)