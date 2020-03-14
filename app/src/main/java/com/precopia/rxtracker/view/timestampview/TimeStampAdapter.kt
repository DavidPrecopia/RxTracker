package com.precopia.rxtracker.view.timestampview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.precopia.domain.datamodel.TimeStamp
import com.precopia.rxtracker.R
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.view.timestampview.ITimeStampViewContract.LogicEvents
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.time_stamp_list_item.*

class TimeStampAdapter(private val logic: ITimeStampViewContract.Logic):
        ListAdapter<TimeStamp, TimeStampAdapter.TimeStampViewHolder>(TimeStampDiffCallback()),
        ITimeStampViewContract.Adapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TimeStampViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.time_stamp_list_item, parent, false),
            logic
    )

    override fun onBindViewHolder(holder: TimeStampViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }


    override fun showList(list: List<TimeStamp>) {
        super.submitList(list)
    }

    override fun delete(position: Int) {
        notifyItemRemoved(position)
    }


    class TimeStampViewHolder(
            private val view: View,
            private val logic: ITimeStampViewContract.Logic
    ):
            RecyclerView.ViewHolder(view),
            LayoutContainer {

        override val containerView: View?
            get() = view


        fun bindView(timeStamp: TimeStamp) {
            tv_title.text = timeStamp.title
            tv_time.text = timeStamp.time
            initContextMenu(timeStamp.id)
        }

        private fun initContextMenu(id: Int) {
            iv_overflow_menu.setOnClickListener { getContextMenu(id).show() }
        }

        private fun getContextMenu(id: Int) =
                PopupMenu(iv_overflow_menu.context, iv_overflow_menu).apply {
                    inflate(R.menu.popup_menu_list_item)
                    setOnMenuItemClickListener(getMenuClickListener(id))
                }

        private fun getMenuClickListener(id: Int) = PopupMenu.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_delete -> logic.onEvent(
                        LogicEvents.DeleteItem(id, adapterPosition)
                )
                else -> UtilExceptions.throwException(IllegalArgumentException("Unknown menu ID"))
            }
            true
        }
    }
}