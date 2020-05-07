package com.wearetriple.tripleonboarding.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Allows the user to pass in a reference to a function in order to separate the
 * code from the observer.
 */
fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, f: (T) -> Unit) {
    this.observe(owner, Observer<T> { t -> t?.let(f) })
}

/**
 * Also allows the user to pass in a reference to a function, however with this, the function is
 * able to not require the user to pass in a parameter.
 */
fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, f: () -> Unit) {
    this.observe(owner, Observer<T> { f() })
}