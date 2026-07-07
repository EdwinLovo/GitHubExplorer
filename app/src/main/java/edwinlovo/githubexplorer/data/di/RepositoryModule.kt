package edwinlovo.githubexplorer.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // TODO — bind each repository:
    // @Binds @Singleton
    // abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
