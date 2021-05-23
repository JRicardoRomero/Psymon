package com.rommultimedia.psymon

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.rommultimedia.psymon.animation.FlagsAnimator
import com.rommultimedia.psymon.animation.SquaresAnimator
import com.rommultimedia.psymon.databinding.ActivityGameBinding
import kotlin.random.Random

class Game : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    lateinit var rBtn:ImageButton
    lateinit var yBtn:ImageButton
    lateinit var gBtn:ImageButton
    lateinit var bBtn:ImageButton

    var topScore=0

    var win = true


    private var soundPool: SoundPool? = null

    var rSound1:Int =0
    var rSound2:Int =0
    var rSound3:Int =0
    var rSound4:Int =0
    var rSound5:Int =0
    var rSound6:Int =0

    val gamePlaylist= mutableListOf<Animator>()
    private val btnList = mutableListOf<Int>()

    var count=0
    var score=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        loadData()


        //Initialize SoundPool
        //check os version
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            var audioAttributes =  AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)

            soundPool = SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes.build())
                    .build()
        } else {
            soundPool = SoundPool(2,AudioManager.STREAM_MUSIC,0)

        }
        rSound1 = soundPool!!.load(this, R.raw.cred,1)
        rSound2 = soundPool!!.load(this, R.raw.dyellow,1)
        rSound3 = soundPool!!.load(this, R.raw.fgreen,1)
        rSound4 = soundPool!!.load(this, R.raw.gblue,1)
        rSound5 = soundPool!!.load(this, R.raw.winsound,1)
        rSound6 = soundPool!!.load(this, R.raw.lose,1)


        rBtn=binding.btnR
        yBtn=binding.btnY
        gBtn=binding.btnG
        bBtn=binding.btnB

        rBtn.setOnClickListener { SquaresAnimator().scaler(rBtn, soundPool!!,rSound1).start()
                                    checkInput(0,btnList)}
        yBtn.setOnClickListener { SquaresAnimator().scaler(yBtn, soundPool!!,rSound2).start()
                                    checkInput(1,btnList)}
        gBtn.setOnClickListener { SquaresAnimator().scaler(gBtn, soundPool!!,rSound3).start()
                                    checkInput(2,btnList)}
        bBtn.setOnClickListener { SquaresAnimator().scaler(bBtn, soundPool!!,rSound4).start()
                                    checkInput(3,btnList)}

        val rSound5:Int = soundPool!!.load(this, R.raw.rightsound,1)
        soundPool?.play(rSound5, 1F,1F,1,0,1F)
        toggleButtons()
        newRound()
        Handler().postDelayed({playlistRun(gamePlaylist)},3000)
    }

    //Adds a new element to the list then plays back the list
    fun newRound(){
        binding.userMessages.text="Psymon Says:"
        val newNumber=Random.nextInt(0,4)

        //Add reference number to btnList
        btnList.add(newNumber)

        //Create animator from random number and add it to the playlist
        when(newNumber){
            0 -> {
                gamePlaylist.add(SquaresAnimator().scaler(rBtn, soundPool!!,rSound1))
                }
            1 -> {
                gamePlaylist.add(SquaresAnimator().scaler(yBtn, soundPool!!,rSound2))
            }
            2 -> {
                gamePlaylist.add(SquaresAnimator().scaler(gBtn, soundPool!!,rSound3))
            }
            3 -> {
                gamePlaylist.add(SquaresAnimator().scaler(bBtn, soundPool!!,rSound4))
            }
        }

        score=gamePlaylist.size-1
        binding.txtScore.text = score.toString()

    }

    fun playlistRun(playlist:MutableList<Animator>){

        binding.userMessages.text="Psymon Says:"
        FlagsAnimator().flagShow(flag = binding.userMessages).start()

        val compuPlaylist = AnimatorSet()
        compuPlaylist.playSequentially(playlist)
        compuPlaylist.start()
        compuPlaylist.doOnEnd {


            if(!win){
                binding.userMessages.setTextColor(Color.parseColor("#FF0000"))
                binding.userMessages.text="GAME OVER"
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE,"GAME OVER")
                }

                Handler().postDelayed({
                    startActivity(intent)
                },1500)


            } else {

                Handler().postDelayed({
                    toggleButtons()
                    binding.userMessages.text="Your turn:"
                    FlagsAnimator().flagShow(flag = binding.userMessages).start()
                },500)
            }
        }

    }

    fun checkInput(userBtn:Int,btnList: MutableList<Int>){
        toggleButtons()
        if(btnList.elementAt(count)==userBtn){
            toggleButtons()
            count++
        } else {
            binding.userMessages.text="WRONG!"
            win = false
            soundPool?.play(this.rSound6, 1F,1F,1,0,1F)
            Handler().postDelayed({playlistRun(gamePlaylist)
                                    binding.userMessages.text="Correct Sequence"
                FlagsAnimator().flagShow(flag = binding.userMessages).start()
                                  },1000)


            count=0

            var currentScore=binding.txtScore.text.toString().toInt()

            println("topScore = ${MainActivity().topScore}")

            println("Current score = $currentScore")


            if(topScore<currentScore) saveData()
        }

        if(count==btnList.size){
            toggleButtons()
            newRound()
            count=0
            binding.userMessages.text="RIGHT!"
            FlagsAnimator().flagShow(flag = binding.userMessages).start()
            Handler().postDelayed({
                playlistRun(gamePlaylist)
                binding.userMessages.text="Psymon Says:"},2000)
                val rSound5:Int = soundPool!!.load(this, R.raw.winsound,1)
                soundPool?.play(rSound5, 1F,1F,1,0,1F)

        }
    }

    //Disable buttons to prevent untimely user input

    private fun toggleButtons(){
        binding.btnR.isEnabled = binding.btnR.isEnabled.not()
        binding.btnY.isEnabled = binding.btnY.isEnabled.not()
        binding.btnG.isEnabled = binding.btnG.isEnabled.not()
        binding.btnB.isEnabled = binding.btnB.isEnabled.not()

    }

    //Save high score
    private fun saveData(){

        var newScore = binding.txtScore.text.toString()
        val sharedPreferences=getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.apply {
            putString("SCORE_KEY", newScore)
        }.apply()
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