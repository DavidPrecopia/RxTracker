package com.precopia.rxtracker.view.addprescriptionview

import androidx.recyclerview.widget.DiffUtil
import com.precopia.domain.datamodel.Prescription

class PrescriptionDiffCallback: DiffUtil.ItemCallback<Prescription>() {
    override fun areItemsTheSame(oldItem: Prescription, newItem: Prescription) =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Prescription, newItem: Prescription) =
            oldItem == newItem
}