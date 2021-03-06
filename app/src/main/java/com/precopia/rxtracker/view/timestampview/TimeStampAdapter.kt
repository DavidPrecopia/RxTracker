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

        private lateinit var timeStamp: TimeStamp


        override val containerView: View?
            get() = view


        fun bindView(timeStamp: TimeStamp) {
            this.timeStamp = timeStamp
            tv_title.text = timeStamp.title
            tv_time.text = timeStamp.time
            restoreSelectedState(timeStamp.isSelected, timeStamp.id)
            initContextMenu(timeStamp)
            initOnClickListener(timeStamp.id)
        }

        private fun restoreSelectedState(isSelected: Boolean, id: Int) {
            when (isSelected) {
                true -> setCheckedState(id)
                false -> setUncheckedState(id)
            }
        }

        private fun initContextMenu(timeStamp: TimeStamp) {
            iv_overflow_menu.setOnClickListener { getContextMenu(timeStamp).show() }
        }

        private fun getContextMenu(timeStamp: TimeStamp) =
                PopupMenu(iv_overflow_menu.context, iv_overflow_menu).apply {
                    inflate(R.menu.timestamp_popup_menu_list_item)
                    setOnMenuItemClickListener(getMenuClickListener(timeStamp))
                }

        private fun getMenuClickListener(timeStamp: TimeStamp) = PopupMenu.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_edit_time -> logic.onEvent(
                        LogicEvents.EditTime(timeStamp.id, timeStamp.time)
                )
                R.id.menu_item_edit_date -> logic.onEvent(
                        LogicEvents.EditDate(timeStamp.id, timeStamp.time)
                )
                R.id.menu_item_delete_timestamp -> logic.onEvent(
                        LogicEvents.DeleteItem(timeStamp.id, adapterPosition)
                )
                else -> UtilExceptions.throwException(IllegalArgumentException("Unknown menu ID"))
            }
            true
        }

        private fun initOnClickListener(id: Int) {
            time_stamp_list_item_root.setOnClickListener {
                when (timeStamp.isSelected) {
                    true -> setUncheckedState(id)
                    false -> setCheckedState(id)
                }
            }
        }


        private fun setCheckedState(id: Int) {
            displayCheckbox()
            timeStamp.isSelected = true
            logic.onEvent(LogicEvents.SelectedAdd(id))
        }

        private fun setUncheckedState(id: Int) {
            displayOverflow()
            timeStamp.isSelected = false
            logic.onEvent(LogicEvents.SelectedRemove(id))
        }


        private fun displayOverflow() {
            selection_checkbox.visibility = View.GONE
            iv_overflow_menu.visibility = View.VISIBLE
        }

        private fun displayCheckbox() {
            iv_overflow_menu.visibility = View.GONE
            selection_checkbox.visibility = View.VISIBLE
        }
    }
}