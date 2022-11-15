package com.example.tabatatimer

import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabatatimer.adapters.TimerAdapter
import com.example.tabatatimer.database.AppDatabase
import com.example.tabatatimer.database.models.Interval
import com.example.tabatatimer.database.models.Training
import com.example.tabatatimer.database.models.TrainingActionType
import com.example.tabatatimer.database.models.TrainingDao
import com.example.tabatatimer.databinding.ActivityTimerBinding
import com.example.tabatatimer.services.TimerService
import kotlin.math.roundToInt

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
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

    lateinit var timerService: TimerService
    var isBound = false

    private val connection = object: ServiceConnection {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onServiceConnected(className: ComponentName, service: IBinder)
        {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            timerService.context = this@TimerActivity
            isBound = true

            if(timerService.isRunning == false)
            {
                this@TimerActivity.foregroundStartService("Start")
                if(timerService.id == -1)
                {
                    timerService.id = trainingId
                }
                else
                {
                    trainingId = timerService.id
                    curPos = timerService.curPos
                }

                dao = AppDatabase.getDatabase(application).trainingDao()
                intervals = dao?.getTrainingsIntervals(trainingId)
                timerService.isRunning = true
                registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

                binding.nextIntervalButton.setOnClickListener { nextTimerInterval() }
                binding.prevIntervalButton.setOnClickListener { prevTimerInterval() }
                binding.stopStartTimeButton.setOnClickListener { stopStartTimer() }

                binding.intervalsInTimerRecyclerView.layoutManager = LinearLayoutManager(this@TimerActivity)
                binding.intervalsInTimerRecyclerView.adapter = TimerAdapter(this@TimerActivity, trainingId)

                binding.stopStartTimeButton.icon = AppCompatResources.getDrawable(this@TimerActivity, R.drawable.ic_baseline_pause_24)
                timerStarted = true
            }
        }

        override fun onServiceDisconnected(name: ComponentName?)
        {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                trainingId = getInt("id")
            }
        }

        updateTheme()
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
        isBound = true
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    private fun stopStartTimer() {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        timerService.isRunning = true

        binding.stopStartTimeButton.icon = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_pause_24)
        timerStarted = true
    }

    private fun stopTimer() {
        timerService.isRunning = false

        binding.stopStartTimeButton.icon = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_play_arrow_24)
        timerStarted = false
    }

    private fun prevTimerInterval() {
        if (curPos != 0) {
            curPos--
            timerService.curPos--
            timerService.gTime = intervals!![curPos].intervalTime.toDouble()

            binding.intervalsInTimerRecyclerView.adapter?.notifyItemChanged(curPos)
            binding.intervalsInTimerRecyclerView.adapter?.notifyItemChanged(curPos + 1)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextTimerInterval() {
        if (curPos < mxPos) {


            curPos++

            if (curPos == mxPos) {
                timerService.isRunning = false
                this@TimerActivity.foregroundStartService("Exit")
//                timerService.stopForeground(Service.STOP_FOREGROUND_REMOVE)


                val setIntent = Intent (this@TimerActivity, MainActivity::class.java)

                this@TimerActivity.startActivity(setIntent)

                finish()

            }
            else {

                timerService.curPos++
                timerService.gTime = intervals!![curPos].intervalTime.toDouble()
                binding.intervalsInTimerRecyclerView.adapter?.notifyItemChanged(curPos)
                binding.intervalsInTimerRecyclerView.adapter?.notifyItemChanged(curPos - 1)
            }
        }
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {

            val id : Int = intent!!.getIntExtra(TimerService.ACTION_ID_EXTRA, 0)
            curPos = intent.getIntExtra(TimerService.CURRENT_POSITION_EXTRA, 0)
            val nameId : Int = intent.getIntExtra(TimerService.NAME_ID_EXTRA, 0)
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            mxPos = intent.getIntExtra(TimerService.MAX_POSITION_EXTRA, 1)
            val tLeft = intent.getStringExtra(TimerService.OV_TIME_LEFT_EXTRA)

            val name : String = if (nameId < 5) { TrainingActionType.values()[nameId].name } else {"Финишь"}

            if (id == 1) {
                binding.intervalTimeLeftTextView.text = getTimeStringFromDouble(time)
                binding.intervalsCountTextView.text = String.format("%02d/%02d", curPos + 1, mxPos)
                binding.overallLeftTimeTextView.text = tLeft
                binding.currentIntervalNameTextView.text = name
            }
            else if (id == 2) {

                if (curPos == mxPos) {

                    timerService.isRunning = false
                    this@TimerActivity.foregroundStartService("Exit")
//                    timerService.stopForeground(Service.STOP_FOREGROUND_REMOVE)


                    val setIntent = Intent (this@TimerActivity, MainActivity::class.java)

                    this@TimerActivity.startActivity(setIntent)

                    finish()
                }
                else {

                    binding.intervalsInTimerRecyclerView.adapter?.notifyItemChanged(curPos)
                    binding.intervalsInTimerRecyclerView.adapter?.notifyItemChanged(curPos - 1)

                    binding.intervalTimeLeftTextView.text = getTimeStringFromDouble(time)
                    binding.intervalsCountTextView.text = String.format("%02d/%02d", curPos + 1, mxPos)
                    binding.overallLeftTimeTextView.text = tLeft
                    binding.currentIntervalNameTextView.text = name
                }
            }

        }
    }

    fun pressBtn(pos : Int) {

        if (curPos == pos) return

        binding.intervalsInTimerRecyclerView.adapter?.notifyItemChanged(curPos)
        binding.intervalsInTimerRecyclerView.adapter?.notifyItemChanged(pos)

        curPos = pos
        timerService.curPos = pos
        timerService.gTime = intervals!![curPos].intervalTime.toDouble()
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {

        timerService.isRunning = false
        this@TimerActivity.foregroundStartService("Exit")
//        timerService.stopForeground(Service.STOP_FOREGROUND_REMOVE)

        val setIntent = Intent (this, MainActivity::class.java)

        this.startActivity(setIntent)

        finish()
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String
    {
        if(hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)

        if(minutes > 0)
            return String.format("%02d:%02d", minutes, seconds)

        return String.format("%02d", seconds)
    }

    fun updateTheme() {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val theme : Boolean = sharedPreference.getBoolean("theme_switch_preference", false)
        val bcg : ConstraintLayout = findViewById(R.id.timerConstraintLayout)
        val font : String? = sharedPreference.getString("font_preference", "-1")


        if (font == "1")
            binding.currentIntervalNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.currentIntervalNameTextView.textSize * (0.5).toFloat())

        if (font == "3")
            binding.currentIntervalNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.currentIntervalNameTextView.textSize * (1.5).toFloat())

        binding.prevIntervalButton.setBackgroundColor(Color.parseColor("#5e5e5e"))

        var cur_color: Int = Color.parseColor("#5e5e5e")
        if(theme)
            cur_color = Color.parseColor("#5e5e5e")

        else cur_color = Color.parseColor("#FF6200EE")

        bcg.setBackgroundColor(cur_color)
        val col : ColorDrawable = ColorDrawable(cur_color)
        getSupportActionBar()?.setBackgroundDrawable(col)

        binding.prevIntervalButton.setBackgroundColor(cur_color)
        binding.nextIntervalButton.setBackgroundColor(cur_color)
        binding.stopStartTimeButton.setBackgroundColor(cur_color)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.foregroundStartService(command: String) {
    val intent : Intent = Intent(this, TimerService::class.java)

    intent.putExtra("command", command)
    this.startForegroundService(intent)
}