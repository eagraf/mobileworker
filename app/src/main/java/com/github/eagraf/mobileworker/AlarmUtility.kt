package com.github.eagraf.mobileworker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*


fun setFirstAlarm(context: Context, hour: Int, minute: Int) {
    // TODO check wack time code.
    val calendar: Calendar = Calendar.getInstance().apply {
        timeZone = TimeZone.getTimeZone("America/New_York")
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }

    // Make sure the alarm is set forward in time
    if (System.currentTimeMillis() > calendar.timeInMillis) {
        calendar.timeInMillis = calendar.timeInMillis + (24 * 60 * 60 * 1000)
    }

    setAlarm(calendar, context)
}

// Alarm that repeats every 24 hour
// Alarm receiver must reschedule. Timing information passed on as extra in intent
fun setRepeatAlarm(context: Context) {
    // TODO check wack time code.
    val calendar: Calendar = Calendar.getInstance().apply {
        timeZone = TimeZone.getTimeZone("America/New_York")
        timeInMillis = System.currentTimeMillis() + (24 * 60 * 60 * 1000)
    }

    setAlarm(calendar, context)
}

fun setAlarm(calendar: Calendar, context: Context) {
    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
        PendingIntent.getBroadcast(context, 2216, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    // Set the alarm
    alarmMgr?.setExact(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        alarmIntent
    )
}