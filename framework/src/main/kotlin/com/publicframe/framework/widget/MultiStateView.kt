package com.publicframe.framework.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.publicframe.framework.R
import com.publicframe.framework.state.UiState

/**
 * 四态视图（加载中/空/错误/内容）。XML 中作为容器直接包裹内容布局，
 * 代码里通过 [render] 或 showXxx() 切换：
 *
 * ```xml
 * <com.publicframe.framework.widget.MultiStateView
 *     android:id="@+id/state_view"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent">
 *
 *     <!-- 内容布局 -->
 * </com.publicframe.framework.widget.MultiStateView>
 * ```
 *
 * ```kotlin
 * stateView.render(state) { viewModel.load() }   // Error 态的重试回调
 * ```
 */
class MultiStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val loadingView: View
    private val emptyView: TextView
    private val errorContainer: LinearLayout
    private val errorText: TextView
    private val retryButton: Button

    private var onRetry: (() -> Unit)? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView)
        val emptyText = typed.getString(R.styleable.MultiStateView_msvEmptyText)
            ?: context.getString(R.string.frame_state_empty)
        val retryText = typed.getString(R.styleable.MultiStateView_msvRetryText)
            ?: context.getString(R.string.frame_state_retry)
        typed.recycle()

        val pad = dp(24)
        val textPad = dp(16)

        loadingView = ProgressBar(context).apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
        }

        emptyView = TextView(context).apply {
            text = emptyText
            gravity = Gravity.CENTER
            setPadding(textPad, pad, textPad, pad)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
        }

        errorText = TextView(context).apply {
            gravity = Gravity.CENTER
            setPadding(textPad, 0, textPad, 0)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        }
        retryButton = Button(context).apply {
            text = retryText
            setOnClickListener { onRetry?.invoke() }
        }
        errorContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(textPad, pad, textPad, pad)
            addView(errorText, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                bottomMargin = dp(12)
            })
            addView(retryButton, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
        }

        addView(loadingView)
        addView(emptyView)
        addView(errorContainer)
        showContent()
    }

    /** 把 [UiState] 渲染到本视图；[onRetry] 会在 Error 态点击重试时回调。 */
    fun render(state: UiState<*>, onRetry: (() -> Unit)? = null) {
        this.onRetry = onRetry
        when (state) {
            is UiState.Loading -> showLoading()
            is UiState.Content -> showContent()
            is UiState.Empty -> showEmpty()
            is UiState.Error -> showError(state.message)
        }
    }

    fun showLoading() = switchTo(loadingView)

    fun showContent() = switchTo(null)

    fun showEmpty() = switchTo(emptyView)

    fun showError(message: String) {
        errorText.text = message
        switchTo(errorContainer)
    }

    /** stateView 之外的子 View 视为内容；[only] 为 null 时显示内容。 */
    private fun switchTo(only: View?) {
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            child.visibility = if (child === only || (only == null && !isStateChild(child))) VISIBLE else GONE
        }
        only?.visibility = VISIBLE
    }

    private fun isStateChild(view: View): Boolean =
        view === loadingView || view === emptyView || view === errorContainer

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density + 0.5f).toInt()

    private companion object {
        private const val WRAP_CONTENT = LayoutParams.WRAP_CONTENT
    }
}
