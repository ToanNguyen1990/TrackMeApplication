package net.toannt.hacore.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.PopupMenu
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.core.view.forEachIndexed
import com.google.android.material.tabs.TabLayout

class AppSegmentTabLayout : TabLayout {

    private var onMenuItemSelected: ((menuItemId: Int) -> Unit)? = null
    private var tabMenuItems = HashMap<@androidx.annotation.IdRes Int, Tab>()

    constructor(context: Context?) : super(context) {
        this@AppSegmentTabLayout.initial()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        this@AppSegmentTabLayout.initial()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this@AppSegmentTabLayout.initial()
    }

    fun inflateMenu(@MenuRes menuResId: Int) {
        this@AppSegmentTabLayout.removeAllTabs()
        this@AppSegmentTabLayout.tabMenuItems.clear()

        val virtualMenuInflater = PopupMenu(this@AppSegmentTabLayout.context, this@AppSegmentTabLayout)
        virtualMenuInflater.inflate(menuResId)

        virtualMenuInflater.menu.forEachIndexed { index, item ->

            val tabItem = this@AppSegmentTabLayout.newTab()
            tabItem.tag = item.itemId
            tabItem.text = item.title
            this@AppSegmentTabLayout.addTab(tabItem)
            this@AppSegmentTabLayout.tabMenuItems[item.itemId] = tabItem
        }
    }

    fun setOnSegmentTabMenuSelected(block: ((itemId: Int) -> Unit)?) {
        this@AppSegmentTabLayout.onMenuItemSelected = block
    }

    fun selecteMenuItem(@IdRes menuItemId: Int) {
        if (!this@AppSegmentTabLayout.tabMenuItems.containsKey(menuItemId)) {
            return
        }

        this@AppSegmentTabLayout.tabMenuItems[menuItemId]?.select()
    }

    private val onTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabReselected(tab: Tab?) {
        }

        override fun onTabUnselected(tab: Tab?) {
        }

        override fun onTabSelected(tab: Tab?) {
            (tab?.tag as? Int)?.let { this@AppSegmentTabLayout.onMenuItemSelected?.invoke(it) }
        }
    }

    private fun initial() {
        this@AppSegmentTabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT)
        this@AppSegmentTabLayout.setTabRippleColorResource(android.R.color.transparent)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this@AppSegmentTabLayout.addOnTabSelectedListener(this@AppSegmentTabLayout.onTabSelectedListener)
    }

    override fun onDetachedFromWindow() {
        this@AppSegmentTabLayout.removeOnTabSelectedListener(this@AppSegmentTabLayout.onTabSelectedListener)
        super.onDetachedFromWindow()
    }
}

