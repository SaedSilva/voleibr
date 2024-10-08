package br.dev.saed.voleibr.model.di

import androidx.room.Room
import br.dev.saed.voleibr.model.repositories.db.TeamDatabase
import br.dev.saed.voleibr.model.repositories.db.team.TeamRepository
import br.dev.saed.voleibr.ui.viewmodel.MainViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

//TODO fix koin

val appModule = module {
    viewModelOf(::MainViewModel)
}

val storageModule = module {
    singleOf(::TeamRepository)
    single{
        Room.databaseBuilder(
            androidContext(),
            TeamDatabase::class.java,
            "team_database"
        ).build()
    }
    single {
        get<TeamDatabase>().teamDAO()
    }
}