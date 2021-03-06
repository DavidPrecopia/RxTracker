package com.precopia.rxtracker.util

import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.datamodel.TimeStamp
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

internal fun <E: List<TimeStamp>> subscribeFlowableTimeStamp(flowable: Flowable<E>,
                                                             onNext: (E) -> Unit,
                                                             onError: (t: Throwable) -> Unit,
                                                             utilSchedulerProvider: IUtilSchedulerProviderContract) =
        flowable
                .subscribeOn(utilSchedulerProvider.io())
                .observeOn(utilSchedulerProvider.ui())
                .onTerminateDetach()
                .subscribe({ onNext.invoke(it) }, { onError.invoke(it) })

internal fun <E: List<Prescription>> subscribeFlowablePrescription(flowable: Flowable<E>,
                                                                   onNext: (E) -> Unit,
                                                                   onError: (t: Throwable) -> Unit,
                                                                   utilSchedulerProvider: IUtilSchedulerProviderContract) =
        flowable
                .subscribeOn(utilSchedulerProvider.io())
                .observeOn(utilSchedulerProvider.ui())
                .onTerminateDetach()
                .subscribe({ onNext.invoke(it) }, { onError.invoke(it) })


internal fun subscribeCompletable(completable: Completable,
                                  onComplete: () -> Unit,
                                  onError: (t: Throwable) -> Unit,
                                  utilSchedulerProvider: IUtilSchedulerProviderContract) =
        completable
                .subscribeOn(utilSchedulerProvider.io())
                .observeOn(utilSchedulerProvider.ui())
                .onTerminateDetach()
                .subscribe({ onComplete.invoke() }, { onError.invoke(it) })