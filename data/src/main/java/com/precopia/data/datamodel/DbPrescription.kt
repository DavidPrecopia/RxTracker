package com.precopia.data.datamodel

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_ID_COLUMN
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_TABLE_NAME
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_TITLE_COLUMN

@Entity(tableName = PRESCRIPTION_TABLE_NAME)
internal data class DbPrescription(
        @NonNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = PRESCRIPTION_ID_COLUMN)
        val id: Int = 0,
        @NonNull
        @ColumnInfo(name = PRESCRIPTION_TITLE_COLUMN)
        val title: String
)