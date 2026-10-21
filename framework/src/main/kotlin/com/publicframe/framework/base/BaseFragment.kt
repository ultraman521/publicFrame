package com.publicframe.framework.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.publicframe.framework.utils.ToastUtils

/**
 * ViewBinding 泛型 Fragment 基类，binding 生命周期与 view 一致，
 * [onDestroyView] 后访问 [binding] 会抛出明确异常。
 *
 * ```kotlin
 * class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
 *     override fun onBindingCreated(binding: FragmentHomeBinding) { ... }
 * }
 * ```
 */
abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding ?: error("Fragment binding is only valid between onCreate and onDestroyView")

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindingInflater(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindingCreated(binding)
    }

    /** binding 就绪后调用（相当于 onViewCreated 的业务部分）。 */
    protected abstract fun onBindingCreated(binding: VB)

    protected fun showToast(message: String) = ToastUtils.show(message)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
