package net.toannt.hacore

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import net.toannt.hacore.ui.fragment.HMFragment
import timber.log.Timber

class HMFragmentHelper(private val fragmentManager: FragmentManager, @IdRes private val containerViewId: Int) {

    fun <T : HMFragment> initRootFragment(fragment: T, allowStateLoss: Boolean = false) {

        if (this@HMFragmentHelper.fragmentManager.findFragmentById(this@HMFragmentHelper.containerViewId) != null) {
            return
        }

        val fragmentTransaction = this@HMFragmentHelper.getFragmentTransaction(false)
            .replace(this@HMFragmentHelper.containerViewId, fragment, fragment::class.java.simpleName)

        if (!fragmentManager.isStateSaved) {
            fragmentTransaction.commitNow()
        } else if (allowStateLoss){
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    fun <T : Fragment> pushFragment(
        fragment: T,
        replaceRootFragment: Boolean = false,
        addToBackStack: Boolean = false,
        isCommitNow: Boolean = false) {
        if (this@HMFragmentHelper.fragmentManager.findFragmentById(this@HMFragmentHelper.containerViewId)?.tag.equals(fragment::class.java.simpleName)) {
            Timber.i("fragment ${fragment.javaClass.name}  : commit false")
            return
        }

        if (replaceRootFragment) {
            this@HMFragmentHelper.popToRoot()
        }

        val fragmentTransaction = this@HMFragmentHelper.getFragmentTransaction(!replaceRootFragment)
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment::class.java.simpleName)
        }

        fragmentTransaction.replace(this.containerViewId, fragment, fragment::class.java.simpleName)

        when {
            fragmentManager.isStateSaved -> when {
                isCommitNow -> fragmentTransaction.commitNowAllowingStateLoss()
                else -> fragmentTransaction.commitAllowingStateLoss()
            }
            isCommitNow -> fragmentTransaction.commitNow()
            else -> fragmentTransaction.commit()
        }

    }

    fun <T : HMFragment> pushFragment(fragment: T, sharedElementView: View, replaceRootFragment: Boolean = false) {

        var previousFragment =
            this@HMFragmentHelper.fragmentManager.findFragmentById(this@HMFragmentHelper.containerViewId)

        if (previousFragment?.tag.equals(fragment::class.java.simpleName)) {
            return
        }

        if (replaceRootFragment) {
            this@HMFragmentHelper.popToRoot()
        }

        var exitFade = Fade()
        exitFade.duration = 300
        previousFragment!!.exitTransition = exitFade


        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(sharedElementView.context).inflateTransition(android.R.transition.move))
        enterTransitionSet.duration = 300
        enterTransitionSet.startDelay = 1000
        fragment.sharedElementEnterTransition = enterTransitionSet

        val enterFade = Fade()
        enterFade.startDelay = 1300
        enterFade.duration = 300
        fragment.enterTransition = enterFade

        val fragmentTransaction = this@HMFragmentHelper.getFragmentTransaction(!replaceRootFragment)
            .addSharedElement(sharedElementView, sharedElementView.transitionName)
            .replace(
                this.containerViewId,
                fragment,
                fragment::class.java.simpleName
            )

        fragmentTransaction.commitAllowingStateLoss()
    }

    fun popFragment(): Boolean {
        if (this@HMFragmentHelper.fragmentManager.backStackEntryCount > 0) {
            return this@HMFragmentHelper.fragmentManager.popBackStackImmediate()
        }

        return false
    }

    fun <T : HMFragment> isShowing(fragmentClass: Class<T>): Boolean {
        return this@HMFragmentHelper.fragmentManager.findFragmentById(this@HMFragmentHelper.containerViewId)
            ?.tag.equals(fragmentClass.simpleName)
    }

    fun <T : HMFragment> getCurrentFragment(): T? {
        return this@HMFragmentHelper.fragmentManager.findFragmentById(this@HMFragmentHelper.containerViewId) as? T
    }

    private fun getFragmentTransaction(hasAnimation: Boolean = true): FragmentTransaction {
        val fragmentTransaction = this@HMFragmentHelper.fragmentManager.beginTransaction()

        if (hasAnimation) {
            // todo custom animation when change fragment
        }
        return fragmentTransaction
    }

    fun popToRoot() {
        val backStackCount = this@HMFragmentHelper.fragmentManager.backStackEntryCount
        for (i in 0 until backStackCount) {
            val backStackId = this@HMFragmentHelper.fragmentManager.getBackStackEntryAt(i).id
            this@HMFragmentHelper.fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}