package com.precopia.rxtracker.view.editdateview

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.precopia.rxtracker.util.application
import com.precopia.rxtracker.view.editdateview.IEditDateContract.LogicEvents
import com.precopia.rxtracker.view.editdateview.buildlogic.DaggerEditDateComponent
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import javax.inject.Inject

class EditDateView: DialogFragment(),
        IEditDateContract.View,
        DatePickerDialog.OnDateSetListener,
        DialogInterface.OnDismissListener {

    @Inject
    lateinit var dialog: DatePickerDialog

    @Inject
    lateinit var logic: IEditDateContract.Logic

    private val args: EditDateViewArgs by navArgs()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    private fun inject() {
        DaggerEditDateComponent.builder()
                .application(application)
                .view(this)
                .onDateSetListener(this)
                .dismissListener(this)
                .dateTime(args.dateTime)
                .build()
                .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog.show(requireActivity().supportFragmentManager, "TAG")
    }


    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        logic.onEvent(LogicEvents.UpdateDate(
                args.id, args.dateTime, monthOfYear, dayOfMonth, year
        ))
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}