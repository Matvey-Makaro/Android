package com.example.tabatatimer

import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabatatimer.database.models.Interval
import com.example.tabatatimer.database.models.TrainingDao

class TimerActivity : AppCompatActivity() {
    private var timerStarted = false

    var time = 0.0
    var trainingId: Int = -1
    var mxPos: Int = 1
    var curPos: Int = 0
    var dao: TrainingDao? = null
    var intervals: MutableList<Interval>? = null

    companion object{
        const val CHANNEL_ID: String = "123123"
        const val NOTIFICATION_ID: Int = 7894613
    }

    // TODO: Починить lateinit var timerService: TimerService
    var isBound = false

    // TODO: Починить private val connection = object: ServiceConnection {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
    }
}