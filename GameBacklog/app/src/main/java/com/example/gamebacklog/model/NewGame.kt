package com.example.gamebacklog.model

data class NewGame(
    var title: String = "",
    var platform: String = "",
    var year: Int? = null,
    var month: Int? = null,
    var day: Int? = null
)