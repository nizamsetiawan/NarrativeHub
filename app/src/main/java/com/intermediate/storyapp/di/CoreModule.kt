package com.intermediate.storyapp.di

import com.intermediate.storyapp.data.RemoteRepository
import com.intermediate.storyapp.data.retrofit.ApiConfig
import com.intermediate.storyapp.data.pref.UserPreferences
import org.koin.dsl.module

val coreModule = module {
    single { ApiConfig.buildApiService }
    single { UserPreferences.initialize(get()) }
    single { RemoteRepository(get()) }

}
