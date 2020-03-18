package com.precopia.rxtracker.view.common.buildlogic

import android.app.Application
import com.precopia.rxtracker.common.buildlogic.ViewCommonModule
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.view.common.MainActivity
import dagger.BindsInstance
import dagger.Component

@ViewScope
@Component(modules = [
    ViewCommonModule::class
])
interface MainActivityComponent {
    fun inject(view: MainActivity)

    @Component.Builder
    interface Builder {
        fun build(): MainActivityComponent

        @BindsInstance
        fun application(application: Application): Builder
    }
}