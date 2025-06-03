package davi.android.alarmapp.di

import androidx.room.Room
import davi.android.alarmapp.core.DatabaseConstants
import davi.android.alarmapp.data.database.AlarmDatabase
import davi.android.alarmapp.data.database.MigrationAlarmDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AlarmDatabase::class.java,
            DatabaseConstants.ALARM_DATABASE
        )
            .addMigrations(MigrationAlarmDb.MIGRATION_7_8)
            .build()
    }
    single {
        get<AlarmDatabase>().getAlarmDatabaseDao()
    }
}