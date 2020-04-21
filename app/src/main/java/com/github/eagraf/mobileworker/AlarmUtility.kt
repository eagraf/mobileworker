package com.github.eagraf.mobileworker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*


fun setFirstAlarm(context: Context, hour: Int, minute: Int, code: Int) {
    // TODO check wack time code.
    val calendar: Calendar = Calendar.getInstance().apply {
        timeZone = TimeZone.getTimeZone("America/New_York")
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // Make sure the alarm is set forward in time
    if (System.currentTimeMillis() > calendar.timeInMillis) {
        Log.d("Utility", "Calendar " + calendar.timeInMillis.toString())
        Log.d("AlarmUtility", "System " + System.currentTimeMillis().toString())
        calendar.timeInMillis = calendar.timeInMillis + (24 * 60 * 60 * 1000)
    }
    Log.d("Utility", "First alarm" + calendar.timeInMillis.toString())

    setAlarm(calendar, context, code)
}

// Alarm that repeats every 24 hour
// Alarm receiver must reschedule. Timing information passed on as extra in intent
fun setRepeatAlarm(context: Context, milliseconds: Int, code: Int) {
    Log.d("Alarm Utiltiy","Set Repeat Alarm")
    // TODO check wack time code.
    val calendar: Calendar = Calendar.getInstance().apply {
        timeZone = TimeZone.getTimeZone("America/New_York")
        timeInMillis = System.currentTimeMillis() + milliseconds
    }

    setAlarm(calendar, context, code)
}

fun setAlarm(calendar: Calendar, context: Context, code: Int) {
    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("code", code) // So that the same alarm can be identified when it is repeated
    val alarmIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_CANCEL_CURRENT)

    if (alarmIntent == null) {
        Log.d("AlarmUtility", "Alarm is already set")
    } else {
        Log.d("AlarmUtility", "Setting alarm for " + calendar.timeInMillis.toString())
        // Set the alarm
        alarmMgr?.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
    }
}

fun setTestAlarm(context: Context, wait: Int) {
    val calendar: Calendar = Calendar.getInstance().apply {
        timeZone = TimeZone.getTimeZone("America/New_York")
        timeInMillis = System.currentTimeMillis() + wait
    }
    // Make sure the alarm is set forward in time
    if (System.currentTimeMillis() > calendar.timeInMillis) {
        Log.d("Utility", "Calendar " + calendar.timeInMillis.toString())
        Log.d("AlarmUtility", "System " + System.currentTimeMillis().toString())
        calendar.timeInMillis = calendar.timeInMillis + (24 * 60 * 60 * 1000)
    }
    Log.d("Utility", "Test alarm" + calendar.timeInMillis.toString())

    setAlarm(calendar, context, wait)
}