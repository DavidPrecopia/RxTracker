package com.precopia.rxtracker.view.editdateview.buildlogic

import android.app.Application
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import com.precopia.rxtracker.common.buildlogic.ViewCommonModule
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.view.editdateview.EditDateView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.BindsInstance
import dagger.Component

@ViewScope
@Component(modules = [
    EditDateModule::class,
    ViewCommonModule::class
])
interface EditDateComponent {
    fun inject(editDateView: EditDateView)

    @Component.Builder
    interface Builder {
        fun build(): EditDateComponent

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun view(view: Fragment): Builder

        @BindsInstance
        fun onDateSetListener(onDateSetListener: DatePickerDialog.OnDateSetListener): Builder

        @BindsInstance
        fun dismissListener(dismissListener: DialogInterface.OnDismissListener): Builder

        @BindsInstance
        fun dateTime(dateTime: String): Builder
    }

}