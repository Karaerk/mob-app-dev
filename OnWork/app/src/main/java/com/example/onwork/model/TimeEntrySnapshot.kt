package com.example.onwork.model

data class TimeEntrySnapshot(
    var key: String = "",
    var value: TimeEntryFirebase? = null,
    var id: String? = null
)