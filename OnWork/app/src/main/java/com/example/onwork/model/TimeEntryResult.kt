package com.example.onwork.model

import java.util.*

data class TimeEntryResult (
    val title: String,
    val userEmail: String,
    var date: String,
    var duration: String,
    var startTime: String,
    var endTime: String
)