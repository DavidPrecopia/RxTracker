package com.precopia.rxtracker.common.buildlogic

import android.app.Application
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.domain.repository.ITimeStampRepoContract
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun timeStampRepo(): ITimeStampRepoContract

    fun prescriptionRepo(): IPrescriptionRepoContract

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder
    }
}