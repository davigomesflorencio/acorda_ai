package davi.android.alarmapp.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import davi.android.alarmapp.core.DatabaseConstants

object MigrationAlarmDb {
    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("ALTER TABLE ${DatabaseConstants.TABLE_ALARM} ADD COLUMN soundActive BOOLEAN NOT NULL DEFAULT 'TRUE'")
//            database.execSQL("ALTER TABLE ${DatabaseConstants.TABLE_ALARM} DROP COLUMN soundActive")
        }
    }

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${DatabaseConstants.TABLE_ALARM} ADD COLUMN soundActive BOOLEAN NOT NULL DEFAULT 'TRUE'")
        }
    }
}