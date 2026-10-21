package com.publicframe.framework.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.publicframe.framework.utils.ToastUtils
import kotlinx.coroutines.launch

/**
 * ViewBinding 泛型基类。子类通过构造参数传入 binding 工厂，无需反射：
 *
 * ```kotlin
 * class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
 *     override fun onBindingCreated(binding: ActivityMainBinding) { ... }
 * }
 * ```
 */
abstract class BaseActivity<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater) -> VB
) : AppCompatActivity() {

    protected lateinit var binding: VB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater(layoutInflater)
        setContentView(binding.root)
        onBindingCreated(binding)
    }

    /** binding 就绪后调用（相当于 onCreate 的业务部分）。 */
    protected abstract fun onBindingCreated(binding: VB)

    protected fun showToast(message: String) = ToastUtils.show(message)

    /**
     * 在 STARTED 及以上状态收集 Flow 的便捷方法。
     *
     * ```kotlin
     * collectWithLifecycle(viewModel.state) { render(it) }
     * ```
     */
    protected fun <T> collectWithLifecycle(flow: kotlinx.coroutines.flow.Flow<T>, block: (T) -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect { block(it) }
            }
        }
    }
}
