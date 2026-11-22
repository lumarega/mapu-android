package com.viajacalu.mapu.core.data.di

import com.viajacalu.mapu.core.data.guides.GuidesDataSource
import com.viajacalu.mapu.core.data.guides.GuidesRepository
import com.viajacalu.mapu.core.data.guides.GuidesRepositoryImpl
import com.viajacalu.mapu.core.data.guides.LocalGuidesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GuidesModule {

    @Binds
    abstract fun bindGuidesDataSource(impl: LocalGuidesDataSource): GuidesDataSource

    @Binds
    abstract fun bindGuidesRepository(impl: GuidesRepositoryImpl): GuidesRepository

}