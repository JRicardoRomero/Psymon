package com.rommultimedia.psymon

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rommultimedia.psymon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var soundPool: SoundPool? = null

    var topScore=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize SoundPool
        //check os version
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val audioAttributes =  AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)

            soundPool = SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes.build())
                .build()
        } else {
            soundPool = SoundPool(2, AudioManager.STREAM_MUSIC,0)

        }
        val winSound:Int = soundPool!!.load(this, R.raw.winsound,1)

        binding.btnStartGame.setOnClickListener {

            soundPool?.play(winSound, 1F,1F,1,0,1F)

            val intent = Intent(this, Game::class.java).apply { }


            startActivity(intent)
        }

        binding.btnCredits.setOnClickListener{
            val intent = Intent(this, Credits::class.java).apply { }
            startActivity(intent)
        }

        loadData()
        setContentView(binding.root)

    }

    private fun loadData() {

        val sharedPreferences=getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val savedScore = sharedPreferences.getString("SCORE_KEY",null)

        println("loadData  savedScore =$savedScore")


        binding.txtScore.text=savedScore
        topScore=savedScore!!.toInt()
        println("loadData  topScore =$topScore")
    }
}