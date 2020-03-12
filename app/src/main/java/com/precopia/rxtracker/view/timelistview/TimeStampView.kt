package com.precopia.rxtracker.view.timelistview

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.precopia.rxtracker.view.timelistview.ITimeStampViewContract.LogicEvents
import com.precopia.rxtracker.view.timelistview.ITimeStampViewContract.ViewEvents
import com.precopia.rxtracker.view.timelistview.buildlogic.DaggerTimeStampComponent
import kotlinx.android.synthetic.main.time_stamp_view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
import javax.inject.Provider

open class TimeStampView : Fragment(R.layout.time_stamp_view),
    ITimeStampViewContract.View {


    @Inject
    lateinit var logic: ITimeStampViewContract.Logic

    @Inject
    lateinit var adapter: ITimeStampViewContract.Adapter

    @Inject
    lateinit var layoutManger: Provider<LinearLayoutManager>

    @Inject
    lateinit var dividerItemDecorator: RecyclerView.ItemDecoration


    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    private fun inject() {
        DaggerTimeStampComponent.builder()
            .application(application)
            .view(this)
            .build()
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
                adapter.showList(event.list)
                displayList()
            }
            is ViewEvents.DisplayError -> displayError(event.message)
            is ViewEvents.DeleteItem -> adapter.delete(event.position)
            ViewEvents.OpenAddTimeStampView -> openAddTimeStampView()
            ViewEvents.OpenPrescriptionView -> openPrescriptionView()
        }
    }

    private fun openAddTimeStampView() {
        TODO("Implement Navigation to the View")
    }

    private fun openPrescriptionView() {
        TODO("Implement Navigation to the View")
    }


    private fun init() {
        initToolbar()
        initRecyclerView()
        initFab()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutManger.get()
            addItemDecoration(dividerItemDecorator)
            adapter = this@TimeStampView.adapter as RecyclerView.Adapter<*>
        }
    }

    private fun initFab() {
        fab.setOnClickListener { logic.onEvent(LogicEvents.OpenAddPrescriptionView) }
        fabScrollListener()
    }

    private fun fabScrollListener() {
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    fab.hide()
                } else if (dy < 0) {
                    fab.show()
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_id_prescriptions -> {
            logic.onEvent(LogicEvents.OpenAddPrescriptionView)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun displayLoading() {
        tv_error.isGone = true
        recycler_view.isGone = true
        fab.isGone = true

        progress_bar.isVisible = true
    }

    private fun displayList() {
        progress_bar.isGone = true
        tv_error.isGone = true

        recycler_view.isVisible = true
        fab.show()
    }

    private fun displayError(errorMessage: String) {
        progress_bar?.isGone = true
        recycler_view?.isGone = true
        fab?.show()

        tv_error?.text = errorMessage
        tv_error?.isVisible = true
    }
}
