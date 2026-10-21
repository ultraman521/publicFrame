package com.publicframe.demo.net

import com.google.gson.annotations.SerializedName
import com.publicframe.framework.net.ApiResponse
import retrofit2.http.GET

/**
 * 示例 API。真实项目中按服务拆分接口文件，
 * 统一通过 [com.publicframe.framework.net.ApiClient.service] 创建实现。
 */
interface DemoApi {

    @GET("banner/json")
    suspend fun banners(): ApiResponse<List<Banner>>
}

data class Banner(
    val id: Long? = null,
    val title: String? = null,
    val url: String? = null,
    @SerializedName("imagePath") val imagePath: String? = null
)
