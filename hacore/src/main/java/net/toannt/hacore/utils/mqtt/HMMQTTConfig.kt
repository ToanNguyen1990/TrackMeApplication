package net.toannt.hacore.utils.mqtt

import android.content.Context
import androidx.annotation.RawRes
import net.toannt.hacore.api.HMSocketFactory
import org.eclipse.paho.client.mqttv3.MqttConnectOptions

data class HMMQTTConfig(
    var ipAddress: String = "",
    var port: Int = 0,
    var clientId: String = "",
    var requirePorts: List<Int>? = emptyList(),
    var userName: String = "",
    var password: String = "",
    @RawRes var certificateResId: Int = 0,
    var autoReconnect: Boolean = true,
    var cleanSession: Boolean = false,
    var isWillTopic: Boolean = false,
    var willTopicMessage: HMTopicWillMessage? = null
) {

    fun updateClientId(clientId: String): HMMQTTConfig {
        this@HMMQTTConfig.clientId = clientId
        return this@HMMQTTConfig
    }

    fun updateUserName(userName: String): HMMQTTConfig {
        this@HMMQTTConfig.userName = userName
        return this@HMMQTTConfig
    }

    fun updatePassword(password: String): HMMQTTConfig {
        this@HMMQTTConfig.password = password
        return this@HMMQTTConfig
    }

    fun updateIPAddress(ipAddress: String): HMMQTTConfig {
        this@HMMQTTConfig.ipAddress = ipAddress
        return this@HMMQTTConfig
    }

    fun updatePort(port: Int): HMMQTTConfig {
        this@HMMQTTConfig.port = port
        return this@HMMQTTConfig
    }

    fun updateRequirePorts(requirePorts: List<Int>): HMMQTTConfig {
        this@HMMQTTConfig.requirePorts = requirePorts
        return this@HMMQTTConfig
    }

    fun updateCertificateRawResId(certificateResId: Int): HMMQTTConfig {
        this@HMMQTTConfig.certificateResId = certificateResId
        return this@HMMQTTConfig
    }

    fun updateAutoReconnect(isReconnect: Boolean): HMMQTTConfig {
        this@HMMQTTConfig.autoReconnect = isReconnect
        return this@HMMQTTConfig
    }

    fun updateCleanSession(isCleanSession: Boolean): HMMQTTConfig {
        this@HMMQTTConfig.cleanSession = isCleanSession
        return this@HMMQTTConfig
    }

    fun updateIsWillTopic(isWillTopic: Boolean): HMMQTTConfig {
        this@HMMQTTConfig.isWillTopic = isWillTopic
        return this@HMMQTTConfig
    }

    fun updateWillTopicMessage(message: HMTopicWillMessage): HMMQTTConfig {
        this@HMMQTTConfig.willTopicMessage = message
        return this@HMMQTTConfig
    }

    fun getConnectOptions(context: Context?): MqttConnectOptions {
        val connectOptions = MqttConnectOptions()
        connectOptions.userName = userName
        connectOptions.password = password.toCharArray()
        connectOptions.isAutomaticReconnect = autoReconnect
        connectOptions.isCleanSession = cleanSession

        if (isWillTopic && willTopicMessage != null) {
            connectOptions.setWill(
                willTopicMessage!!.topic,
                willTopicMessage!!.payload,
                willTopicMessage!!.qos,
                willTopicMessage!!.retained
            )
        }

        if (isSecuredPort() || context != null) {
            connectOptions.socketFactory = HMSocketFactory.getSocketFactory(context!!, certificateResId)
        }

        return connectOptions
    }

    fun serverUrl(): String {
        val prefix = if (isSecuredPort()) HMMQTTConstant.SSL else HMMQTTConstant.TCP
        return "$prefix$ipAddress:$port"
    }


    private fun isSecuredPort(): Boolean {
        return requirePorts?.contains(port) ?: false
    }
}