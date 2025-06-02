package davi.android.alarmapp.di

import org.koin.core.context.GlobalContext.loadKoinModules

fun injectMobileFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            databaseModule, repositoryModule, viewModelModule, serviceModule, workerModule
        )
    )
}