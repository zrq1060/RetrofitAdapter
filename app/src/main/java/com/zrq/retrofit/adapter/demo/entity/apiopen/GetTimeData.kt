package com.zrq.retrofit.adapter.demo.entity.apiopen

/**
 * 描述:
 *
 * @author zhangrq
 * createTime 2022/8/9 22:48
 */
// {
//  "code": 200,
//  "message": "成功!",
//  "result": {
//    "date": "2022-08-10",
//    "time": "09:10:17",
//    "weekday": "星期三",
//    "dateTime": "2022-08-10 09:10:17"
//  }
//}
data class GetTimeData(
    val date: String?,
    val time: String?,
    val weekday: String?,
    val dateTime: String?
)