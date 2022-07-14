package com.dent.denttask.di

import com.dent.denttask.data.local.FileManagement
import com.dent.denttask.data.repository.CountriesRepositoryImpl
import com.dent.denttask.domain.repository.CountriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun bindCountriesRepository(fileManagement: FileManagement): CountriesRepository =
        CountriesRepositoryImpl(fileManagement)
}