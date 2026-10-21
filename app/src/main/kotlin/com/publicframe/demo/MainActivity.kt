package com.publicframe.demo

import androidx.activity.viewModels
import coil.load
import com.publicframe.demo.databinding.ActivityMainBinding
import com.publicframe.demo.net.Banner
import com.publicframe.framework.base.BaseActivity
import com.publicframe.framework.state.UiState

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()

    override fun onBindingCreated(binding: ActivityMainBinding) {
        collectWithLifecycle(viewModel.state) { state ->
            binding.stateView.render(state) { viewModel.loadRemote() }
            if (state is UiState.Content) {
                renderContent(state.data)
            }
        }

        binding.btnMock.setOnClickListener { viewModel.loadMock() }
        binding.btnRemote.setOnClickListener { viewModel.loadRemote() }
    }

    private fun renderContent(banners: List<Banner>) {
        val first = banners.firstOrNull()
        binding.resultText.text = if (first == null) {
            getString(R.string.main_result_empty)
        } else {
            getString(R.string.main_result_format, banners.size, first.title ?: "-")
        }

        val imageUrl = first?.imagePath
        if (imageUrl.isNullOrBlank()) {
            binding.bannerImage.setImageResource(android.R.drawable.ic_menu_report_image)
        } else {
            binding.bannerImage.load(imageUrl) {
                crossfade(true)
            }
        }
    }
}
