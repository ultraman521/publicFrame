package com.publicframe.framework.state

/**
 * 页面级 UI 状态。配合 [com.publicframe.framework.widget.MultiStateView] 或
 * 自定义视图在 Activity/Fragment 中渲染。
 */
sealed interface UiState<out T> {

    /** 加载中。 */
    data object Loading : UiState<Nothing>

    /** 内容为空。 */
    data object Empty : UiState<Nothing>

    /** 数据加载成功。 */
    data class Content<out T>(val data: T) : UiState<T>

    /** 加载失败；[message] 已是面向用户的文案。 */
    data class Error(val message: String, val cause: Throwable? = null) : UiState<Nothing>
}
