package com.example.onwork.model

data class TimeEntryFirebase(
    var userEmail: String = "",
    var startTime: Long = 0,
    var endTime: Long = 0,
    var title: String = ""
)