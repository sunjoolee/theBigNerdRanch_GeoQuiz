package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider

//Log.d TAG
private const val TAG = "MainActivity"

//SIS에 저장할 Bundle 객체에 저장될 데이터의 키
private const val KEY_INDEX = "index"

//요청 코드
//자식 액티비티로부터 데이터를 돌려받을 때 어떤 자식 액티비티가 결과를 돌려주는 것인지 알고자 할때 사용
private const val REQUEST_CODE_CHEAT = 0


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton:Button
    private lateinit var versionTextView: TextView

    //ViewModel 인스턴스 사용하기
    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            Log.d(TAG, "onCreate(Bundle?) called")
            setContentView(R.layout.activity_main)

            val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0)?:0
            quizViewModel.currentIndex = currentIndex

            trueButton = findViewById(R.id.true_button)
            falseButton = findViewById(R.id.false_button)
            nextButton = findViewById(R.id.next_button)
            previousButton = findViewById(R.id.previous_button)
            questionTextView = findViewById(R.id.question_text_view)
            cheatButton = findViewById(R.id.cheat_button)

            versionTextView = findViewById(R.id.version_text_view)
            versionTextView.setText("API 레벨 ${Build.VERSION.SDK_INT}")

            trueButton.setOnClickListener {
                checkAnswer(true)
            }

            falseButton.setOnClickListener {
                checkAnswer(false)
            }

            nextButton.setOnClickListener {
                quizViewModel.moveToNext()
                updateQuestion()
            }

            questionTextView.setOnClickListener {
                quizViewModel.moveToNext()
                updateQuestion()
            }

            previousButton.setOnClickListener {
                quizViewModel.moveToPrevious()
                updateQuestion()
            }

            cheatButton.setOnClickListener{ view ->
                //CheatActivity를 시작시킨다
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val options =
                        ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                    //자식 액티비티로부터 데이터를 돌려받기 위해 사용
                    startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
                }else{
                    startActivityForResult(intent, REQUEST_CODE_CHEAT)
                }
            }

            updateQuestion()
        }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }
    
    //CheatActivity가 돌려주는 결과 값을 가져오기 위해 오버라이드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if(resultCode != Activity.RESULT_OK){
            return
        }

        if(requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.setCurrentQuestionCheated(
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?: false
            )
        }
    }

        private fun updateQuestion() {
            val questionTextResId = quizViewModel.currentQuestionText
            questionTextView.setText(questionTextResId)
        }

        private fun checkAnswer(userAnswer: Boolean) {
            val correctAnswer = quizViewModel.currentQuestionAnswer

            val messageResId = when{
                quizViewModel.getCurrentQuestionCheated() -> R.string.judgement_toast
                userAnswer == correctAnswer -> R.string.correct_toast
                else -> R.string.incorrect_toast
            }

            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .apply{
                    this.setGravity(Gravity.TOP,Gravity.CENTER,300)
                }
                .show()
        }
    }


