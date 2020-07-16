package net.toannt.hacore.utils.sharepreference

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.io.Serializable

class HMPreference(context: Context) {

    private val sharedPref: SharedPreferences =
        context.applicationContext.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    private var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private var sharedObservable = PublishSubject.create<String>()

    init {
        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            sharedObservable.onNext(key)
        }

        sharedPref.registerOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Put data into shared preference
     * This method will negotiate data type automatically
     *
     * @param key String preference key need to store
     * @param data Any? data need to store, set to null to remove value by its key
     */
    fun set(key: String, data: Any?) {
        if (key.isBlank()) throw Exception("Preference key is blank!")
        val editor = this@HMPreference.sharedPref.edit()

        if (data == null) {
            editor.remove(key)
            editor.apply()
            return
        }

        when (data) {
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is String -> editor.putString(key, data)
            is Serializable -> {

            }
        }

        editor.apply()
    }

    /**
     * Retrieve data by preference key and data class
     *
     * @param key String preference key need to retrieve value
     * @param dataType Class<T> class type of data need to get
     * @param defaultValue T? if reference value is null or parsing error, will return default value (optional)
     * @return T?
     */
    fun <T> get(key: String, dataType: Class<T>, defaultValue: T? = null): T? {
        if (key.isBlank()) throw Exception("Preference key is blank!")

        if (!this@HMPreference.sharedPref.contains(key)) {
            return null
        }

        return when (dataType) {
            Int::class.java -> this@HMPreference.sharedPref.getInt(key, (defaultValue as? Int) ?: -1) as T
            Long::class.java -> this@HMPreference.sharedPref.getLong(key, (defaultValue as? Long) ?: -1) as T
            Boolean::class.java -> this@HMPreference.sharedPref.getBoolean(
                key,
                (defaultValue as? Boolean) ?: false
            ) as T
            Float::class.java -> this@HMPreference.sharedPref.getFloat(key, (defaultValue as? Float) ?: 0.0f) as T
            Double::class.java -> this@HMPreference.sharedPref.getFloat(key, (defaultValue as? Float) ?: 0.0f) as T
            String::class.java -> this@HMPreference.sharedPref.getString(key, (defaultValue as? String) ?: "") as T
            else -> null
        }
    }

    fun <T> observe(key: String, dataType: Class<T>, defaultValue: T? = null): Observable<T>? {
        if (key.isBlank()) throw Exception("Preference key is blank!")

        if (!this@HMPreference.sharedPref.contains(key)) {
            return notify(key, defaultValue) as Observable<T>
        }

        return when (dataType) {
            Int::class.java -> notify(key, (defaultValue as? Int) ?: -1) as Observable<T>
            Long::class.java -> notify(key, (defaultValue as? Long) ?: -1) as Observable<T>
            Long::class.java -> notify(key, (defaultValue as? Boolean) ?: false) as Observable<T>
            Float::class.java -> notify(key, (defaultValue as? Float) ?: 0.0f) as Observable<T>
            Double::class.java -> notify(key, (defaultValue as? Double) ?: 0.0f) as Observable<T>
            String::class.java -> notify(key, (defaultValue as? String) ?: "") as Observable<T>
            else -> null
        }
    }

    fun <T> observeLiveData(key: String, dataType: Class<T>, defaultValue: T? = null): MutableLiveData<T>? {
        if (key.isBlank()) throw Exception("Preference key is blank!")

        if (!this@HMPreference.sharedPref.contains(key)) {
            return LiveSharedPreference(key, defaultValue) as MutableLiveData<T>
        }

        return when (dataType) {
            Int::class.java -> LiveSharedPreference(key, (defaultValue as? Int) ?: -1) as MutableLiveData<T>
            Long::class.java -> LiveSharedPreference(key, (defaultValue as? Long) ?: -1) as MutableLiveData<T>
            Long::class.java -> LiveSharedPreference(key, (defaultValue as? Boolean) ?: false) as MutableLiveData<T>
            Float::class.java -> LiveSharedPreference(key, (defaultValue as? Float) ?: 0.0f) as MutableLiveData<T>
            Double::class.java -> LiveSharedPreference(key, (defaultValue as? Double) ?: 0.0f) as MutableLiveData<T>
            String::class.java -> LiveSharedPreference(key, (defaultValue as? String) ?: "") as MutableLiveData<T>
            else -> null
        }
    }

    companion object {
        private var preference: HMPreference? = null


        fun init(context: Context) {
            preference =
                HMPreference(context)
        }

        fun getDefault(): HMPreference {
            return preference ?: throw Exception("you must init HMPreference")
        }
    }

    private fun <T> notify(key: String, defaultValue: T): PublishSubject<T> {
        var instance = PublishSubject.create<T>()

        this@HMPreference.sharedObservable
            .filter { content -> content == key }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<String>() {
                override fun onComplete() {
                }

                override fun onNext(t: String) {
                    instance.onNext(this@HMPreference.sharedPref.all[key] as T ?: defaultValue)
                }

                override fun onError(e: Throwable) {
                }

            })

        return instance
    }

    inner class LiveSharedPreference<T>(private val key: String, private val defaultValue: T) :
        MutableLiveData<T>() {
        private val compositeDisable = CompositeDisposable()

        init {
            value = this@HMPreference.sharedPref.all[key] as T ?: defaultValue

            compositeDisable.add(
                this@HMPreference.sharedObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<String>() {

                        override fun onComplete() {
                        }

                        override fun onError(e: Throwable) {
                        }

                        override fun onNext(t: String) {
                            postValue((this@HMPreference.sharedPref.all[key] as T) ?: defaultValue)
                        }
                    })
            )
        }
    }
}