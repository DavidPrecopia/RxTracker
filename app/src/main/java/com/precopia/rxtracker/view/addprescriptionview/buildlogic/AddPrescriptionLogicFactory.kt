package com.precopia.rxtracker.view.addprescriptionview.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.addprescriptionview.AddPrescriptionLogic
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Suppress("UNCHECKED_CAST")
class AddPrescriptionLogicFactory(
        private val repo: IPrescriptionRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable
): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return AddPrescriptionLogic(repo, utilSchedulerProvider, disposable) as T
    }
}