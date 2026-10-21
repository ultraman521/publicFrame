package com.publicframe.framework.net

import com.google.gson.annotations.SerializedName

/**
 * 通用响应信封 `{code, message, data}`。
 *
 * 通过 Gson `alternate` 兼容常见字段名变体：
 * `code/errorCode/errCode`、`message/msg/errorMsg`。
 * 业务成功码默认为 0，服务端不同可在反序列化后自行判断。
 */
data class ApiResponse<T>(
    @SerializedName(value = "code", alternate = ["errorCode", "errCode"])
    val code: Int = SUCCESS_CODE,
    @SerializedName(value = "message", alternate = ["msg", "errorMsg"])
    val message: String? = null,
    val data: T? = null
) {
    val isOk: Boolean get() = code == SUCCESS_CODE

    companion object {
        const val SUCCESS_CODE = 0
    }
}

/** 业务失败（HTTP 成功但 code != 0）。 */
class ApiException(
    val code: Int,
    override val message: String
) : RuntimeException("[$code] $message")

/**
 * 解包 [ApiResponse]：业务失败抛 [ApiException]，`data == null` 也视为失败。
 *
 * ```kotlin
 * val banners = ApiClient.unwrap { api.banners() }
 * ```
 */
suspend fun <T : Any> ApiClient.unwrap(block: suspend () -> ApiResponse<T>): T {
    val response = block()
    if (!response.isOk) {
        throw ApiException(response.code, response.message ?: "business error (code=${response.code})")
    }
    return response.data ?: throw ApiException(response.code, "response data is null")
}
