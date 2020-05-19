package com.precopia.rxtracker.view.manageprescriptionsview.buildlogic

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.manageprescriptionsview.IManagePrescriptionsContact
import com.precopia.rxtracker.view.manageprescriptionsview.ManagePrescriptionsAdapter
import com.precopia.rxtracker.view.manageprescriptionsview.ManagePrescriptionsLogic
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
class AddPrescriptionModule {
    @ViewScope
    @Provides
    fun logic(
            view: Fragment,
            factory: ViewModelProvider.NewInstanceFactory
    ): IManagePrescriptionsContact.Logic {
        return ViewModelProvider(view, factory).get(ManagePrescriptionsLogic::class.java)
    }

    @ViewScope
    @Provides
    fun factory(
            repo: IPrescriptionRepoContract,
            utilSchedulerProvider: IUtilSchedulerProviderContract,
            disposable: CompositeDisposable
    ): ViewModelProvider.NewInstanceFactory {
        return AddPrescriptionLogicFactory(repo, utilSchedulerProvider, disposable)
    }

    @ViewScope
    @Provides
    fun adapter(logic: IManagePrescriptionsContact.Logic,
                itemTouchHelper: ItemTouchHelper
    ): IManagePrescriptionsContact.Adapter {
        return ManagePrescriptionsAdapter(logic, itemTouchHelper)
    }
}