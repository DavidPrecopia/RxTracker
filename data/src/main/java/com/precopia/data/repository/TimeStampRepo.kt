package com.precopia.data.repository

import com.precopia.data.dao.TimeStampDao
import com.precopia.data.datamodel.DbTimeStamp
import com.precopia.data.util.ICurrentTimeUtil
import com.precopia.domain.datamodel.TimeStamp
import com.precopia.domain.repository.ITimeStampRepoContract
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

internal class TimeStampRepo(private val dao: TimeStampDao, private val timeUtil: ICurrentTimeUtil) :
    ITimeStampRepoContract {

    override fun getAll(): Flowable<List<TimeStamp>> =
        RxJavaBridge.toV3Flowable(dao.getAll())
            .map { mapDbTimeStamp(it) }

    override fun add(rxTitle: String): Completable =
        RxJavaBridge.toV3Completable(dao.add(DbTimeStamp(title = rxTitle, time = timeUtil.currentTime())))

    override fun delete(id: Int): Completable =
        RxJavaBridge.toV3Completable(dao.delete(id))


    private fun mapDbTimeStamp(list: List<DbTimeStamp>) = list.map {
        TimeStamp(it.id, it.title, it.time)
    }
}