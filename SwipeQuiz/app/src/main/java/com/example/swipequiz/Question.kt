package com.example.swipequiz

data class Question(
    val questionText: String,
    val questionAnswer: Boolean
) {
    companion object {
        var questions = arrayListOf(
            "A \'val\' and \'var\' are the same.",
            "Mobile Application Development grants 12 ECTS.",
            "A Unit in Kotlin corresponds to a void in Java.",
            "In Kotlin \'when\' replaces the \'switch\' operator in Java."
        )

        var answers = arrayListOf(
            false,
            false,
            true,
            true
        )
    }
}