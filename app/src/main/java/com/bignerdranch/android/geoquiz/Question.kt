package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes

val notAnswered = 0
val correct = 1
val wrong = 2

data class Question (@StringRes val textResId:Int, val answer: Boolean, var answered:Int = notAnswered) {
}