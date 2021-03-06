package com.precopia.rxtracker.view.manageprescriptionsview.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.manageprescriptionsview.ManagePrescriptionsLogic
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ManagePrescriptionsLogicFactory(
        private val repo: IPrescriptionRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return ManagePrescriptionsLogic(repo, utilSchedulerProvider, disposable) as T
    }
}