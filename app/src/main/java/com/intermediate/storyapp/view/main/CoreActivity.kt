package com.intermediate.storyapp.view.main

import android.app.Application
import com.intermediate.storyapp.data.pref.UserPreferences
import com.intermediate.storyapp.di.coreModule
import com.intermediate.storyapp.di.repositoryDependency
import com.intermediate.storyapp.di.viewModelDependency
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin

class CoreActivity: Application() {
    override fun onCreate() {

        super.onCreate()
        UserPreferences.initialize(this)
        GlobalContext.startKoin {
            androidContext(this@CoreActivity)
            modules(
                listOf(
                    viewModelDependency,
                    repositoryDependency,
                    coreModule
                )
            )
        }
    }
    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}