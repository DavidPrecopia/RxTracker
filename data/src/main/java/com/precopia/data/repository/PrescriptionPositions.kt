package com.precopia.data.repository

import com.precopia.data.dao.PrescriptionDao
import com.precopia.data.datamodel.DbPrescription
import io.reactivex.Completable
import io.reactivex.CompletableEmitter

internal class PrescriptionPositions(private val dao: PrescriptionDao): IPrescriptionPositions {

    override fun update(id: Int, oldPosition: Int, newPosition: Int): Completable {
        return when {
            positionsAreSame(oldPosition, newPosition) -> Completable.complete()
            positionsAreInvalid(oldPosition, newPosition) -> Completable.error(IllegalArgumentException())
            else -> Completable.create { updateDb(id, oldPosition, newPosition, it) }
        }
    }

    private fun positionsAreSame(oldPos: Int, newPos: Int) =
            oldPos == newPos

    private fun positionsAreInvalid(oldPos: Int, newPos: Int) =
            oldPos < 0 || newPos < 0


    private fun updateDb(id: Int, oldPos: Int, newPos: Int, emitter: CompletableEmitter) {
        val list = dao.updatePositionAndGetAll(id, getNewTempPosition(oldPos, newPos))
        dao.updateAllPositions(reorderConsecutively(list))
                .doOnError { emitter.onError(it) }
                .doOnComplete { emitter.onComplete() }
                .subscribe()
    }

    private fun getNewTempPosition(oldPos: Int, newPos: Int) = when {
        newPos > oldPos -> newPos + 0.5
        else -> newPos - 0.5
    }

    private fun reorderConsecutively(list: MutableList<DbPrescription>): List<DbPrescription> {
        for ((index, value) in list.withIndex()) {
            value.position = index.toDouble()
        }
        return list
    }
}
