package com.example.hvaquest.database

import com.example.hvaquest.R
import com.example.hvaquest.model.Question

class QuestRepository {
    fun getHvaQuest(): List<Question> {
        return arrayListOf(
            Question(
                "Who is the co-founder of Android?",
                arrayOf("Andy Rubin", "Larry Page & Sergey Brin", "Sundar Pichai"),
                "Andy Rubin",
                R.drawable.nicolaes_tulphuis
            ),
            Question(
                "In which language is Android written?",
                arrayOf("Java/Kotlin", "Swift", "JavaScript"),
                "Java/Kotlin",
                R.drawable.fraijlemaborg
            ),
            Question(
                "Which method gets called after onCreate in the Activity-lifecycle?",
                arrayOf("onStart", "onCreateView", "onResume"),
                "onStart",
                R.drawable.leeuwenburg
            ),
            Question(
                "In what year did google announce Material Design?",
                arrayOf("2012", "2013", "2014"),
                "2014",
                R.drawable.muller_lulofshuis
            ),
            Question(
                "What is the code name of version 1.6?",
                arrayOf("Cupcake", "Donut", "Eclair"),
                "Donut",
                R.drawable.wibauthuis
            ),
            Question(
                "Which version of Android did not run on phones?",
                arrayOf("Android 1.0", "Android 2.0", "Android 3.0"),
                "Android 3.0",
                R.drawable.studio_hva
            ),
            Question(
                "When did Google announced Android Wear?",
                arrayOf("2012", "2013", "2014"),
                "2014",
                R.drawable.theo_thijssenhuis
            ),
            Question(
                "Who built the first smartwatch?",
                arrayOf("Sony, 2010", "Samsung, 2012", "LG, 2014"),
                "Sony, 2010",
                R.drawable.kohnstammhuis
            ),
            Question(
                "For what for OS was Android originally created?",
                arrayOf("Desktop computers", "Digital cameras", "Smartphones"),
                "Digital cameras",
                R.drawable.benno_premselahuis
            ),
            Question(
                "Who is the CEO of Google?",
                arrayOf("Andy Rubin", "Larry Page & Sergey Brin", "Sundar Pichai"),
                "Sundar Pichai",
                R.drawable.koetsier_montaignehuis
            )
        )
    }
}