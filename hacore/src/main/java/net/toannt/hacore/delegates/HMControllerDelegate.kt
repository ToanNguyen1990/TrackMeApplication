package net.toannt.hacore.delegates

import android.os.Bundle
import android.view.View

interface HMControllerDelegate {

    fun onContentViewCreated(parentView: View?, saveInstanceState: Bundle?)

}