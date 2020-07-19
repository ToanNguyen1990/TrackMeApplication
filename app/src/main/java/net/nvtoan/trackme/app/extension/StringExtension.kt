package net.nvtoan.trackme.app.extension

fun Double.convertToString(): String {
    return String.format("%.1f", this)
}

fun Float.convertToString(): String {
    return String.format("%.2f", this)
}

fun Int.convertToTimeString(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    val minuteStr = getTimeString(minutes)
    val secondStr =  getTimeString(seconds)
    return when {
        hours > 0 -> {
            val hourStr = getTimeString(hours)
            "$hourStr:$minuteStr:$secondStr"
        }
        else -> "$minuteStr:$secondStr"
    }
}

private fun getTimeString(time: Int): String {
    return when {
        time < 10 -> "0$time"
        else -> "$time"
    }
}