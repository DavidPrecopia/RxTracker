package com.precopia.data.datamodel

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_DATE_COLUMN
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_ID_COLUMN
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_TABLE_NAME
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_TITLE_COLUMN

@Entity(tableName = TIME_STAMP_TABLE_NAME)
internal data class DbTimeStamp(
        @NonNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = TIME_STAMP_ID_COLUMN)
        val id: Int = 0,
        @NonNull
        @ColumnInfo(name = TIME_STAMP_TITLE_COLUMN)
        val title: String,
        @NonNull
        @ColumnInfo(name = TIME_STAMP_DATE_COLUMN)
        val time: String
)