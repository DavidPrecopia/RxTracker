package com.precopia.rxtracker.view.edittimeview.buildlogic

import android.app.Application
import android.content.DialogInterface
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.util.IUtilParseDateTime
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.IUtilThemeContract
import com.precopia.rxtracker.util.UtilParseDateTime
import com.precopia.rxtracker.view.edittimeview.EditTimeLogic
import com.precopia.rxtracker.view.edittimeview.IEditTimeContract
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_2
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
class EditTimeModule {
    @ViewScope
    @Provides
    fun dialog(onTimeSetListener: TimePickerDialog.OnTimeSetListener,
               dateTime: String,
               utilParseDateTime: IUtilParseDateTime,
               application: Application,
               dismissListener: DialogInterface.OnDismissListener,
               utilTheme: IUtilThemeContract
    ): TimePickerDialog {
        val parsedTime = utilParseDateTime.parsedTime(dateTime)
        val white = "#FFFFFF"

        return TimePickerDialog.newInstance(
                onTimeSetListener,
                parsedTime[0],
                parsedTime[1],
                DateFormat.is24HourFormat(application)
        ).apply {
            version = VERSION_2
            isThemeDark = utilTheme.isNightModeEnabled()
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
    ): IEditTimeContract.Logic {
        return ViewModelProvider(view, factory).get(EditTimeLogic::class.java)
    }

    @ViewScope
    @Provides
    fun factory(repo: ITimeStampRepoContract,
                utilSchedulerProvider: IUtilSchedulerProviderContract,
                disposable: CompositeDisposable,
                utilParseDateTime: IUtilParseDateTime
    ): ViewModelProvider.NewInstanceFactory {
        return EditTimeLogicFactory(
                repo, utilSchedulerProvider, disposable, utilParseDateTime
        )
    }

    @ViewScope
    @Provides
    fun utilParseDateTime(): IUtilParseDateTime {
        return UtilParseDateTime()
    }
}