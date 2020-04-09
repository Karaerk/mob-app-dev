package com.example.quizmaker

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    var question: String = ""
    var answer: String = ""

    fun isAnswerCorrect(answer: String): Boolean {
        return answer.toLowerCase() == this.answer.toLowerCase()
    }

    fun createQuestion(question: String, answer: String) {
        this.question = question
        this.answer = answer
    }

    fun isQuestionCreated(): Boolean {
        return question.isNotBlank() && answer.isNotBlank()
    }
}