package net.toannt.hacore.utils.mqtt

class HMTopicWillMessage(var topic: String, var payload: ByteArray, var qos: Int, var retained: Boolean)