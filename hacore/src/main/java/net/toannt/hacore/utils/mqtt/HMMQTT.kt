package net.toannt.hacore.utils.mqtt

import android.content.Context
import net.toannt.hacore.utils.localmessage.LocalMessageController
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import timber.log.Timber


class HMMQTT(var context: Context) {

    companion object {
        private var HMMQTT: HMMQTT? = null
        var MQTTConfig = HMMQTTConfig()
        private var MQTTAndroidClient: MqttAndroidClient? = null

        fun initMQTT(context: Context) {
            if (HMMQTT != null) {
                return
            }

            HMMQTT = HMMQTT(context)
        }

        fun getDefault(): HMMQTT {
            return HMMQTT ?: throw RuntimeException("you must init HMMQTT!")
        }
    }

    fun connect(): Observable<Boolean> {
        var instance = PublishSubject.create<Boolean>()
        if (MQTTAndroidClient != null && MQTTAndroidClient!!.isConnected) {
            instance.onNext(true)
            return instance
        }

        instance.onNext(false)
        MQTTAndroidClient = MqttAndroidClient(context, MQTTConfig.serverUrl(), MQTTConfig.clientId, MemoryPersistence())
        setCallback()
        MQTTAndroidClient?.connect(MQTTConfig.getConnectOptions(context = context), null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                val disconnectedBufferOptions = DisconnectedBufferOptions()
                disconnectedBufferOptions.isBufferEnabled = true
                disconnectedBufferOptions.bufferSize = 100
                disconnectedBufferOptions.isPersistBuffer = false
                disconnectedBufferOptions.isDeleteOldestMessages = false
                MQTTAndroidClient!!.setBufferOpts(disconnectedBufferOptions)
                instance.onNext(true)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                instance.onError(exception!!)
            }
        })

        return instance
    }

    private fun setCallback() {
        MQTTAndroidClient?.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                if (reconnect) {
                    Timber.i("HMMQTT Reconnected to : $serverURI")
                    // Because Clean Session is true, we need to re-subscribe
                    //subscribeToTopic()
                } else {
                    Timber.i("HMMQTT Connected to: $serverURI")
                }
            }

            override fun connectionLost(cause: Throwable) {
                Timber.i("HMMQTT The connection of HMMQTT was lost.")
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                Timber.i("HMMQTT messageArrived: ${topic} , ${message}")
                var customMessage = HMMQTTMessage(topic = topic, mqttMessage = message)
                LocalMessageController.getInstance().processLocalMessage(customMessage)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                Timber.i("HMMQTT deliveryComplete: " + token)
            }
        })
    }

    fun isConnected(): Boolean {
        return MQTTAndroidClient?.isConnected ?: false
    }

    fun getClientId(): String? {
        return MQTTAndroidClient?.clientId
    }

    fun subscribeTopic(topic: String) {
        MQTTAndroidClient?.subscribe(
            topic, HMMQTTConstant.QOS1, null,
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Timber.i("HMMQTT subscribe topic: ${topic} success: token ${asyncActionToken?.toString()}")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Timber.i("HMMQTT subscribe topic: ${topic} failure")
                }
            })
    }

    fun publishMessage(message: HMMQTTMessage) {
        MQTTAndroidClient?.publish(message.topic, message.mqttMessage)
    }

    fun publishMessage(topic: String, message: MqttMessage) {
        MQTTAndroidClient?.publish(topic, message)
    }

}