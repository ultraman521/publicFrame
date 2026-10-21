package com.publicframe.framework.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.publicframe.framework.net.HttpError
import com.publicframe.framework.state.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel 基类：提供把挂起请求映射为 [UiState] 的 [launchWithState]，
 * 以及跨页面通用的 [toastEvent] 单发事件。
 */
abstract class BaseViewModel : ViewModel() {

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    /** 从任意协程发送一条 Toast 提示，由 BaseActivity 统一消费。 */
    protected suspend fun toast(message: String) {
        _toastEvent.emit(message)
    }

    /**
     * 典型用法：
     *
     * ```kotlin
     * private val _state = MutableStateFlow<UiState<List<Banner>>>(UiState.Loading)
     * val state = _state.asStateFlow()
     *
     * fun load() = launchWithState(_state) { ApiClient.unwrap { api.banners() } }
     * ```
     *
     * 请求前置为 [UiState.Loading]；返回 null 视为 [UiState.Empty]；
     * 异常翻译为用户可读文案并进入 [UiState.Error]。协程取消会原样抛出。
     */
    protected fun <T> launchWithState(
        state: MutableStateFlow<UiState<T>>,
        block: suspend () -> T?
    ): Job = viewModelScope.launch {
        state.value = UiState.Loading
        try {
            val result = block()
            state.value = if (result == null) UiState.Empty else UiState.Content(result)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            state.value = UiState.Error(HttpError.messageOf(t), t)
        }
    }
}
