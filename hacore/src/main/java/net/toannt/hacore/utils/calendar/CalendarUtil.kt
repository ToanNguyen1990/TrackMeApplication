package net.toannt.hacore.utils.calendar

import android.content.Context
import com.homa.app.demo.hacore.R
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

const val ONE_DAY = (1000 * 3600 * 24).toLong()
const val HOUR_MIN_FORMAT_STRING = "hh:mm"
const val SHORT_DATE_FORMAT_STRING = "EEE MMM d"
const val HOUR_MIN_AM_PM_FORMAT_STRING = "hh:mm a"
const val DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm"
const val DATE_TIME_HOUR_MIN_FORMAT = "HH:mm"
const val START_TIME_ENERGY_BILLING = "yyyy-MM-dd"
const val DATE_TIME_EVENT_FORMAT = "EEEE, MMMM d, yyyy"

fun Calendar.hasSameDay(calendar: Calendar): Boolean {
    return this.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
            && this.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
            && this.get(Calendar.DATE) == calendar.get(Calendar.DATE)
}

fun Calendar.getStringBy(format: String = HOUR_MIN_FORMAT_STRING): String {
    val date = this.time
    val dateFormat = SimpleDateFormat(format)
    return dateFormat.format(date)
}

fun Date.getStringBy(format: String = HOUR_MIN_AM_PM_FORMAT_STRING): String {
    val dateFormat = SimpleDateFormat(format)
    return dateFormat.format(this)
}

object CalendarUtil {

    fun getPreviousWeekCalendars(calendar: Calendar, weekStart: Int): List<Calendar> {
        var currentStartWeekDay = getStartDayWeek(calendar, weekStart)
        currentStartWeekDay.add(Calendar.DATE, -7)
        return getWeekCalendars(currentStartWeekDay, weekStart)
    }

    fun getNextWeekCalendars(calendar: Calendar, weekStart: Int): List<Calendar> {
        var currentStartWeekDay = getStartDayWeek(calendar, weekStart)
        currentStartWeekDay.add(Calendar.DATE, 7)
        return getWeekCalendars(currentStartWeekDay, weekStart)
    }

    fun getWeekCalendars(
        calendar: Calendar = Calendar.getInstance(),
        weekStart: Int
    ): List<Calendar> {
        return initCalendarForWeekView(getStartDayWeek(calendar, weekStart), weekStart)
    }

    fun getStartDayWeek(calendar: Calendar, weekStart: Int): Calendar {
        var curTime = calendar.timeInMillis

        val date = Calendar.getInstance()
        date.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE)
        )

        val week = date.get(Calendar.DAY_OF_WEEK)
        val startDiff: Int = when (weekStart) {
            1 -> week - 1
            2 -> if (week == 1) 6 else week - weekStart
            else -> if (week == 7) 0 else week
        }

        curTime -= startDiff * ONE_DAY
        val minCalendar = Calendar.getInstance()
        minCalendar.timeInMillis = curTime

        val startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.YEAR, minCalendar.get(Calendar.YEAR))
        startCalendar.set(Calendar.MONTH, minCalendar.get(Calendar.MONTH))
        startCalendar.set(Calendar.DATE, minCalendar.get(Calendar.DAY_OF_MONTH))
        return startCalendar
    }

    private fun initCalendarForWeekView(calendar: Calendar, weekStart: Int): List<Calendar> {
        val date = Calendar.getInstance()
        date.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE)
        )
        val curDateMills = date.timeInMillis

        val weekEndDiff = getWeekViewEndDiff(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE),
            weekStart
        )
        val mItems = ArrayList<Calendar>()

        date.timeInMillis = curDateMills
        date.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        mItems.add(calendar)

        for (i in 1..weekEndDiff) {
            date.timeInMillis = curDateMills + i * ONE_DAY
            val calendarDate = Calendar.getInstance()
            calendarDate.set(Calendar.YEAR, date.get(Calendar.YEAR))
            calendarDate.set(Calendar.MONTH, date.get(Calendar.MONTH))
            calendarDate.set(Calendar.DATE, date.get(Calendar.DAY_OF_MONTH))
            calendarDate.set(Calendar.HOUR_OF_DAY, 0)
            calendarDate.set(Calendar.MINUTE, 0)
            calendarDate.set(Calendar.SECOND, 0)
            calendarDate.set(Calendar.MILLISECOND, 0)
            mItems.add(calendarDate)
        }
        return mItems
    }

    private fun getWeekViewEndDiff(year: Int, month: Int, day: Int, weekStart: Int): Int {
        val date = Calendar.getInstance()
        date.set(year, month, day)
        val week = date.get(Calendar.DAY_OF_WEEK)
        if (weekStart == 1) {
            return 7 - week
        }
        if (weekStart == 2) {
            return if (week == 1) 0 else 7 - week + 1
        }
        return if (week == 7) 6 else 7 - week - 1
    }

    fun getTodayCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    fun makeShortTimeString(context: Context, secs: Long): String {
        var seconds = secs
        val hours: Long
        val minutes: Long

        hours = seconds / 3600
        seconds %= 3600
        minutes = seconds / 60
        seconds %= 60

        val formatString = if (hours == 0L) {
            R.string.durationformatshort
        } else {
            R.string.durationformatlong
        }
        val durationFormat = context.resources.getString(formatString)
        return String.format(durationFormat, hours, minutes, seconds)
    }

    fun makeShortTimeString(calendar: Calendar): String {
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        return makeShortTimeString(minutes, hours)
    }

    fun makeShortTimeString(minutes: Int, hours: Int): String {
        val hourString = when {
            hours < 10 -> {
                "0${hours - 1}"
            }

            else -> {
                "$hours"
            }
        }

        val minutes = when {
            minutes < 10 -> {
                "0${minutes}"
            }

            else -> {
                "$minutes"
            }
        }

        return "$hourString:$minutes"
    }


    fun getFirstTimeOfDay(year: Int, month: Int, date: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, date, 0, 0, 0)
        System.out.println(calendar.time.toString())
        return calendar.timeInMillis.div(1000)
    }

    fun getFirstTimeOfWeek(year: Int, month: Int, date: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, date, 0, 0, 0)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return calendar.timeInMillis.div(1000)
    }

    fun getFirstTimeOfMonth(year: Int, month: Int, date: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, date, 0, 0, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        System.out.println(calendar.time.toString())
        return calendar.timeInMillis.div(1000)
    }

    fun getFirstTimeOfYear(year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, 0, 1, 0, 0, 0)
        System.out.println(calendar.time.toString())
        return calendar.timeInMillis.div(1000)
    }

    fun convertStringToTimeStamp(
        dateStr: String,
        format: String = START_TIME_ENERGY_BILLING
    ): Long {
        val df = SimpleDateFormat(format, Locale.getDefault())
        try {
            val today = df.parse(dateStr)
            return today.time
        } catch (e: Exception) {
            Timber.e("convertStringToTimeStamp : $e")
            e.printStackTrace()
        }
        return 0
    }

    fun shouldUseHomeSettingDate(startInMillis: Long, homeSettingInMillis: Long): Boolean {
        val startCal = Calendar.getInstance().apply { timeInMillis = startInMillis }
        val startMonth = startCal.get(Calendar.MONTH)
        val startDay = startCal.get(Calendar.DAY_OF_MONTH)

        val homeSettingCal = Calendar.getInstance().apply { timeInMillis = homeSettingInMillis }
        val homeSettingYear = homeSettingCal.get(Calendar.YEAR)
        val homeSettingMonth = homeSettingCal.get(Calendar.MONTH)
        val homeSettingDay = homeSettingCal.get(Calendar.DAY_OF_MONTH)

        val currentCal = Calendar.getInstance()
        val currentYear = currentCal.get(Calendar.YEAR)
        val currentMonth = currentCal.get(Calendar.MONTH)
        val currentDay = currentCal.get(Calendar.DAY_OF_MONTH)

        return when {
            // 1<= startTime <= homeSetting <= current <= startTime <= endOfMonth
            homeSettingMonth == currentMonth && homeSettingDay <= currentDay && (startDay < homeSettingDay || startDay > currentDay) -> true

            // TODO: Check by year: Dec vs Jan
            // 1<= startTime <= homeSetting <endOfMonth && 1<= current < startTime < homeSetting
            homeSettingMonth == currentMonth - 1 && homeSettingDay > currentDay && (startDay in (currentDay + 1) until homeSettingDay) -> true
            else -> false
        }
    }

    fun getCurrentMonth(millis: Long): Long {
        val calendarSelected = Calendar.getInstance()
        calendarSelected.timeInMillis = millis
        val calendar = getTodayCalendar()
        calendar.set(Calendar.DAY_OF_MONTH, calendarSelected.get(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }
}
