package com.precopia.rxtracker.view.timestampview

import androidx.recyclerview.widget.DiffUtil
import com.precopia.domain.datamodel.TimeStamp

class TimeStampDiffCallback: DiffUtil.ItemCallback<TimeStamp>() {
    override fun areItemsTheSame(oldItem: TimeStamp, newItem: TimeStamp) =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TimeStamp, newItem: TimeStamp) =
            oldItem == newItem
}