package com.example.onwork.model

import java.util.*

data class TimeEntryResult (
    val title: String,
    val userEmail: String,
    var date: Date,
    var duration: String,
    var startTime: Date,
    var endTime: Date,
    var id: Long
)