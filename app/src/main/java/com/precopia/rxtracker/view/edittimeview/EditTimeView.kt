package com.precopia.rxtracker.view.edittimeview

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.precopia.rxtracker.util.application
import com.precopia.rxtracker.view.edittimeview.IEditTimeContract.LogicEvents
import com.precopia.rxtracker.view.edittimeview.buildlogic.DaggerEditTimeComponent
import java.util.*
import javax.inject.Inject

class EditTimeView: DialogFragment(),
        TimePickerDialog.OnTimeSetListener,
        IEditTimeContract.View {

    @Inject
    lateinit var logic: IEditTimeContract.Logic

    @Inject
    lateinit var calendar: Calendar

    private val args: EditTimeViewArgs by navArgs()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    private fun inject() {
        DaggerEditTimeComponent.builder()
                .application(application)
                .view(this)
                .build()
                .inject(this)
    }

    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(
                context,
                this,
                getCurrentHour(),
                getCurrentMinute(),
                getIs24Hour()
        )
    }

    private fun getCurrentHour() = calendar.get(Calendar.HOUR_OF_DAY)

    private fun getCurrentMinute() = calendar.get(Calendar.MINUTE)

    private fun getIs24Hour() = DateFormat.is24HourFormat(context)


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        logic.onEvent(LogicEvents.UpdateTime(args.id, hourOfDay, minute))
    }
}