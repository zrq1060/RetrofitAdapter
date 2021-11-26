package com.zrq.retrofit.adapter.demo.entity.wanandroid

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2021/5/17 16:45
 */
data class FeedArticleData(
    val curPage: Int?,
    val datas: List<FeedArticleItemData>?,
    val offset: Int?,
    val over: Boolean?,
    val pageCount: Int?,
    val size: Int?,
    val total: Int?
)

data class FeedArticleItemData(
    val apkLink: String?,
    val audit: Int?,
    val author: String?,
    val canEdit: Boolean?,
    val chapterId: Int?,
    val chapterName: String?,
    val collect: Boolean?,
    val courseId: Int?,
    val desc: String?,
    val descMd: String?,
    val envelopePic: String?,
    val fresh: Boolean?,
    val host: String?,
    val id: Int?,
    val link: String?,
    val niceDate: String?,
    val niceShareDate: String?,
    val origin: String?,
    val prefix: String?,
    val projectLink: String?,
    val publishTime: Long?,
    val realSuperChapterId: Int?,
    val selfVisible: Int?,
    val shareDate: Long?,
    val shareUser: String?,
    val superChapterId: Int?,
    val superChapterName: String?,
    val title: String?,
    val type: Int?,
    val userId: Int?,
    val visible: Int?,
    val zan: Int?
)