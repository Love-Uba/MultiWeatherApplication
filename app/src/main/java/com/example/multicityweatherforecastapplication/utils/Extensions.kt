package com.example.multicityweatherforecastapplication.utils

import android.view.View
import com.example.multicityweatherforecastapplication.utils.UtilConstants.SERVER_DATE_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun String.getFormattedDateTime(displayFormat: String): String {

    val dateFormatIn = SimpleDateFormat(SERVER_DATE_FORMAT, Locale.getDefault())
    return try {
        val dateIn = dateFormatIn.parse(this)!!
        val offset = TimeZone.getDefault().getOffset(dateIn.time)
        val cal = Calendar.getInstance().apply {
            time = dateIn
            add(Calendar.MILLISECOND, offset)
        }
        val dateFormatOut = SimpleDateFormat(displayFormat, Locale.getDefault())
        dateFormatOut.format(cal.time)
    }catch (exception: ParseException) {
        SimpleDateFormat(displayFormat, Locale.getDefault()).format(Date())
    } catch (exception: Exception) {
        SimpleDateFormat(displayFormat, Locale.getDefault()).format(Date())
    }
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter
            = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

private fun getDateTime(s: String): String? {
    try {
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val netDate = Date(s.toLong() * 1000)
        return sdf.format(netDate)
    } catch (e: Exception) {
        return e.toString()
    }
}

fun showcurrentTime(): Date {
    return Calendar.getInstance().time
}

fun convertLongToTime(time: Long): String {
    val date = Date(time * 1000)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(date)
}



fun currentTimeToLong(): Long {
    return System.currentTimeMillis()
}

fun convertDateToLong(date: String): Long {
    val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return df.parse(date)?.time ?: 0
}

fun View.gone(){
    this.visibility = View.GONE
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.INVISIBLE
}
