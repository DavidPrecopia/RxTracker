package com.precopia.rxtracker.common.buildlogic

import android.app.Application
import com.precopia.data.buildlogic.PrescriptionRepoServiceLocator
import com.precopia.data.buildlogic.TimeStampRepoServiceLocator
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.domain.repository.ITimeStampRepoContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun timeStampRepo(application: Application): ITimeStampRepoContract {
        return TimeStampRepoServiceLocator(application).timeStampRepo()
    }

    @Singleton
    @Provides
    fun prescriptionRepo(application: Application): IPrescriptionRepoContract {
        return PrescriptionRepoServiceLocator(application).prescriptionRepo()
    }
}
