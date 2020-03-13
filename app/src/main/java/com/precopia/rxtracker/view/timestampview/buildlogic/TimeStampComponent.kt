package com.precopia.rxtracker.view.timestampview.buildlogic

import android.app.Application
import androidx.fragment.app.Fragment
import com.precopia.rxtracker.common.buildlogic.ViewCommonModule
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.view.timestampview.TimeStampView
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
        fun view(view: Fragment): Builder
    }
}