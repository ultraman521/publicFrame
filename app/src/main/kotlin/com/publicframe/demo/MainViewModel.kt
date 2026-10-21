package com.publicframe.demo

import com.publicframe.demo.net.Banner
import com.publicframe.demo.net.DemoApi
import com.publicframe.framework.base.BaseViewModel
import com.publicframe.framework.net.ApiClient
import com.publicframe.framework.net.unwrap
import com.publicframe.framework.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : BaseViewModel() {

    private val api: DemoApi by lazy { ApiClient.service() }

    private val _state = MutableStateFlow<UiState<List<Banner>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Banner>>> = _state.asStateFlow()

    init {
        loadMock()
    }

    /** 本地模拟数据：演示 UiState 流转，不依赖网络。 */
    fun loadMock() = launchWithState(_state) {
        delay(600)
        listOf(
            Banner(id = 1, title = "PublicFrame 脚手架已就绪", url = null, imagePath = null),
            Banner(id = 2, title = "点击下方按钮发起真实网络请求", url = null, imagePath = null)
        )
    }

    /** 真实请求模板：ApiClient + unwrap + 异常自动进入 UiState.Error。 */
    fun loadRemote() = launchWithState(_state) {
        ApiClient.unwrap { api.banners() }
    }
}
