package com.precopia.rxtracker.view.timelistview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.precopia.domain.datamodel.TimeStamp

class TimeStampAdapter(private val view: ITimeStampListViewContract.View) :
    ListAdapter<TimeStamp, TimeStampAdapter.TimeStampViewHolder>(TimeStampDiffCallback()),
    ITimeStampListViewContract.Adapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeStampViewHolder {
        TODO("not implemented")
    }

    override fun onBindViewHolder(holder: TimeStampViewHolder, position: Int) {
        TODO("not implemented")
    }


    override fun showList(list: List<TimeStamp>) {
        super.submitList(list)
    }

    override fun delete(position: Int) {
        notifyItemRemoved(position)
    }


    class TimeStampViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}