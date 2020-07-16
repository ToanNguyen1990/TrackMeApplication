package net.toannt.hacore.utils.recycleradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

abstract class HMSingleModelRecyclerAdapter<M, V : HMSingleModelRecyclerAdapter.HMSingleModelViewHolder<M>> :
    RecyclerView.Adapter<V>() {

    private val originItems: ArrayList<M> = ArrayList()
    protected val actualItems: ArrayList<M> = ArrayList()
    protected val compositeDisposable = CompositeDisposable()

    open fun updateItems(newItems: List<M>?, isDiffUtil: Boolean = false) {
        this@HMSingleModelRecyclerAdapter.originItems.clear()
        this@HMSingleModelRecyclerAdapter.originItems.addAll(newItems ?: emptyList())
        this@HMSingleModelRecyclerAdapter.filterItems(isDiffUtil)
    }

    fun getItemWith(position: Int): M? {
        return this@HMSingleModelRecyclerAdapter.actualItems.getOrNull(position)
    }

    private fun filterItems(isDiffUtil: Boolean) {
        val willDisplayingItems = this@HMSingleModelRecyclerAdapter.originItems

        if (willDisplayingItems.isNotEmpty() && this@HMSingleModelRecyclerAdapter.actualItems.isEmpty() || isDiffUtil) {
            this@HMSingleModelRecyclerAdapter.actualItems.clear()
            this@HMSingleModelRecyclerAdapter.actualItems.addAll(willDisplayingItems)
            this@HMSingleModelRecyclerAdapter.notifyDataSetChanged()
            return
        }

        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize(): Int = this@HMSingleModelRecyclerAdapter.actualItems.size

            override fun getNewListSize(): Int = willDisplayingItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = this@HMSingleModelRecyclerAdapter.actualItems[oldItemPosition]
                val newItem = willDisplayingItems[newItemPosition]
                return this@HMSingleModelRecyclerAdapter.onCheckItem(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = this@HMSingleModelRecyclerAdapter.actualItems[oldItemPosition]
                val newItem = willDisplayingItems[newItemPosition]
                return this@HMSingleModelRecyclerAdapter.onCheckItemContents(oldItem, newItem)
            }

        }, true)

        this@HMSingleModelRecyclerAdapter.actualItems.clear()
        this@HMSingleModelRecyclerAdapter.actualItems.addAll(willDisplayingItems)
        diffResult.dispatchUpdatesTo(this@HMSingleModelRecyclerAdapter)
    }

    open fun onCheckItemContents(oldItem: M, newItem: M): Boolean {
        return false
    }

    open fun onCheckItem(oldItem: M, newItem: M): Boolean {
        return false
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        holder.bind(this@HMSingleModelRecyclerAdapter.actualItems[position], position)
    }

    override fun getItemCount(): Int {
        return this@HMSingleModelRecyclerAdapter.actualItems.count()
    }

    fun getItems(): List<M> {
        return actualItems
    }

    abstract class HMSingleModelViewHolder<M>(parent: ViewGroup, @LayoutRes layoutResId: Int) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)) {

        open fun bind(item: M, position: Int) = Unit
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.clear()
    }
}