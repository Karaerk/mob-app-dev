package com.example.onwork.ui.helper

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class CountUpService : Service() {
    private val mHandler = Handler()
    private lateinit var dateCurrent: Date
    private lateinit var dateDiff: Date
    private lateinit var mpref: SharedPreferences
    lateinit var calendar: Calendar
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var strDate: String

    //String str_testing;
    private lateinit var mTimer: Timer
    lateinit var intent: Intent

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mpref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

        Log.v(TAG, mpref.getString("timeAwal", "")!!)

        mTimer = Timer()
        mTimer.scheduleAtFixedRate(
            TimeDisplayTimerTask(),
            5,
            NOTIFY_INTERVAL
        )
        intent = Intent(str_receiver)
    }

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int {
        Log.v(TAG, "service starting")
        return START_STICKY
    }

    internal inner class TimeDisplayTimerTask : TimerTask() {
        override fun run() {
            mHandler.post {
                calendar = Calendar.getInstance()

                simpleDateFormat =
                    SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
                strDate = simpleDateFormat.format(calendar.getTime())
                Log.e("strDate", strDate)
                twoDatesBetweenTime()
            }
        }
    }

    fun twoDatesBetweenTime(): String {
        try {
            dateCurrent = simpleDateFormat.parse(strDate)!!
        } catch (e: Exception) {
        }
        try {
            dateDiff = simpleDateFormat.parse(mpref.getString("timeAwal", "")!!)!!
        } catch (e: Exception) {
        }
        try {
            val longHours = Math.abs(dateCurrent.time - dateDiff.time)
            //long long_hours = diff;
            val diffSeconds2 = longHours / 1000 % 60
            val diffMinutes2 = longHours / (60 * 1000) % 60
            val diffHours2 = longHours / (60 * 60 * 1000)
            val strTesting = String.format("%02d", diffHours2) + ":" + String.format(
                "%02d",
                diffMinutes2
            ) + ":" + String.format("%02d", diffSeconds2)
            mpref.edit().putString("timeAkhir", strTesting).apply()
            intent.putExtra("time", strTesting)
            sendBroadcast(intent)
        } catch (e: Exception) {
            mTimer.cancel()
            mTimer.purge()
        }
        return ""
    }

    override fun onDestroy() {
        super.onDestroy()
        mpref.edit().putBoolean("finish", true).apply()
        mTimer.cancel()
        Log.e("Service finish", "Finish")
    }

    companion object {
        var str_receiver = "com.example.onwork"
        const val NOTIFY_INTERVAL: Long = 1000
        val TAG = CountUpService::class.simpleName
    }
}