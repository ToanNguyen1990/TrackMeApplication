package net.toannt.hacore.extensions

import androidx.lifecycle.*

typealias LiveDataFilter<T> = (T) -> Boolean

fun <X, Y> LiveData<X>.map(mapper: (X) -> Y) =
    Transformations.map(this, mapper)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, onEmission: (T) -> Unit) {
    return observe(owner, Observer<T> {
        if (it != null) {
            onEmission(it)
        }
    })
}

class FilterLiveData<T>(source: LiveData<T>,
                         filter: LiveDataFilter<T>): MediatorLiveData<T>() {
    init {
        super.addSource(source) {
            if (filter(it)) {
                value = it
            }
        }
    }

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        throw UnsupportedOperationException()
    }

    override fun <T : Any?> removeSource(toRemote: LiveData<T>) {
        throw UnsupportedOperationException()
    }
}

fun <T> LiveData<T>.filter(filter: LiveDataFilter<T>) : MediatorLiveData<T> =  FilterLiveData(this, filter)