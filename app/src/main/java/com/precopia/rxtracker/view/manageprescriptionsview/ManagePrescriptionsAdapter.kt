package com.precopia.rxtracker.view.manageprescriptionsview

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.precopia.domain.datamodel.Prescription
import com.precopia.rxtracker.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.prescription_list_item.*
import kotlinx.android.synthetic.main.time_stamp_list_item.tv_title

class ManagePrescriptionsAdapter(private val itemTouchHelper: ItemTouchHelper):
        ListAdapter<Prescription, ManagePrescriptionsAdapter.PrescriptionViewHolder>(PrescriptionDiffCallback()),
        IManagePrescriptionsContact.Adapter {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PrescriptionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.prescription_list_item, parent, false),
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


    class PrescriptionViewHolder(private val view: View,
                                 private val itemTouchHelper: ItemTouchHelper):
            RecyclerView.ViewHolder(view),
            LayoutContainer {

        override val containerView: View?
            get() = view


        fun bindView(prescription: Prescription) {
            initDragHandle()
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
    }
}