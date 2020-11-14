package com.precopia.rxtracker.view.timestampview

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.precopia.rxtracker.R
import com.precopia.rxtracker.util.application
import com.precopia.rxtracker.util.navigate
import com.precopia.rxtracker.view.timestampview.ITimeStampViewContract.LogicEvents
import com.precopia.rxtracker.view.timestampview.ITimeStampViewContract.ViewEvents
import com.precopia.rxtracker.view.timestampview.buildlogic.DaggerTimeStampComponent
import kotlinx.android.synthetic.main.time_stamp_view.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
import javax.inject.Provider

class TimeStampView: Fragment(R.layout.time_stamp_view),
        ITimeStampViewContract.View {


    private lateinit var menu: Menu


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
            observe().observe(viewLifecycleOwner, { evalViewEvents(it) })
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
            is ViewEvents.OpenEditTimeView -> openEditTimeView(event.id, event.dateTime)
            is ViewEvents.OpenEditDateView -> openEditDateView(event.id, event.dateTime)
            ViewEvents.DisplayDeleteButton -> changeVisibilityDeleteButton(true)
            ViewEvents.HideDeleteButton -> changeVisibilityDeleteButton(false)
        }
    }

    private fun openAddTimeStampView() {
        navigate(
                TimeStampViewDirections.actionTimesListViewToAddTimeStampView()
        )
    }

    private fun openPrescriptionView() {
        navigate(
                TimeStampViewDirections.actionTimesListViewToAddPrescriptionView()
        )
    }

    private fun openEditTimeView(id: Int, time: String) {
        navigate(
                TimeStampViewDirections.actionTimesListViewToEditTimeView(id, time)
        )
    }

    private fun openEditDateView(id: Int, time: String) {
        navigate(
                TimeStampViewDirections.actionTimesListViewToEditDateView(id, time)
        )
    }


    private fun changeVisibilityDeleteButton(visible: Boolean) {
        menu.findItem(R.id.menu_id_delete_all).isVisible = visible
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
            adapter = (this@TimeStampView.adapter as RecyclerView.Adapter<*>)
        }
    }

    private fun initFab() {
        fab.setOnClickListener { logic.onEvent(LogicEvents.OpenAddTimeStampView) }
        fabScrollListener()
    }

    private fun fabScrollListener() {
        recycler_view.addOnScrollListener(object: RecyclerView.OnScrollListener() {
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
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_id_prescriptions -> logic.onEvent(LogicEvents.OpenAddPrescriptionView)
            R.id.menu_id_delete_all -> logic.onEvent(LogicEvents.DeleteAll)
        }
        return super.onOptionsItemSelected(item)
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


    override fun onDestroyView() {
        super.onDestroyView()
        recycler_view.adapter = null
    }
}
