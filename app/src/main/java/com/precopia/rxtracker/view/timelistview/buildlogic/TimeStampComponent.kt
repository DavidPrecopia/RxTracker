package com.precopia.rxtracker.view.timelistview.buildlogic

import android.app.Application
import androidx.lifecycle.ViewModelStore
import com.precopia.rxtracker.common.buildlogic.ViewCommonModule
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.view.timelistview.TimeStampView
import dagger.BindsInstance
import dagger.Component

@ViewScope
@Component(
    modules = [
        TimeStampModule::class,
        ViewCommonModule::class
    ]
)
interface TimeStampComponent {
    fun inject(timeStampView: TimeStampView)
    @Component.Builder
    interface Builder {
        fun build(): TimeStampComponent

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun view(view: ViewModelStore): Builder
    }
}