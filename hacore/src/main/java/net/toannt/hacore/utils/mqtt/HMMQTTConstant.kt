package net.toannt.hacore.utils.mqtt

object HMMQTTConstant {

    val TCP = "tcp://"

    val SSL = "ssl://"

    val UTF_8 = "UTF-8"

    val F_SLASH = "/"

    val DASH = "-"

    val HS = "hs"

    val CS = "cs"

    val HA = "ha"

    val PA = "pa"

    val QOS1 = 1

    val QOS2 = 2

    val CONNECTION_STATUS = "status"

    val EVENT = "event"

    fun getHaStatusPrefix(): String {
        return "$HA$F_SLASH$CONNECTION_STATUS"
    }


    fun getHsStatusPrefix(): String {
        return "$HS$F_SLASH$CONNECTION_STATUS"
    }

    val CONNECTED = 1
    val DISCONNECTED = 2
    val CONNECTION_LOST = 3

    val retained_true = true
    val retained_false = false


    val hs_event_prefix = "hs/event/"

    val ha_event_prefix = "ha/event/"
    //cloud topics
    val cs_event_prefix = "cs/event/"

}