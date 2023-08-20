package com.dokiwei.wanandroid.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dokiwei.wanandroid.data.local.dao.HomeDao
import com.dokiwei.wanandroid.data.local.dao.HomeRemoteKeysDao
import com.dokiwei.wanandroid.data.local.dao.ProjectDao
import com.dokiwei.wanandroid.data.local.dao.ProjectRemoteKeysDao
import com.dokiwei.wanandroid.data.local.dao.ProjectTabDao
import com.dokiwei.wanandroid.data.local.dao.ProjectTabRemoteKeysDao
import com.dokiwei.wanandroid.data.local.dao.QaDao
import com.dokiwei.wanandroid.data.local.dao.QaRemoteKeysDao
import com.dokiwei.wanandroid.data.local.dao.SquareDao
import com.dokiwei.wanandroid.data.local.dao.SquareRemoteKeysDao
import com.dokiwei.wanandroid.model.entity.home.HomeEntity
import com.dokiwei.wanandroid.model.entity.home.QaEntity
import com.dokiwei.wanandroid.model.entity.home.SquareEntity
import com.dokiwei.wanandroid.model.entity.project.ProjectEntity
import com.dokiwei.wanandroid.model.entity.project.ProjectTabEntity
import com.dokiwei.wanandroid.model.entity.remotekey.HomeRemoteKeys
import com.dokiwei.wanandroid.model.entity.remotekey.ProjectRemoteKeys
import com.dokiwei.wanandroid.model.entity.remotekey.ProjectTabRemoteKeys
import com.dokiwei.wanandroid.model.entity.remotekey.QaRemoteKeys
import com.dokiwei.wanandroid.model.entity.remotekey.SquareRemoteKeys

/**
 * @author DokiWei
 * @date 2023/8/9 15:20
 */
@Database(
    entities = [
        HomeEntity::class,
        HomeRemoteKeys::class,
        QaEntity::class,
        QaRemoteKeys::class,
        SquareEntity::class,
        SquareRemoteKeys::class,
        ProjectEntity::class,
        ProjectRemoteKeys::class,
        ProjectTabEntity::class,
        ProjectTabRemoteKeys::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun homeDao(): HomeDao
    abstract fun qaDao(): QaDao
    abstract fun squareDao(): SquareDao
    abstract fun homeRemoteKeysDao(): HomeRemoteKeysDao
    abstract fun qaRemoteKeysDao(): QaRemoteKeysDao
    abstract fun squareRemoteKeysDao(): SquareRemoteKeysDao

    abstract fun projectDao(): ProjectDao
    abstract fun projectRemoteKeysDao(): ProjectRemoteKeysDao
    abstract fun projectTabDao(): ProjectTabDao
    abstract fun projectTabRemoteKeysDao(): ProjectTabRemoteKeysDao

}