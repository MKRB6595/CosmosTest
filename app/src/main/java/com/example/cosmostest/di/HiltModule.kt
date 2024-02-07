package com.example.cosmostest.di

import com.example.cosmostest.api.CosmoApiService
import com.example.cosmostest.repo.DeviceRepository
import com.example.cosmostest.repo.IDeviceRepository
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
    fun provideDeviceRepository(
        apiService: CosmoApiService
    ): IDeviceRepository = DeviceRepository(apiService)
}


