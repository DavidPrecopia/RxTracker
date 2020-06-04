package com.precopia.rxtracker.view.editdateview.buildlogic

import android.content.DialogInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.util.IUtilNightModeContract
import com.precopia.rxtracker.util.IUtilParseDateTime
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.UtilParseDateTime
import com.precopia.rxtracker.view.editdateview.EditDateLogic
import com.precopia.rxtracker.view.editdateview.IEditDateContract
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_2
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
class EditDateModule {
    @ViewScope
    @Provides
    fun dialog(onDateSetListener: DatePickerDialog.OnDateSetListener,
               dateTime: String,
               utilParseDateTime: IUtilParseDateTime,
               dismissListener: DialogInterface.OnDismissListener,
               utilNightMode: IUtilNightModeContract
    ): DatePickerDialog {
        val parsedDate = utilParseDateTime.parsedDate(dateTime)
        val white = "#FFFFFF"

        return DatePickerDialog.newInstance(
                onDateSetListener,
                parsedDate[2],
                parsedDate[0],
                parsedDate[1]
        ).apply {
            version = VERSION_2
            isThemeDark = utilNightMode.nightModeEnabled
            setOnDismissListener(dismissListener)
            dismissOnPause(true)
            setCancelColor(white)
            setOkColor(white)
        }
    }

    @ViewScope
    @Provides
    fun logic(view: Fragment,
              factory: ViewModelProvider.NewInstanceFactory
    ): IEditDateContract.Logic {
        return ViewModelProvider(view, factory).get(EditDateLogic::class.java)
    }

    @ViewScope
    @Provides
    fun factory(repo: ITimeStampRepoContract,
                utilSchedulerProvider: IUtilSchedulerProviderContract,
                disposable: CompositeDisposable,
                utilParseDateTime: IUtilParseDateTime
    ): ViewModelProvider.NewInstanceFactory {
        return EditDateLogicFactory(
                repo, utilSchedulerProvider, disposable, utilParseDateTime
        )
    }

    @ViewScope
    @Provides
    fun utilParseDateTime(): IUtilParseDateTime {
        return UtilParseDateTime()
    }
}