package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


//부모 액티비티로부터 넘겨받은 인텐트에 저장된 엑스트라의 키
//엑스트라의 데이터를 읽어서 사용하는 액티비티에 엑스트라 키를 정의함
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"

//부모 액티비티로 넘겨줄 인텐트에 저장할 엑스트라의 키 (public)
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {
            val answerText = when{
                answerIsTrue -> R.string.correct_toast
                else -> R.string.incorrect_toast
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isAnswerShown:Boolean){
        val data = Intent().apply{
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        //Activity.RESULT_OK : 자식 액티비티가 setResult(...)를 호출했다는 결과 코드
        //Activity.RESULT_CANCELED : 자식 액티비티가 setResult(...)를 호출하지 않은 채로 종료된 경우 결과 코드
        setResult(Activity.RESULT_OK, data)
    }

    companion object{
        //CheatActivity가 필요로하는 엑스트라 데이터를 갖는 인텐트 생성
        //MainActivity의 cheatButton 버튼 리스너에서 newIntent(...)함수 사용
        fun newIntent(packageContext: Context, answerIsTrue:Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply{
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}