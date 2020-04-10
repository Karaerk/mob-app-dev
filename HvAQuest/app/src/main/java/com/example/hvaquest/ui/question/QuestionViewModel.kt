package com.example.hvaquest.ui.question

import android.widget.RadioButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hvaquest.database.QuestRepository
import com.example.hvaquest.model.Question

class QuestionViewModel : ViewModel() {

    private val questRepository = QuestRepository()
    private val questions = questRepository.getHvaQuest()
    var questionNumber = 0

    val question = MutableLiveData<Question>()
    val status = MutableLiveData<String>()
    val choice = MutableLiveData<RadioButton>()
    val error = MutableLiveData<Boolean>(false)
    val showLocationClue = MutableLiveData<Boolean>(false)
    val gameOver = MutableLiveData<Boolean>(false)

    init {
        prepareQuestion()
    }

    /**
     * Executes some checks on the confirmed choice to see if user can go to next
     * question/complete screen.
     */
    fun confirmChoice() {
        if (isChoiceFilledIn() && isChoiceCorrect()) {
            if (isLastQuestionShown()) {
                gameOver.value = true
            } else {
                showLocationClue.value = true
                prepareQuestion()
            }
        } else {
            error.value = true
        }
    }

    /**
     * Returns the index of current question.
     */
    fun getQuestionIndexOfCurrent(): Int {
        return questions.indexOf(question.value)
    }

    /**
     * Returns a question found by its index.
     */
    fun getQuestionByIndex(index: Int): Question {
        return questions[index]
    }

    /**
     * Resets the whole game to its initial state.
     */
    fun resetGame() {
        questionNumber = 0
        prepareQuestion()
    }

    /**
     * Prepares a new question to the user while bringing some attributes to its initial state.
     */
    fun prepareQuestion(index: Int = questionNumber) {
        questionNumber = index

        question.value = questions[questionNumber]
        status.value = "${questionNumber + 1}/${questions.size}"
        choice.value = null
        error.value = false
        gameOver.value = false
        questionNumber++
    }

    private fun isChoiceFilledIn() = choice.value != null && choice.value!!.text != ""
    private fun isChoiceCorrect() = question.value!!.correctAnswer == choice.value!!.text
    private fun isLastQuestionShown() = questionNumber == questions.size
}