package net.nvtoan.trackme.app.ui.splashscreen

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.nvtoan.trackme.R
import net.toannt.hacore.ui.fragment.HMFragment
import java.util.concurrent.TimeUnit

class SplashFragment: HMFragment() {

    override fun getLayoutResId(savedInstanceState: Bundle?): Int? {
        return R.layout.fragment_splash
    }

    override fun initUI() {
        compositeDisposable.add(Observable.timer(100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                findNavController().navigate(R.id.goToHomeFragment)
            })
    }
}