package com.zrq.retrofit.adapter.demo.entity

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 18:39
 */
data class BaseData<T>(val errorCode: Int?, val errorMsg: String?, val data: T?)