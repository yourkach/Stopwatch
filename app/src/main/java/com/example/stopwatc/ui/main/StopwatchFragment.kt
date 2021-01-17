package com.example.stopwatc.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.stopwatc.R
import com.example.stopwatc.logDebug
import kotlinx.android.synthetic.main.fragment_timer.*
import java.text.DecimalFormat

class StopwatchFragment : Fragment(R.layout.fragment_timer) {

    private val viewModel: StopwatchViewModel by viewModels()

    private val doubleDigitsFormat = DecimalFormat("00")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTimeMainText.setOnClickListener {
            viewModel.onTimeTextClick()
        }
        btnReset.setOnClickListener {
            viewModel.onResetClick()
        }
        viewModel.timeSnapshotData.observe(viewLifecycleOwner) { timeTextModel ->
            val secondsText = doubleDigitsFormat.format(timeTextModel.seconds)
            val minutesText = doubleDigitsFormat.format(timeTextModel.minutes)
            val hoursText = timeTextModel.hours.let { if (it > 0) "$it:" else "" }
            val mainText = "$hoursText$minutesText:$secondsText"
            tvTimeMainText.text = mainText
            val millisFormatted = DecimalFormat("00").format(timeTextModel.millis)
            tvTimeMillis.text = millisFormatted
        }
        pbTimeProgress.max = MAX_PROGRESS_VALUE
        viewModel.progressUpdateData.observe(viewLifecycleOwner) { progressUpdateModel ->
            pbTimeProgress.progress =
                (MAX_PROGRESS_VALUE * progressUpdateModel.progressMultiplier).toInt()
        }
    }

    companion object {
        fun newInstance() = StopwatchFragment()

        private const val MAX_PROGRESS_VALUE = 3600

    }
}