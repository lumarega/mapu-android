package com.viajacalu.mapu.core.data.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.viajacalu.mapu.core.data.auth.AuthDataSource
import com.viajacalu.mapu.core.data.auth.AuthRepository
import com.viajacalu.mapu.core.data.auth.AuthRepositoryImpl
import com.viajacalu.mapu.core.data.auth.CredentialManagerDataSource
import com.viajacalu.mapu.core.data.auth.CredentialsDataSource
import com.viajacalu.mapu.core.data.auth.FirebaseAuthDataSource
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
