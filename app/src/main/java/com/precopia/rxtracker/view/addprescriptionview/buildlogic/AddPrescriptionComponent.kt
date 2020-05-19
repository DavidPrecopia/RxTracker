package com.precopia.rxtracker.view.addprescriptionview.buildlogic

import android.app.Application
import androidx.fragment.app.Fragment
import com.precopia.rxtracker.common.buildlogic.ViewCommonModule
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.view.addprescriptionview.AddPrescriptionView
import com.precopia.rxtracker.view.addprescriptionview.ItemTouchHelperCallback
import dagger.BindsInstance
import dagger.Component

@ViewScope
@Component(modules = [
    AddPrescriptionModule::class,
    ViewCommonModule::class
])
interface AddPrescriptionComponent {
    fun inject(view: AddPrescriptionView)

    @Component.Builder
    interface Builder {
        fun build(): AddPrescriptionComponent

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun view(view: Fragment): Builder

        @BindsInstance
        fun movementCallback(movementCallback: ItemTouchHelperCallback.MovementCallback): Builder
    }
}