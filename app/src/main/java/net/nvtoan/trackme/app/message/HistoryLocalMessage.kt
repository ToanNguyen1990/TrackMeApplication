package net.nvtoan.trackme.app.message

import net.toannt.hacore.utils.localmessage.LocalMessage

data class HistoryLocalMessage(var localId: Long, var type: HistoryLocalActionTypeEnum): LocalMessage()

enum class HistoryLocalActionTypeEnum() {
    START,
    END
}