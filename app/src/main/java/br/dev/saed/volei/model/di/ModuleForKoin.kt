package br.dev.saed.volei.model.di

import androidx.room.Room
import br.dev.saed.volei.model.repositories.datastore.DataStoreHelper
import br.dev.saed.volei.model.repositories.db.TeamDatabase
import br.dev.saed.volei.model.repositories.db.team.TeamRepository
import br.dev.saed.volei.model.repositories.db.winner.WinnerRepository
import br.dev.saed.volei.ui.viewmodel.MainViewModel
import br.dev.saed.volei.ui.viewmodel.StatsViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::StatsViewModel)
}

val storageModule = module {
    singleOf(::TeamRepository)
    singleOf(::WinnerRepository)
    single {
        Room.databaseBuilder(
            androidContext(),
            TeamDatabase::class.java,
            "team_database"
        ).fallbackToDestructiveMigration(false).build()
    }
    single {
        get<TeamDatabase>().teamDAO()
    }
    single {
        get<TeamDatabase>().winnerDAO()
    }
    single {
        DataStoreHelper(androidContext())
    }
}