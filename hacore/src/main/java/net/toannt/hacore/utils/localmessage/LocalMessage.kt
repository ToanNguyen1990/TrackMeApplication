package net.toannt.hacore.utils.localmessage

import java.io.Serializable

open class LocalMessage : Serializable {

    protected var indentifier = hashCode()

}