package net.toannt.hacore.entities.request

class BrokerRequest : HARestRequest() {

    var brokerIP: String = "192.168.123.1"
        set(value) {
            field = value; notifyRequireValidation()
        }

    var brokerPort: String = "8883"
        set(value) {
            field = value; notifyRequireValidation()
        }

    var userName: String = "homa"
        set(value) {
            field = value; notifyRequireValidation()
        }

    var password: String = "64caccd476f511e9"
        set(value) {
            field = value; notifyRequireValidation()
        }

    fun setBrokerIP(text: CharSequence?) {
        this@BrokerRequest.brokerIP = text.toString()
    }

    fun setBrokerPort(text: CharSequence?) {
        this@BrokerRequest.brokerPort = text.toString()
    }

    fun setUsername(text: CharSequence?) {
        this@BrokerRequest.userName = text.toString()
    }

    fun setPassword(text: CharSequence?) {
        this@BrokerRequest.password = text.toString()
    }

    override fun validates(): Boolean {
        if (brokerIP.isBlank() || brokerPort.isNullOrBlank() || userName.isNullOrBlank() || password.isNullOrBlank()) {
            return false
        }

        return true
    }
}