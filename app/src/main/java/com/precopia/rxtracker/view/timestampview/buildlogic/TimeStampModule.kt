package com.precopia.rxtracker.view.timestampview.buildlogic

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.timestampview.ITimeStampViewContract
import com.precopia.rxtracker.view.timestampview.TimeStampAdapter
import com.precopia.rxtracker.view.timestampview.TimeStampLogic
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
class TimeStampModule {
    @ViewScope
    @Provides
    fun logic(
            view: Fragment,
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
