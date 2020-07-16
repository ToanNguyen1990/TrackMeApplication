package net.toannt.hacore.utils.mqtt

import net.toannt.hacore.utils.localmessage.LocalMessage
import org.eclipse.paho.client.mqttv3.MqttMessage

data class HMMQTTMessage(val topic: String, val mqttMessage: MqttMessage) : LocalMessage() {
}