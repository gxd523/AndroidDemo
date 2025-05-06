package com.gxd.demo.lib.dal.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gxd.demo.lib.dal.source.database.dao.RepoDao
import com.gxd.demo.lib.dal.source.database.dao.UserDao
import com.gxd.demo.lib.dal.source.database.model.RepoEntity
import com.gxd.demo.lib.dal.source.database.model.UserEntity

@Database(entities = [RepoEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class GxdDatabase : RoomDatabase() {
    companion object {
        /**
         * 「int」类型的「age」字段，升级后变为「String」类型的「name」字段
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1、创建临时表：「users_new」
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `users_new` (
                        `id` INTEGER PRIMARY KEY NOT NULL,
                        `name` TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent()
                )

                // 2、迁移旧数据（将「age」转换为「String」）
                database.execSQL(
                    """
                    INSERT INTO users_new (id, name)
                    SELECT id, CAST(age AS TEXT) FROM users
                    """.trimIndent()
                )

                // 3、删除旧表
                database.execSQL("DROP TABLE users")

                // 4、重命名新表
                database.execSQL("ALTER TABLE users_new RENAME TO users")
            }
        }
    }

    abstract fun repoDao(): RepoDao

    abstract fun userDao(): UserDao
}