package com.example.stopwatc.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatc.logDebug
import com.example.stopwatc.startRepeatableAction
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StopwatchViewModel : ViewModel() {

    private var stopwatch: StopwatchHelper? = null

    private val _timeTextData = MutableLiveData(TimeSnapshotModel.initialTime())

    val timeSnapshotData: LiveData<TimeSnapshotModel>
        get() = _timeTextData

    private val _progressUpdateData = MutableLiveData(
        ProgressUpdateModel(0.0)
    )

    val progressUpdateData: LiveData<ProgressUpdateModel>
        get() = _progressUpdateData

    private var sendDataJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    fun onTimeTextClick() {
        stopwatch?.apply {
            if (isActive) {
                pauseTimer()
            } else {
                startTimer()
            }
        } ?: let {
            stopwatch = StopwatchHelper().apply { startTimer() }
        }
        onTimerStatusChange()
    }

    fun onResetClick() {
        stopwatch = null
        _timeTextData.value = TimeSnapshotModel.initialTime()
        onTimerStatusChange()
    }

    private fun onTimerStatusChange() {
        if (stopwatch?.isActive == true) {
            sendDataJob = viewModelScope.launch {
                startRepeatableAction(repeatMillis = 30) {
                    stopwatch?.let { watch ->
                        watch.isActive.takeIf { it }
                                ?.apply {
                                    val totalMillis = watch.elapsedMillis
                                    _timeTextData.postValue(TimeSnapshotModel.fromMilliseconds(totalMillis))
                                    val progressToOneMinute = (totalMillis % 60000) / 60000.0
                                    _progressUpdateData.postValue(ProgressUpdateModel(progressToOneMinute))
                                }
                    } ?: false
                }
            }
        } else {
            sendDataJob?.cancel()
            if(stopwatch==null) {
                _progressUpdateData.value = ProgressUpdateModel(0.0)
            }
        }
    }

}


data class ProgressUpdateModel(
    /**
     * in 0..1 interval (inclusive)
     */
    val progressMultiplier: Double
)

class StopwatchHelper {

    private var startTime: Long? = null

    private var timeOnPaused: Long = 0

    var isActive = false
        private set

    val elapsedMillis: Long
        get() = startTime?.let { System.currentTimeMillis() - it }
                ?: throw StopWatchNotStartedError()

    fun pauseTimer() {
        timeOnPaused = elapsedMillis
        isActive = false
    }

    fun startTimer() {
        startTime = System.currentTimeMillis() - timeOnPaused
        isActive = true
    }

    class StopWatchNotStartedError : Exception("Stopwatch has not started")
}