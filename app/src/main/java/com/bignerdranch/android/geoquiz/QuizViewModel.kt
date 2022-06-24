package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel(){
    var currentIndex = 0

    //사용자의 컨닝 상태 구성 변경 시 보존하기 위해 QuizViewModel에 저장
    var isCheater = false

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    val currentQuestionAnswer:Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText : Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious(){
        currentIndex =
            if(currentIndex - 1 < 0) questionBank.size - 1
            else currentIndex - 1
    }
}