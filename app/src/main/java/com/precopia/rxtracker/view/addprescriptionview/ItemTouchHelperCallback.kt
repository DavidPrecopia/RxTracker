package com.precopia.rxtracker.view.addprescriptionview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(private val movementCallback: MovementCallback) : ItemTouchHelper.Callback() {

    interface MovementCallback {
        fun dragging(fromPosition: Int, toPosition: Int)

        fun movedPermanently(newPosition: Int)
    }


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) =
            makeMovementFlags(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    0
            )

    override fun isLongPressDragEnabled() = false

    override fun isItemViewSwipeEnabled() = false


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        movementCallback.dragging(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}


    /**
     * This is called post [ItemTouchHelperCallback.onMove] and [ItemTouchHelperCallback.onSwiped].
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        movementCallback.movedPermanently(viewHolder.bindingAdapterPosition)
    }
}
