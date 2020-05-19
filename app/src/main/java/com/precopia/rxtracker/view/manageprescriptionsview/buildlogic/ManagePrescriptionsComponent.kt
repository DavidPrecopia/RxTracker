package com.precopia.rxtracker.view.manageprescriptionsview.buildlogic

import android.app.Application
import androidx.fragment.app.Fragment
import com.precopia.rxtracker.common.buildlogic.ViewCommonModule
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.view.manageprescriptionsview.ItemTouchHelperCallback
import com.precopia.rxtracker.view.manageprescriptionsview.ManagePrescriptionsView
import dagger.BindsInstance
import dagger.Component

@ViewScope
@Component(modules = [
    ManagePrescriptionsModule::class,
    ViewCommonModule::class
])
interface ManagePrescriptionsComponent {
    fun inject(view: ManagePrescriptionsView)

    @Component.Builder
    interface Builder {
        fun build(): ManagePrescriptionsComponent

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun view(view: Fragment): Builder

        @BindsInstance
        fun movementCallback(movementCallback: ItemTouchHelperCallback.MovementCallback): Builder
    }
}