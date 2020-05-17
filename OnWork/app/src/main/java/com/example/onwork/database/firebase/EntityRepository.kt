package com.example.onwork.database.firebase

import com.example.onwork.model.DateFormatSnapshot
import com.example.onwork.model.IdentifiableUser
import com.example.onwork.model.TimeEntryFirebase

/**
 * Functions as an access point to the database for the application.
 * The base repository decides which database class the app uses to get its data from.
 */
open class EntityRepository {
    companion object {
        val repository = FirebaseRepository()
    }

    /**
     * Gets all data from given table name.
     */
    suspend inline fun <reified E : IdentifiableUser> getAllFromTable(
        table: String,
        userEmail: String
    ): List<E> {
        return repository.getAllFromTable(table, userEmail, E::class.java)
    }

    /**
     * Updates user's date format.
     */
    suspend inline fun updateItemFromDateFormat(
        userEmail: String,
        item: DateFormatSnapshot
    ): DateFormatSnapshot {
        return repository.updateItemFromDateFormat(userEmail, item)
    }

    /**
     * Updates user's time entry.
     */
    suspend inline fun insertItemFromTimeEntry(
        item: TimeEntryFirebase
    ) {
        repository.insertItemFromTimeEntry(item)
    }

    /**
     * Deletes user's time entry.
     */
    suspend inline fun deleteItemFromTimeEntry(
        item: TimeEntryFirebase
    ) {
        repository.deleteItemFromTimeEntry(item)
    }

    /**
     * Updates user's time entry.
     */
    suspend inline fun updateItemFromTimeEntry(
        current: TimeEntryFirebase,
        new: TimeEntryFirebase
    ) {
        repository.updateItemFromTimeEntry(current, new)
    }

    /**
     * Deletes user's all time entries.
     */
    suspend inline fun deleteAllFromTimeEntry(
        userEmail: String
    ) {
        repository.deleteAllFromTimeEntry(userEmail)
    }

    /**
     * Deletes user's date format.
     */
    suspend inline fun deleteAllFromDateFormat(
        userEmail: String
    ) {
        repository.deleteAllFromDateFormat(userEmail)
    }

    /**
     * Gets user's date format.
     */
    suspend inline fun getItemFromDateFormat(userEmail: String): DateFormatSnapshot? {
        return repository.getItemFromDateFormat(userEmail)
    }

    /**
     * Gets user's time entries.
     */
    suspend inline fun getAllTimeEntries(userEmail: String): List<TimeEntryFirebase> {
        return repository.getAllTimeEntries(userEmail)
    }
}