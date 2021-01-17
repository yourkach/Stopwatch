package com.example.stopwatc.ui.main

import java.util.concurrent.TimeUnit

data class TimeSnapshotModel(
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val millis: Int
) {
    companion object {
        fun fromMilliseconds(millisTotal: Long): TimeSnapshotModel {
            return TimeSnapshotModel(
                hours = TimeUnit.MILLISECONDS.toHours(millisTotal).toInt(),
                seconds = (TimeUnit.MILLISECONDS.toSeconds(millisTotal) % 60).toInt(),
                minutes = (TimeUnit.MILLISECONDS.toMinutes(millisTotal) % 60).toInt(),
                millis = (millisTotal % 1000 / 10).toInt()
            )
        }

        fun initialTime(): TimeSnapshotModel {
            return TimeSnapshotModel(
                hours = 0,
                minutes = 0,
                seconds = 0,
                millis = 0
            )
        }
    }
}
