package com.precopia.rxtracker.view.edittimeview

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.precopia.rxtracker.util.application
import com.precopia.rxtracker.view.edittimeview.IEditTimeContract.LogicEvents
import com.precopia.rxtracker.view.edittimeview.buildlogic.DaggerEditTimeComponent
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*
import javax.inject.Inject

class EditTimeView: DialogFragment(),
        IEditTimeContract.View,
        TimePickerDialog.OnTimeSetListener,
        DialogInterface.OnDismissListener {

    @Inject
    lateinit var dialog: TimePickerDialog

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
                .onTimeSetListener(this)
                .dismissListener(this)
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog.show(requireActivity().supportFragmentManager, "TAG")
    }


    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        logic.onEvent(LogicEvents.UpdateTime(args.id, args.dateTime, hourOfDay, minute))
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.dismiss()
    }
}