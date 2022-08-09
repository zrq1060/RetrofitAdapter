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
//    "total": 19080,
//    "list": [
//      {
//        "id": 11289,
//        "title": "古墓丽影Lara Croft 4K壁",
//        "url": "https://pic.netbian.com/uploads/allimg/170623/163948-1498207188b30c.jpg",
//        "type": "game"
//      }
//    ]
//  }
//}
data class GetImagesData(val total: Int?, val list: List<GetImagesItemData>?)
data class GetImagesItemData(val id: Int?, val title: String?, val url: String?, val type: String?)