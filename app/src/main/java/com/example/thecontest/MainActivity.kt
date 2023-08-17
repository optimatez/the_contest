package com.example.thecontest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Button
import android.media.MediaPlayer
import android.util.Log
import android.content.res.Configuration

class MainActivity : AppCompatActivity() {

    private lateinit var counterTextView: TextView
    private var counter = 0
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var beepMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_linear)
            Log.d("contentView", "LinearView")
        } else {
            setContentView(R.layout.activity_main_constraint)
            Log.d("contentView", "ConstraintsView")
        }

        counterTextView = findViewById(R.id.counterTextView)
        mediaPlayer = MediaPlayer.create(this, R.raw.slide_whistle_crazy_series)
        beepMediaPlayer = MediaPlayer.create(this, R.raw.beep_short)

        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt("counter", 0)
            updateCounterText()
            val isPlaying = savedInstanceState.getBoolean("isPlaying", false)
            if (isPlaying) {
                val currentPosition = savedInstanceState.getInt("currentPosition", 0)
                mediaPlayer.seekTo(currentPosition)
                mediaPlayer.start()
            }
            Log.d("savedInstanceState", "The saved instance state was loaded.")
        }

        val plusButton: Button = findViewById(R.id.plusButton)
        plusButton.setOnClickListener {
            onPlusButtonClick(it)
        }

        val minusButton: Button = findViewById(R.id.minusButton)
        minusButton.setOnClickListener {
            onMinusButtonClick(it)
        }

        val resetButton: Button = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            onResetButtonClick(it)
        }
    }

    private fun playBeepSound() {
        if (beepMediaPlayer.isPlaying) {
            beepMediaPlayer.seekTo(0)
        } else {
            beepMediaPlayer.start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("counter", counter)
        if (mediaPlayer.isPlaying) {
            outState.putBoolean("isPlaying", true)
            outState.putInt("currentPosition", mediaPlayer.currentPosition)
        }
    }

    private fun onPlusButtonClick(view: View) {
        if (counter < 15) {
            playBeepSound()
            counter++
            updateCounterText()
            checkCounterValueForSound()
        }
    }

    private fun onMinusButtonClick(view: View) {
        if (counter > 0) {
            playBeepSound()
            counter--
            updateCounterText()
            checkCounterValueForSound()
        }
    }

    private fun onResetButtonClick(view: View) {
        playBeepSound()
        counter = 0
        updateCounterText()
        checkCounterValueForSound()
    }

    private fun updateCounterText() {
        counterTextView.text = counter.toString()
    }

    private fun checkCounterValueForSound() {
        if (counter == 15) {
            mediaPlayer.start()
            Log.d("mediaPlayer", "The counter has reached 15, and the sound has started.")
        } else if (counter < 15 && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            Log.d("mediaPlayer", "The counter has dropped below 15, and the sound has stopped.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}