package com.precopia.rxtracker.view.addtimestamp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.precopia.rxtracker.R
import com.precopia.rxtracker.util.application
import com.precopia.rxtracker.util.navigateUp
import com.precopia.rxtracker.util.toast
import com.precopia.rxtracker.view.addtimestamp.IAddTimeStampContract.LogicEvents
import com.precopia.rxtracker.view.addtimestamp.IAddTimeStampContract.ViewEvents
import com.precopia.rxtracker.view.addtimestamp.bulidlogic.DaggerAddTimeStampComponent
import kotlinx.android.synthetic.main.add_time_stamp_view.*
import javax.inject.Inject

class AddTimeStampView: Fragment(R.layout.add_time_stamp_view),
        IAddTimeStampContract.View {

    @Inject
    lateinit var logic: IAddTimeStampContract.Logic

    @Inject
    lateinit var adapter: ArrayAdapter<String>


    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    private fun inject() {
        DaggerAddTimeStampComponent.builder()
                .application(application)
                .activity((activity as AppCompatActivity))
                .view(this)
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        with(logic) {
            onEvent(LogicEvents.OnStart)
            observe().observe(viewLifecycleOwner, Observer { evalViewEvents(it) })
        }
    }

    private fun evalViewEvents(event: ViewEvents) {
        when (event) {
            ViewEvents.DisplayLoading -> displayLoading()
            is ViewEvents.DisplayList -> displayList(event.list)
            is ViewEvents.DisplayError -> displayError(event.message)
            is ViewEvents.DisplayMessage -> toast(event.message)
            ViewEvents.Close -> navigateUp()
        }
    }

    private fun init() {
        initToolbar()
        initClickListeners()
        initSpinner()
    }


    private fun initToolbar() {
        with(toolbar) {
            (activity as AppCompatActivity).setSupportActionBar(this)
            (activity as AppCompatActivity).supportActionBar?.title =
                    getString(R.string.nav_label_add_time_stamp)
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener { navigateUp() }
        }
    }

    private fun initClickListeners() {
        button_confirm.setOnClickListener {
            logic.onEvent(LogicEvents.Save(getCurrentSpinnerValue()))
        }
    }

    private fun initSpinner() {
        spinner.adapter = adapter
    }


    private fun displayLoading() {
        tv_error.isGone = true
        group.isVisible = true
        progress_bar.isVisible = true
    }

    private fun displayList(list: List<String>) {
        tv_error.isGone = true
        progress_bar.isGone = true
        group.isVisible = true

        with(adapter) {
            clear()
            addAll(list)
        }
    }

    private fun displayError(message: String) {
        progress_bar.isGone = true
        group.isGone = true
        with(tv_error) {
            text = message
            isVisible = true
        }
    }


    private fun getCurrentSpinnerValue() = spinner.selectedItem as String
}