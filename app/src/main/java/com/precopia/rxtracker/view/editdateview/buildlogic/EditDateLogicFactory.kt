package com.precopia.rxtracker.view.editdateview.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilParseDateTime
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.editdateview.EditDateLogic
import io.reactivex.rxjava3.disposables.CompositeDisposable

class EditDateLogicFactory(
        private val repo: ITimeStampRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable,
        private val utilParseDateTime: IUtilParseDateTime
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return EditDateLogic(repo, utilSchedulerProvider, disposable, utilParseDateTime) as T
    }
}