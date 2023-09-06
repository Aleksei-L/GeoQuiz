package com.example.geoquiz

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

private const val KEY_INDEX = "index"
private const val KEY_ANSWERS = "answers"

class MainActivity : AppCompatActivity() {
	private lateinit var trueButton: Button
	private lateinit var falseButton: Button
	private lateinit var nextButton: Button
	private lateinit var questionTextView: TextView

	private val quizViewModel by lazy {
		ViewModelProviders.of(this).get(QuizViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
		quizViewModel.answers = savedInstanceState?.getBooleanArray(KEY_ANSWERS) ?: BooleanArray(6)

		trueButton = findViewById(R.id.true_button)
		falseButton = findViewById(R.id.false_button)
		nextButton = findViewById(R.id.next_button)
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
		}

		updateQuestion()
	}

	private fun updateQuestion() {
		val questionTextResId = quizViewModel.currentQuestionText
		questionTextView.setText(questionTextResId)
		trueButton.isEnabled = true
		falseButton.isEnabled = true
	}

	private fun checkAnswer(userAnswer: Boolean) {
		val messageResId: Int
		if (userAnswer == quizViewModel.currentQuestionAnswer) {
			messageResId = R.string.correct_toast
			quizViewModel.answers[quizViewModel.currentIndex] = true
		} else {
			messageResId = R.string.incorrect_toast
			quizViewModel.answers[quizViewModel.currentIndex] = false
		}
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
		trueButton.isEnabled = false
		falseButton.isEnabled = false
		if (quizViewModel.currentIndex == 5) {
			var counter = 0f
			for (i in quizViewModel.answers) {
				if (i)
					counter++
			}
			Toast.makeText(
				this,
				"Your score is: ${(counter / 6 * 100).toInt()}%",
				Toast.LENGTH_SHORT
			).show()
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
		outState.putBooleanArray(KEY_ANSWERS, quizViewModel.answers)
	}
}
