package net.toannt.hacore.utils

open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {

    private var creator: ((A) -> T)? = creator

    fun init(arg: A) {
        val created = creator!!(arg)
        instance = created
        creator = null
    }

    @Volatile
    private var instance: T? = null

    @Synchronized
    fun getInstance(): T {
        if (instance == null) {
            throw NullPointerException("${this::class.java.simpleName} instance is not initialized")
        }
        return this.instance!!
    }
}