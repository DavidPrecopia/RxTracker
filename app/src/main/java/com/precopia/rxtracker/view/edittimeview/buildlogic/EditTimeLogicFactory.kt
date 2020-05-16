package com.precopia.rxtracker.view.edittimeview.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.edittimeview.EditTimeLogic
import io.reactivex.rxjava3.disposables.CompositeDisposable

class EditTimeLogicFactory(
        private val repo: ITimeStampRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return EditTimeLogic(repo, utilSchedulerProvider, disposable) as T
    }
}