package com.example.onwork.database.firebase

import com.example.onwork.model.DateFormatSnapshot
import com.example.onwork.model.IdentifiableUser

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
     * Gets user's date format.
     */
    suspend inline fun getItemFromDateFormat(userEmail: String): DateFormatSnapshot? {
        return repository.getItemFromDateFormat(userEmail)
    }
}