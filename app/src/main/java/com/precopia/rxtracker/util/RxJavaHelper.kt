package com.precopia.rxtracker.util

import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.datamodel.TimeStamp
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

internal fun <E : List<TimeStamp>> subscribeFlowableTimeStamp(flowable: Flowable<E>,
                                                            onNext: (E) -> Unit,
                                                            onError: (t: Throwable) -> Unit,
                                                            schedulerProvider: ISchedulerProviderContract) =
    flowable
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .onTerminateDetach()
        .subscribe({ onNext.invoke(it) }, { onError.invoke(it) })

internal fun <E : List<Prescription>> subscribeFlowablePrescription(flowable: Flowable<E>,
                                                            onNext: (E) -> Unit,
                                                            onError: (t: Throwable) -> Unit,
                                                            schedulerProvider: ISchedulerProviderContract) =
    flowable
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .onTerminateDetach()
        .subscribe({ onNext.invoke(it) }, { onError.invoke(it) })


internal fun subscribeCompletable(completable: Completable,
                                  onComplete: () -> Unit,
                                  onError: (t: Throwable) -> Unit,
                                  schedulerProvider: ISchedulerProviderContract) =
    completable
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .onTerminateDetach()
        .subscribe({ onComplete.invoke() }, { onError.invoke(it) })