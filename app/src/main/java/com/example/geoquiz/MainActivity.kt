package com.example.geoquiz

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
	private lateinit var trueButton: Button
	private lateinit var falseButton: Button
	private lateinit var nextButton: Button
	private lateinit var questionTextView: TextView

	private val questionBank = listOf(
		Question(R.string.question_australia, true),
		Question(R.string.question_oceans, true),
		Question(R.string.question_mideast, false),
		Question(R.string.question_africa, false),
		Question(R.string.question_americas, true),
		Question(R.string.question_asia, true)
	)
	private val answers = arrayOfNulls<Boolean>(6)
	private var currentIndex = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		currentIndex = savedInstanceState?.getInt("index") ?: 0

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
			currentIndex = (currentIndex + 1) % questionBank.size
			updateQuestion()
		}

		updateQuestion()
	}

	private fun updateQuestion() {
		val questionTextResId = questionBank[currentIndex].textResId
		questionTextView.setText(questionTextResId)
		trueButton.isEnabled = true
		falseButton.isEnabled = true
	}

	private fun checkAnswer(userAnswer: Boolean) {
		val messageResId: Int
		if (userAnswer == questionBank[currentIndex].answer) {
			messageResId = R.string.correct_toast
			answers[currentIndex] = true
		} else {
			messageResId = R.string.incorrect_toast
			answers[currentIndex] = false
		}
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
		trueButton.isEnabled = false
		falseButton.isEnabled = false
		if (currentIndex == 5) {
			var counter = 0f
			for (i in answers) {
				if (i == true)
					counter++
			}
			Toast.makeText(this, "Your score is: ${(counter / 6 * 100).toInt()}%", Toast.LENGTH_SHORT).show()
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putInt("index", currentIndex)
	}
}
