package com.precopia.rxtracker.view.addtimestamp.bulidlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.addtimestamp.AddTimeStampLogic
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AddTimeStampLogicFactory(
        private val prescriptionRepo: IPrescriptionRepoContract,
        private val timeStampRepo: ITimeStampRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return AddTimeStampLogic(
                prescriptionRepo, timeStampRepo, utilSchedulerProvider, disposable
        ) as T
    }
}