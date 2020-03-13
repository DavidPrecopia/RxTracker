package com.precopia.rxtracker.view.addtimestamp.bulidlogic

import android.app.Application
import androidx.fragment.app.Fragment
import com.precopia.rxtracker.common.buildlogic.ViewCommonModule
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.view.addtimestamp.AddTimeStampView
import dagger.BindsInstance
import dagger.Component

@ViewScope
@Component(modules = [
    AddTimeStampModule::class,
    ViewCommonModule::class
])
interface AddTimeStampComponent {
    fun inject(addTimeStampView: AddTimeStampView)

    @Component.Builder
    interface Builder {
        fun build(): AddTimeStampComponent

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun view(view: Fragment): Builder
    }
}