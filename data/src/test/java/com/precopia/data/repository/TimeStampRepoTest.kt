package com.precopia.data.repository

import com.precopia.data.dao.TimeStampDao
import com.precopia.data.datamodel.DbTimeStamp
import com.precopia.data.util.ICurrentTimeUtil
import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TimeStampRepoTest {


    private val dao = mockk<TimeStampDao>(relaxUnitFun = true)

    private val timeUtil = mockk<ICurrentTimeUtil>(relaxUnitFun = true)


    private val repo = TimeStampRepo(dao, timeUtil)


    @BeforeEach
    fun init() {
        clearAllMocks()
    }


    /**
     * - Accept the prescription's title as a parameter.
     * - Acquire the current date and time.
     * - Create a new instance of [DbTimeStamp].
     * - Pass the instance to the Dao.
     */
    @Test
    fun delete() {
        val capturedArg = CapturingSlot<DbTimeStamp>()
        val rxTitle = "title"
        val currentTimeString = "time"

        every { timeUtil.currentTime() } returns currentTimeString
        every { dao.add(timeStamp = capture(capturedArg)) } answers { Completable.complete() }

        repo.add(rxTitle)

        verify { dao.add(capturedArg.captured) }
    }
}