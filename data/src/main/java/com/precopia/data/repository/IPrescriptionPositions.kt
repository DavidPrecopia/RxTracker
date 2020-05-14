package com.precopia.data.repository

import io.reactivex.Completable

internal interface IPrescriptionPositions {
    fun update(id: Int, oldPosition: Int, newPosition: Int): Completable
}
