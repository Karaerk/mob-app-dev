package com.example.onwork.database.firebase

import android.util.Log
import com.example.onwork.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Gets its data from a Firebase realtime database.
 * This repository allows to define callback wrappers which are protected from
 * accidental multiple calls to resume/resumeWithException
 */
class FirebaseRepository {

    private val dateFormats = DateFormatEnum.values()

    companion object {
        const val LOG_TAG = "FirebaseRepository"
        const val MISSING_DATA = "data missing"
        const val INVALID_FORMAT = "invalid data format"
    }

    inner class WrappedContinuation<T>(private val c: Continuation<T>) : Continuation<T> {
        private var isResolved = false
        override val context: CoroutineContext
            get() = c.context

        override fun resumeWith(result: Result<T>) {
            if (!isResolved) {
                isResolved = true
                c.resumeWith(result)
            }
        }

    }

    private suspend inline fun <T> suspendCoroutineWrapper(crossinline block: (WrappedContinuation<T>) -> Unit): T =
        suspendCancellableCoroutine { c ->
            val wd = WrappedContinuation(c)
            block(wd)
        }

    suspend fun <T : IdentifiableUser> getAllFromTable(
        table: String,
        userEmail: String,
        dataType: Class<T>
    ): List<T> =
        suspendCoroutineWrapper { d ->
            val ref = FirebaseDatabase.getInstance().getReference(table)

            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    d.resumeWithException(p0.toException())
                    Log.e(LOG_TAG, "Error while getting data from $table", p0.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val data = ArrayList<T>()

                        snapshot.children.forEach {
                            val item: T? = it.getValue(dataType)

                            if (item != null && item.userEmail == userEmail) {
                                data.add(item)
                            }
                        }

                        if (data.isNotEmpty()) {
                            d.resume(data)
                        } else {
                            val errorMessage = when (snapshot.value) {
                                null -> MISSING_DATA
                                else -> INVALID_FORMAT
                            }
                            d.resumeWithException(Exception(errorMessage))
                        }
                    } catch (e: Exception) {
                        d.resumeWithException(e)
                        Log.e(LOG_TAG, e.message, e)
                    }
                }
            })
        }

    suspend fun updateItemFromDateFormat(
        userEmail: String,
        item: DateFormatSnapshot
    ): DateFormatSnapshot =
        suspendCoroutineWrapper { d ->
            val child = "dateFormat"
            val ref = FirebaseDatabase.getInstance().getReference(child)

            val itemDb = ref.orderByChild("userEmail").equalTo(userEmail).limitToFirst(1)

            itemDb.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    d.resumeWithException(p0.toException())
                    Log.e(LOG_TAG, "Error while getting data from $child", p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        // Only update item if item exists
                        if (p0.value != null) {
                            val snapshot = DateFormatSnapshot()

                            // Assume the database has more than one entry
                            try {
                                val values = p0.value as ArrayList<*>
                                snapshot.id = values.lastIndex.toString()
                            } catch (e: Exception) {
                                val value = p0.value as HashMap<*, *>
                                snapshot.id = value.entries.iterator().next().key.toString()
                            }
                            snapshot.key = p0.key!!

                            p0.children.forEach {
                                snapshot.value = it.getValue(DateFormatFirebase::class.java)
                            }

                            ref.child(snapshot.id.toString()).setValue(item.value)
                        } else {
                            ref.push()
                                .setValue(item.value) // Push new item as there's no item to update
                        }
                        d.resume(item)
                    } catch (e: Exception) {
                        d.resumeWithException(e)
                        Log.e(LOG_TAG, e.message, e)
                    }
                }
            })
        }

    suspend fun getItemFromDateFormat(userEmail: String): DateFormatSnapshot? =
        suspendCoroutineWrapper { d ->
            val child = "dateFormat"
            val ref = FirebaseDatabase.getInstance().getReference(child)

            val itemDb = ref.orderByChild("userEmail").equalTo(userEmail).limitToFirst(1)

            itemDb.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    d.resumeWithException(p0.toException())
                    Log.e(LOG_TAG, "Error while getting data from $child", p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        if (p0.value != null) {
                            val snapshot = DateFormatSnapshot()

                            // Assume the database has more than one entry
                            try {
                                val values = p0.value as ArrayList<*>
                                snapshot.id = values.lastIndex.toString()
                            } catch (e: Exception) {
                                val value = p0.value as HashMap<*, *>
                                snapshot.id = value.entries.iterator().next().key.toString()
                            }
                            snapshot.key = p0.key!!

                            p0.children.forEach {
                                snapshot.value = it.getValue(DateFormatFirebase::class.java)
                            }

                            d.resume(snapshot)
                        } else {
                            d.resume(null)
                        }
                    } catch (e: Exception) {
                        d.resumeWithException(e)
                        Log.e(LOG_TAG, e.message, e)
                    }
                }
            })
        }

    /*
    suspend fun getAllTimeEntries(userEmail: String): List<TimeEntrySnapshot> =
        suspendCoroutineWrapper { d ->
            val child = "timeEntry"
            val ref = FirebaseDatabase.getInstance().getReference(child)

            val itemDb = ref.orderByChild("userEmail").equalTo(userEmail)

            itemDb.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    d.resumeWithException(p0.toException())
                    Log.e(LOG_TAG, "Error while getting data from $child", p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        val data = ArrayList<TimeEntrySnapshot>()
                        println(p0)
                        println(p0.children)
                        println(p0.value)
                        println(p0.key)

                        if (p0.children != null) {
                            val values = p0.children as HashMap<*, *>
                            values.forEach {
                                println(it.key)
                                println(it.value)
                                println(it)
                            }


                            // Assume the database has more than one entry
//                            try {
//                                val values = p0.value as ArrayList<*>
//                                snapshot.id = values.lastIndex.toString()
//                            } catch (e: Exception) {
//                                val value = p0.value as HashMap<*, *>
//                                snapshot.id = value.entries.iterator().next().key.toString()
//                            }
//                            snapshot.key = p0.key!!
//
//                            p0.children.forEach {
//                                snapshot.value = it.getValue(TimeEntryFirebase::class.java)
//                            }

                            d.resume(data)
                        } else {
                            d.resume(data)
                        }
                    } catch (e: Exception) {
                        d.resumeWithException(e)
                        Log.e(LOG_TAG, e.message, e)
                    }
                }
            })
        }
     */

    suspend fun insertItemFromTimeEntry(
        item: TimeEntryFirebase
    ): Unit =
        suspendCoroutineWrapper { d ->
            val child = "timeEntry"
            val ref = FirebaseDatabase.getInstance().getReference(child)

            ref.push().setValue(item)
        }
}