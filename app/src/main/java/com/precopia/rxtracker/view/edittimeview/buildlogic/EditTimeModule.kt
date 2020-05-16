package com.precopia.rxtracker.view.edittimeview.buildlogic

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.edittimeview.EditTimeLogic
import com.precopia.rxtracker.view.edittimeview.IEditTimeContract
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.*

@Module
class EditTimeModule {
    @ViewScope
    @Provides
    fun logic(view: Fragment,
              factory: ViewModelProvider.NewInstanceFactory
    ): IEditTimeContract.Logic {
        return ViewModelProvider(view, factory).get(EditTimeLogic::class.java)
    }

    @ViewScope
    @Provides
    fun factory(repo: ITimeStampRepoContract,
                utilSchedulerProvider: IUtilSchedulerProviderContract,
                disposable: CompositeDisposable
    ): ViewModelProvider.NewInstanceFactory {
        return EditTimeLogicFactory(repo, utilSchedulerProvider, disposable)
    }

    @ViewScope
    @Provides
    fun calendar(): Calendar {
        return Calendar.getInstance()
    }
}