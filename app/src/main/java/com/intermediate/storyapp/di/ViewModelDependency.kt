package com.intermediate.storyapp.di

import com.intermediate.storyapp.view.dashboard.DashboardViewModel
import com.intermediate.storyapp.view.login.LoginViewModel
import com.intermediate.storyapp.view.register.RegisterViewModel
import com.intermediate.storyapp.view.story.StoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelDependency = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { StoryViewModel(get()) }
    viewModel { DashboardViewModel(get()) }


}