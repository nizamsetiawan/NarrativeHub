package com.intermediate.storyapp.view.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.intermediate.storyapp.R
import com.intermediate.storyapp.data.pref.UserPreferences
import com.intermediate.storyapp.data.response.DetailResponse
import com.intermediate.storyapp.databinding.ActivityStoryDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding
    private val storyViewModel: StoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemId = intent.getStringExtra("item_id") ?: ""
        val token = UserPreferences.userToken
        observeDetailViewModel()
        storyViewModel.detailStory(token, itemId)
        setupToolbar()
    }

    private fun observeDetailViewModel() {
        storyViewModel.detailResponse.observe(this) { detailResponse ->
            detailResponse?.let { response ->
                if (response.error == false) {
                    displayStoryDetails(response)
                } else {
                    // Handle error
                }
            }
        }
    }
    private fun setupToolbar() {
        setSupportActionBar(binding.detailStoryToolbar);
        binding.detailStoryToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_24);
            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun displayStoryDetails(detailResponse: DetailResponse) {
        binding.apply {
            username.text = detailResponse.story?.name
            description.text = detailResponse.story?.description
            Glide.with(this@StoryDetailActivity)
                .load(detailResponse.story?.photoUrl)
                .into(imageStory)
        }
    }
}
