package com.dokiwei.wanandroid.di

import android.content.Context
import androidx.room.Room
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import com.dokiwei.wanandroid.model.util.Constants.DATABASE_ARTICLE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author DokiWei
 * @date 2023/8/8 17:12
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(context,ArticleDatabase::class.java,DATABASE_ARTICLE).build()
    }
}
