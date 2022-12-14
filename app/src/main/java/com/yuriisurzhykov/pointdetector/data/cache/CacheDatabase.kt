package com.yuriisurzhykov.pointdetector.data.cache

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yuriisurzhykov.pointdetector.data.cache.configs.ConfigsDao
import com.yuriisurzhykov.pointdetector.data.cache.converters.WorkingHoursListConverter
import com.yuriisurzhykov.pointdetector.data.cache.entities.ConfigEntity
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.cache.migrations.MigrationV1ToV2Spec

@Database(
    version = 2,
    entities = [PointCache::class, ConfigEntity::class],
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2, spec = MigrationV1ToV2Spec::class)]
)
@TypeConverters(WorkingHoursListConverter::class)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun providePointsDao(): PointsDao
    abstract fun provideConfigsDao(): ConfigsDao

}