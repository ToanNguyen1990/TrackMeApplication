package net.toannt.hacore.utils.spinneradapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter


abstract class HMSingleModeSpinnerAdapter<M> : ArrayAdapter<M> {

    protected val actualItems: ArrayList<M> = ArrayList()
    private var layoutResId: Int = 0

    constructor(context: Context, resourceId: Int) : super(context, resourceId) {
        this@HMSingleModeSpinnerAdapter.layoutResId = resourceId
    }

    open fun getDropDownViewSameView(): Boolean = true

    abstract fun getViewHolder(): HMSingleViewModel<M>

    open fun getDropDownViewHolder(): HMSingleViewModel<M>? = null

    fun updateItems(newItems : List<M>?) {
        this@HMSingleModeSpinnerAdapter.actualItems.clear()
        this@HMSingleModeSpinnerAdapter.actualItems.addAll(newItems ?: emptyList())
    }

    fun getItems(): List<M> {
        return actualItems
    }

    override fun getCount(): Int {
        return this@HMSingleModeSpinnerAdapter.actualItems.size
    }

    override fun getItem(position: Int): M? {
        return actualItems[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        when(getDropDownViewSameView()) {
            true -> {
                return initView(position, convertView, parent)
            }

            else -> {
                return initDropDownView(position, convertView, parent)
            }
        }
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View
        var holder : HMSingleViewModel<M>

        if (convertView == null) {
            holder = getViewHolder()
            view = LayoutInflater.from(parent.context).inflate(holder.layoutResId, parent, false)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as HMSingleViewModel<M>
        }


        holder.bind(view, actualItems[position], position)
        return view
    }

    private fun initDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View
        var holder : HMSingleViewModel<M>

        if (convertView == null) {
            holder = getDropDownViewHolder()!!
            view = LayoutInflater.from(parent.context).inflate(holder.layoutResId, parent, false)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as HMSingleViewModel<M>
        }


        holder.bind(view, actualItems[position], position)
        return view
    }

    abstract class HMSingleViewModel<M>(var layoutResId: Int) {

        open fun bind(view: View, item: M, position: Int) = Unit
    }
}