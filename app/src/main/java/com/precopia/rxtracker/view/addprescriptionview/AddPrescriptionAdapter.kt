package com.precopia.rxtracker.view.addprescriptionview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.precopia.domain.datamodel.Prescription
import com.precopia.rxtracker.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.time_stamp_list_item.*

class AddPrescriptionAdapter:
        ListAdapter<Prescription, AddPrescriptionAdapter.PrescriptionViewHolder>(PrescriptionDiffCallback()),
        IAddPrescriptionContact.Adapter {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PrescriptionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.prescription_list_view, parent, false)
    )

    override fun onBindViewHolder(holder: PrescriptionViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }


    override fun displayList(list: List<Prescription>) {
        super.submitList(list)
    }


    class PrescriptionViewHolder(private val view: View):
            RecyclerView.ViewHolder(view),
            LayoutContainer {

        override val containerView: View?
            get() = view


        fun bindView(prescription: Prescription) {
            tv_title.text = prescription.title
        }
    }
}