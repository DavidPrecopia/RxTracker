package com.precopia.data.repository

import com.precopia.data.dao.TimeStampDao
import com.precopia.data.datamodel.DbTimeStamp
import com.precopia.data.datamodel.DbTimeStampDelete
import com.precopia.data.util.ITimeUtil
import com.precopia.domain.datamodel.TimeStamp
import com.precopia.domain.repository.ITimeStampRepoContract
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.util.*

internal class TimeStampRepo(private val dao: TimeStampDao, private val timeUtil: ITimeUtil):
        ITimeStampRepoContract {

    override fun getAll(): Flowable<List<TimeStamp>> =
            RxJavaBridge.toV3Flowable(dao.getAll())
                    .map { mapDbTimeStamp(it) }

    override fun add(rxTitle: String): Completable =
            RxJavaBridge.toV3Completable(dao.add(
                    DbTimeStamp(title = rxTitle, time = timeUtil.getCurrentTime(), year = timeUtil.getCurrentYear())
            ))

    override fun delete(id: Int): Completable =
            RxJavaBridge.toV3Completable(dao.delete(id))

    override fun deleteAll(ids: List<Int>): Completable =
            RxJavaBridge.toV3Completable(dao.deleteAll(ids.map { DbTimeStampDelete(it) }))

    override fun modifyDateTime(id: Int, calendar: Calendar): Completable =
            RxJavaBridge.toV3Completable(dao.modifyDateTime(
                    id, timeUtil.calendarToString(calendar), timeUtil.calendarToYear(calendar)
            ))


    private fun mapDbTimeStamp(list: List<DbTimeStamp>) = list.map {
        TimeStamp(it.id, it.title, it.time, it.year)
    }
}