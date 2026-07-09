package edwinlovo.githubexplorer.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edwinlovo.githubexplorer.data.local.AppDatabase
import edwinlovo.githubexplorer.data.local.FavoriteRepoDao
import javax.inject.Singleton

private const val DATABASE_NAME = "github_explorer.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideFavoriteRepoDao(database: AppDatabase): FavoriteRepoDao = database.favoriteRepoDao()
}
