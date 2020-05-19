package com.precopia.rxtracker.view.addprescriptionview

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
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

class AddPrescriptionView: Fragment(R.layout.add_prescription_view),
        ItemTouchHelperCallback.MovementCallback {


    @Inject
    lateinit var logic: IAddPrescriptionContact.Logic

    @Inject
    lateinit var adapter: IAddPrescriptionContact.Adapter

    @Inject
    lateinit var layoutManger: Provider<LinearLayoutManager>

    @Inject
    lateinit var dividerItemDecorator: RecyclerView.ItemDecoration

    @Inject
    lateinit var itemTouchHelper: ItemTouchHelper


    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    private fun inject() {
        DaggerAddPrescriptionComponent.builder()
                .application(application)
                .view(this)
                .movementCallback(this)
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
            is ViewEvents.Dragging -> adapter.move(event.fromPosition, event.toPosition)
        }
    }


    private fun init() {
        initToolbar()
        initEditText()
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

    private fun initEditText() {
        text_input_layout.hint = getString(R.string.edit_text_hint_prescription)
        initEditTextListener()
    }

    private fun initEditTextListener() {
        text_input_edit_text.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                logic.onEvent(LogicEvents.Save(getEnteredText()))
                text_input_edit_text.text?.clear()
                handled = true
            }
            handled
        }
    }


    private fun initRecyclerView() {
        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutManger.get()
            addItemDecoration(dividerItemDecorator)
            itemTouchHelper.attachToRecyclerView(this)
            adapter = (this@AddPrescriptionView.adapter as RecyclerView.Adapter<*>).apply {
                stateRestorationPolicy = PREVENT_WHEN_EMPTY
            }
        }
    }

    private fun initClickListeners() {
        button_save.setOnClickListener {
            with(text_input_edit_text) {
                logic.onEvent(LogicEvents.Save(getEnteredText()))
                text?.clear()
            }
        }
    }


    private fun getEnteredText() =
            text_input_edit_text.text.toString().trim { it <= ' ' }


    override fun dragging(fromPosition: Int, toPosition: Int) {
        logic.onEvent(LogicEvents.Dragging(fromPosition, toPosition))
    }

    override fun movedPermanently(newPosition: Int) {
        logic.onEvent(LogicEvents.PermanentlyMoved(newPosition))
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