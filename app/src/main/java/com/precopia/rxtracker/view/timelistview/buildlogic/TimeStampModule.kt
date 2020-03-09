package com.precopia.rxtracker.view.timelistview.buildlogic

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.timelistview.ITimeStampViewContract
import com.precopia.rxtracker.view.timelistview.TimeStampAdapter
import com.precopia.rxtracker.view.timelistview.TimeStampLogic
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
class TimeStampModule {
    @ViewScope
    @Provides
    fun logic(
        view: ViewModelStore,
        factory: ViewModelProvider.NewInstanceFactory
    ): ITimeStampViewContract.Logic {
        return ViewModelProvider(view, factory).get(TimeStampLogic::class.java)
    }

    @ViewScope
    @Provides
    fun factory(
        repo: ITimeStampRepoContract,
        utilSchedulerProvider: IUtilSchedulerProviderContract,
        disposable: CompositeDisposable
    ): ViewModelProvider.NewInstanceFactory {
        return TimeStampLogicFactory(repo, utilSchedulerProvider, disposable)
    }

    @ViewScope
    @Provides
    fun adapter(logic: ITimeStampViewContract.Logic): ITimeStampViewContract.Adapter {
        return TimeStampAdapter(logic)
    }
}
