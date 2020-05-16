package com.precopia.rxtracker.view.edittimeview.buildlogic

import android.app.Application
import androidx.fragment.app.Fragment
import com.precopia.rxtracker.common.buildlogic.ViewCommonModule
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.view.edittimeview.EditTimeView
import dagger.BindsInstance
import dagger.Component

@ViewScope
@Component(modules = [
    EditTimeModule::class,
    ViewCommonModule::class
])
interface EditTimeComponent {
    fun inject(editTimeView: EditTimeView)

    @Component.Builder
    interface Builder {
        fun build(): EditTimeComponent

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun view(view: Fragment): Builder
    }
}