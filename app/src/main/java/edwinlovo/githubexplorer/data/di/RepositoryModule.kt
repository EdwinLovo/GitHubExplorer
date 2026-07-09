package edwinlovo.githubexplorer.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edwinlovo.githubexplorer.data.repository.favorites.FavoritesRepositoryImpl
import edwinlovo.githubexplorer.data.repository.repos.RepoRepositoryImpl
import edwinlovo.githubexplorer.data.repository.search.SearchRepositoryImpl
import edwinlovo.githubexplorer.domain.repository.FavoritesRepository
import edwinlovo.githubexplorer.domain.repository.RepoRepository
import edwinlovo.githubexplorer.domain.repository.SearchRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    abstract fun bindRepoRepository(impl: RepoRepositoryImpl): RepoRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
}
