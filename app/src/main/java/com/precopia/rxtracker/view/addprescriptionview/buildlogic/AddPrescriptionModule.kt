package com.precopia.rxtracker.view.addprescriptionview.buildlogic

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.addprescriptionview.AddPrescriptionAdapter
import com.precopia.rxtracker.view.addprescriptionview.AddPrescriptionLogic
import com.precopia.rxtracker.view.addprescriptionview.IAddPrescriptionContact
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
    ): IAddPrescriptionContact.Logic {
        return ViewModelProvider(view, factory).get(AddPrescriptionLogic::class.java)
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
    fun adapter(itemTouchHelper: ItemTouchHelper): IAddPrescriptionContact.Adapter {
        return AddPrescriptionAdapter(itemTouchHelper)
    }
}