package com.viajacalu.mapu.core.auth.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.viajacalu.mapu.core.auth.data.AuthDataSource
import com.viajacalu.mapu.core.auth.data.AuthRepositoryImpl
import com.viajacalu.mapu.core.auth.data.CredentialManagerDataSource
import com.viajacalu.mapu.core.auth.data.CredentialsDataSource
import com.viajacalu.mapu.core.auth.data.FirebaseAuthDataSource
import com.viajacalu.mapu.core.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(impl: FirebaseAuthDataSource): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindCredentialsDataSource(impl: CredentialManagerDataSource): CredentialsDataSource

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

        @Provides
        @Singleton
        fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
            CredentialManager.create(context)
    }
}
