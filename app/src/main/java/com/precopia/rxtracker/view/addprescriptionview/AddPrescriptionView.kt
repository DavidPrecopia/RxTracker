package com.precopia.rxtracker.view.addprescriptionview

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.precopia.rxtracker.R
import com.precopia.rxtracker.util.application
import com.precopia.rxtracker.util.navigateUp
import com.precopia.rxtracker.util.toast
import com.precopia.rxtracker.view.addprescriptionview.IAddPrescriptionContact.LogicEvents
import com.precopia.rxtracker.view.addprescriptionview.IAddPrescriptionContact.ViewEvents
import com.precopia.rxtracker.view.addprescriptionview.buildlogic.DaggerAddPrescriptionComponent
import kotlinx.android.synthetic.main.add_prescription_view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
import javax.inject.Provider

class AddPrescriptionView: Fragment(R.layout.add_prescription_view) {


    @Inject
    lateinit var logic: IAddPrescriptionContact.Logic

    @Inject
    lateinit var adapter: IAddPrescriptionContact.Adapter

    @Inject
    lateinit var layoutManger: Provider<LinearLayoutManager>

    @Inject
    lateinit var dividerItemDecorator: RecyclerView.ItemDecoration


    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    private fun inject() {
        DaggerAddPrescriptionComponent.builder()
                .application(application)
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
            is ViewEvents.DisplayList -> {
                adapter.displayList(event.list)
                displayList()
            }
            is ViewEvents.DisplayError -> displayError(event.message)
            is ViewEvents.DisplayMessage -> toast(event.message)
        }
    }


    private fun init() {
        initToolbar()
        initEditTextHint()
        initRecyclerView()
        initClickListeners()
    }

    private fun initToolbar() {
        with(toolbar) {
            (activity as AppCompatActivity).setSupportActionBar(this)
            (activity as AppCompatActivity).supportActionBar?.title =
                    getString(R.string.nav_label_add_prescription)
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener { navigateUp() }
        }
    }

    private fun initEditTextHint() {
        text_input_layout.hint = getString(R.string.edit_text_hint_prescription)
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutManger.get()
            addItemDecoration(dividerItemDecorator)
            adapter = this@AddPrescriptionView.adapter as RecyclerView.Adapter<*>
        }
    }

    private fun initClickListeners() {
        button_save.setOnClickListener {
            with(text_input_edit_text) {
                logic.onEvent(LogicEvents.Save(text.toString()))
                text?.clear()
            }
        }
    }


    private fun displayLoading() {
        tv_error.isGone = true
        recycler_view_parent.isGone = true

        progress_bar.isVisible = true
    }

    private fun displayList() {
        progress_bar.isGone = true
        tv_error.isGone = true

        recycler_view_parent.isVisible = true
    }

    private fun displayError(errorMessage: String) {
        progress_bar.isGone = true
        recycler_view_parent.isGone = true

        tv_error.text = errorMessage
        tv_error.isVisible = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        recycler_view.adapter = null
    }
}