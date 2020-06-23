package com.precopia.rxtracker.view.manageprescriptionsview

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.precopia.domain.datamodel.Prescription
import com.precopia.rxtracker.R
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.view.manageprescriptionsview.IManagePrescriptionsContact.LogicEvents
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.prescription_list_item.*
import kotlinx.android.synthetic.main.time_stamp_list_item.tv_title

class ManagePrescriptionsAdapter(private val logic: IManagePrescriptionsContact.Logic,
                                 private val itemTouchHelper: ItemTouchHelper):
        ListAdapter<Prescription, ManagePrescriptionsAdapter.PrescriptionViewHolder>(PrescriptionDiffCallback()),
        IManagePrescriptionsContact.Adapter {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PrescriptionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.prescription_list_item, parent, false),
            logic,
            itemTouchHelper
    )

    override fun onBindViewHolder(holder: PrescriptionViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }


    override fun displayList(list: List<Prescription>) {
        super.submitList(list)
    }

    override fun move(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun delete(position: Int) {
        notifyItemRemoved(position)
    }


    class PrescriptionViewHolder(private val view: View,
                                 private val logic: IManagePrescriptionsContact.Logic,
                                 private val itemTouchHelper: ItemTouchHelper):
            RecyclerView.ViewHolder(view),
            LayoutContainer {

        override val containerView: View?
            get() = view


        fun bindView(prescription: Prescription) {
            initDragHandle()
            initContextMenu(prescription.id)
            tv_title.text = prescription.title
        }

        private fun initDragHandle() {
            iv_drag_handle.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    itemTouchHelper.startDrag(this)
                }
                view.performClick()
                true
            }
        }

        private fun initContextMenu(id: Int) {
            iv_overflow_menu.setOnClickListener { getContextMenu(id).show() }
        }

        private fun getContextMenu(id: Int) =
                PopupMenu(iv_overflow_menu.context, iv_overflow_menu).apply {
                    inflate(R.menu.prescription_popup_menu_list_item)
                    setOnMenuItemClickListener(getMenuClickListener(id))
                }

        private fun getMenuClickListener(id: Int) = PopupMenu.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_delete_prescription -> logic.onEvent(
                        LogicEvents.DeleteItem(id, adapterPosition)
                )
                else -> UtilExceptions.throwException(IllegalArgumentException("Unknown menu ID"))
            }
            true
        }
    }
}