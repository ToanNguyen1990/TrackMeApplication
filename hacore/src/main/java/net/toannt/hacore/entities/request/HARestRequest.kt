package net.toannt.hacore.entities.request

import android.util.Patterns
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.homa.app.demo.hacore.BR
import java.util.regex.Matcher
import java.util.regex.Pattern

abstract class HARestRequest : BaseObservable() {

    var isRequestValid: Boolean = false
        @Bindable get() {
            return validates()
        }

    abstract fun validates(): Boolean

    protected fun notifyRequireValidation() {
        this@HARestRequest.notifyPropertyChanged(BR.requestValid)
    }

    protected fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    protected fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(passwordPattern)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }
}