package com.precopia.rxtracker.view.timestampview.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.timestampview.TimeStampLogic
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TimeStampLogicFactory(
        private val repo: ITimeStampRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return TimeStampLogic(repo, utilSchedulerProvider, disposable) as T
    }
}