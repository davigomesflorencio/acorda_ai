package davi.android.alarmapp.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import davi.android.alarmapp.core.DatabaseConstants

object MigrationAlarmDb {

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${DatabaseConstants.TABLE_ALARM} ADD COLUMN snoozeActive INTEGER NOT NULL DEFAULT 0")
        }
    }

    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${DatabaseConstants.TABLE_ALARM} ADD COLUMN name TEXT NOT NULL DEFAULT ''")
        }
    }

    val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${DatabaseConstants.TABLE_ALARM} ADD COLUMN ringtone TEXT NOT NULL DEFAULT ''")
        }
    }
}