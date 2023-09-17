package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

private const val KEY_INDEX = "index"
private const val KEY_CHEAT = "cheat"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
	private lateinit var trueButton: Button
	private lateinit var falseButton: Button
	private lateinit var nextButton: Button
	private lateinit var cheatButton: Button
	private lateinit var questionTextView: TextView

	private val quizViewModel by lazy {
		ViewModelProviders.of(this).get(QuizViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
		quizViewModel.isCheated = savedInstanceState?.getBoolean(KEY_CHEAT, false) ?: false

		trueButton = findViewById(R.id.true_button)
		falseButton = findViewById(R.id.false_button)
		nextButton = findViewById(R.id.next_button)
		cheatButton = findViewById(R.id.cheat_button)
		questionTextView = findViewById(R.id.question_text_view)

		trueButton.setOnClickListener {
			checkAnswer(true)
		}
		falseButton.setOnClickListener {
			checkAnswer(false)
		}
		nextButton.setOnClickListener {
			quizViewModel.moveToNext()
			updateQuestion()
			quizViewModel.isCheated = false
		}
		cheatButton.setOnClickListener {
			val intent =
				CheatActivity.newIntent(this@MainActivity, quizViewModel.currentQuestionAnswer)
			startActivityForResult(intent, REQUEST_CODE_CHEAT)
		}

		updateQuestion()
	}

	private fun updateQuestion() {
		val questionTextResId = quizViewModel.currentQuestionText
		questionTextView.setText(questionTextResId)
	}

	private fun checkAnswer(userAnswer: Boolean) {
		val messageResId = when {
			quizViewModel.isCheated -> R.string.judgment_toast
			userAnswer == quizViewModel.currentQuestionAnswer -> R.string.correct_toast
			else -> R.string.incorrect_toast
		}
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
		outState.putBoolean(KEY_CHEAT, quizViewModel.isCheated)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode != Activity.RESULT_OK)
			return

		if (requestCode == REQUEST_CODE_CHEAT)
			quizViewModel.isCheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
	}
}
