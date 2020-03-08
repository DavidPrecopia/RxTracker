package com.precopia.rxtracker

import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import io.mockk.every
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Keeping code DRY.
 *
 * Trampoline: a Scheduler that queues work on the current thread to be
 * executed after the current work completes.
 * Another way to put it: emits result in a sequentially predictable order.
 *
 * Because this is a unit test that is running on the JVM,
 * all operations run on the same thread the tests are running on.
 * Otherwise, an error in thrown by the Observable.
 */
object UtilSchedulerProviderMockInit {
    fun init(utilSchedulerProvider: IUtilSchedulerProviderContract) {
        every { utilSchedulerProvider.io() } returns Schedulers.trampoline()
        every { utilSchedulerProvider.ui() } returns Schedulers.trampoline()
    }
}
