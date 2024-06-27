package com.intermediate.storyapp.view.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.intermediate.storyapp.R
import com.intermediate.storyapp.data.pref.UserPreferences
import com.intermediate.storyapp.databinding.ActivityDashboardBinding
import com.intermediate.storyapp.view.story.MapsActivity
import com.intermediate.storyapp.view.story.StoryAddActivity
import com.intermediate.storyapp.view.welcome.WelcomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.dashboardToolbar)

        setupRecyclerView()
        observeViewModel()
        setupMainButton()
    }

    private fun setupRecyclerView() {
        itemAdapter = ItemAdapter(this)
        binding.rvStory.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this@DashboardActivity)
        }
    }

    private fun observeViewModel() {

        dashboardViewModel.storyResponse.observe(this) { pagingData ->
            pagingData?.let { pagingData ->
                itemAdapter.submitData(lifecycle, pagingData)
            }

        }
        val token = UserPreferences.userToken
        dashboardViewModel.getStory(token)


    }

    private fun setupMainButton() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, StoryAddActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                UserPreferences.clearUserPreferences()
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
