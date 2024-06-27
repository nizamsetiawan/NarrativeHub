package com.intermediate.storyapp.di

import com.intermediate.storyapp.data.UserRepository
import org.koin.dsl.module

val repositoryDependency = module {
    single { UserRepository(get()) }
}
