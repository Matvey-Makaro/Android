package com.example.tabatatimer.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.tabatatimer.R
import com.example.tabatatimer.TimerActivity
import com.example.tabatatimer.database.AppDatabase
import com.example.tabatatimer.database.models.Interval
import com.example.tabatatimer.database.models.TrainingDao
import java.util.*

const val INTENT_COMMAND = "Command"
const val INTENT_COMMAND_BACK = "Back"
const val INTENT_COMMAND_FORWARD = "Forward"
const val INTENT_COMMAND_RESUME = "Resume"
const val INTENT_COMMAND_PAUSE = "Pause"
const val INTENT_COMMAND_START = "Start"
const val INTENT_COMMAND_EXIT = "Exit"

private const val NOTIFICATION_CHANNEL_GENERAL = "Checking"
private const val CODE_FOREGROUND_SERVICE = 1
const val CODE_BACK_INTENT = 2
const val CODE_FORWARD_INTENT = 3
const val CODE_RESUME_INTENT = 4
const val CODE_PAUSE_INTENT = 5
private const val CODE_CLICK_INTENT = 6

class TimerService: Service()
{

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    private val binder = TimerBinder()
    lateinit var context: TimerActivity
    override fun onBind(intent: Intent?): IBinder = binder

    val timer : Timer = Timer()
    var id : Int = -1

    var dao : TrainingDao? = null
    var intervals : MutableList<Interval>? = null
    var curPos : Int = -1
    var gTime = -1.0
    var isRunning = false

    private lateinit var tmpIntent: Intent

    @SuppressLint("UnspecifiedImmutableFlag")
    fun clickPendingIntent(): PendingIntent? {
        val clickIntent = Intent(
            this,
            TimerActivity::class.java
        )

        clickIntent.putExtra("id", id)

        return androidx.core.app.TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(CODE_CLICK_INTENT, PendingIntent.FLAG_IMMUTABLE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun notifyBind() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        with(
            NotificationChannel(
                TimerActivity.CHANNEL_ID,
                "Timer",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        ) {
            enableLights(false)
            setShowBadge(false)
            enableVibration(false)
            setSound(null, null)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(this)
        }

        with(
            NotificationCompat.Builder(this, TimerActivity.CHANNEL_ID)
        ) {
            setTicker(null)
            setContentTitle(gTime.toInt().toString())
            setContentText(context.resources.getStringArray(R.array.interval_types)[intervals!![curPos].actionType.ordinal + 1])
            setAutoCancel(false)
            setOngoing(true)
            setWhen(System.currentTimeMillis())
            setSmallIcon(R.drawable.ic_launcher_foreground)
            priority = Notification.PRIORITY_MAX
            setContentIntent(clickPendingIntent())
            startForeground(CODE_FOREGROUND_SERVICE, build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val command = intent?.getStringExtra("command")
        if (command == "Exit") {
            stopSelf()
            return START_NOT_STICKY
        }

        if (command == "Stop") {
            isRunning = false
        }

        if (command == "Start") {

            if (dao == null) {
                id = context.trainingId
                dao = AppDatabase.getDatabase(application).trainingDao()
                intervals = dao?.getTrainingsIntervals(id)
                curPos = 0
                val time = intervals!![curPos].intervalTime.toDouble()
                gTime = time
                timer.scheduleAtFixedRate(TimeTask(gTime), 0, 1000)
                isRunning = true
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy()
    {
        timer.cancel()
        super.onDestroy()
    }

    companion object {
        const val TIMER_UPDATED = "timerUpdate"
        const val TIME_EXTRA = "timeExtra"
        const val ID_EXTRA = "idExtra"
        const val ACTION_ID_EXTRA = "actionIdExtra"
        const val NAME_ID_EXTRA = "nameIdExtra"
        const val CURRENT_POSITION_EXTRA = "currentPositionIdExtra"
        const val MAX_POSITION_EXTRA = "maxPositionExtra"
        const val OV_TIME_LEFT_EXTRA = "overallTimeLeftExtra"
    }

    private inner class TimeTask(private var time: Double): TimerTask()
    {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run()
        {
            if(!isRunning)
            {
                notifyBind()
                return
            }

            time = gTime
            time--
            val intent = Intent(TIMER_UPDATED)

            if(time <= 0)
            {
                curPos++
                val soundTimer = Timer()
                if(curPos < intervals?.size!!)
                    soundTimer.scheduleAtFixedRate(SoundTask(1,  RingtoneManager.getRingtone(applicationContext, RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_RINGTONE))), 0, 100)
                else
                    soundTimer.scheduleAtFixedRate(SoundTask(1, RingtoneManager.getRingtone(applicationContext, RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_ALARM))), 0, 2000)

                time = -1.0
                if(curPos < intervals?.size!!)
                    time = intervals!![curPos].intervalTime.toDouble()

                var nameId: Int = 5
                if(curPos < intervals?.size!!)
                    nameId = intervals!![curPos].actionType.ordinal

                intent.putExtra(ACTION_ID_EXTRA, 2)
                intent.putExtra(NAME_ID_EXTRA, nameId)
            }
            else
            {
                intent.putExtra(ACTION_ID_EXTRA, 1)
                intent.putExtra(NAME_ID_EXTRA, intervals!![curPos].actionType.ordinal)
            }

            intent.putExtra(TIME_EXTRA, time)
            gTime = time
            if(curPos < intervals?.size!!)
                notifyBind()

            intent.putExtra(CURRENT_POSITION_EXTRA, curPos)
            intent.putExtra(MAX_POSITION_EXTRA, intervals!!.size)
            intent.putExtra(OV_TIME_LEFT_EXTRA, countOvLeftTime())
            sendBroadcast(intent)
        }

    }

    private inner class SoundTask(private var tics: Int, private var rng: Ringtone) : TimerTask()
    {
        private var cur = 0

        override fun run()
        {
            if(cur == 0)
                rng.play()

            if(cur == tics)
                rng.stop()

            cur++
        }
    }

    private fun countOvLeftTime(): String {
        var secs :Int = gTime.toInt()
        val l : Int = curPos + 1
        val r : Int = intervals!!.size - 1
        for (i in l..r) {
            secs += intervals!![i].intervalTime
        }

        return String.format("%02d:%02d", secs / 60, secs % 60)
    }
}