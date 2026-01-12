package com.example.rountinegps

import android.os.Bundle
import android.util.Log
import android.widget.Switch
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var recorder: Recorder
    private lateinit var storage: GpsStorage
    private lateinit var replay: ReplayManager

    private lateinit var recordSwitch: SwitchMaterial
    private lateinit var replaySwitch: SwitchMaterial


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recordSwitch = findViewById(R.id.Record)
        replaySwitch = findViewById(R.id.Replay)

        recorder = Recorder(this)
        storage = GpsStorage(this)
        replay = ReplayManager(this)

        // Ví dụ gọi bằng button (bạn gắn UI sau)

        recordSwitch.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                recorder.start()
            }else{
                val points = recorder.stop()   // Stop record
                storage.save(points)           // Save
            }
        }
//
        replaySwitch.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                Log.d("flag","start replay")
                val saved = storage.load()
                replay.setSpeed(1.0)
                replay.play(saved) {
                    Log.d("flag","finish ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    replaySwitch.isChecked = false // cause replay class, function play using onFinish so that all syntax in here will be run if playing is finish
                }
            }else{
                replay.pause()
            }
        }

        /*
        replay.setSpeed(1.0)           // x1
        replay.play(saved) // attention, this may be use try catch

        // replay.setSpeed(2.0)         // x2
        // replay.pause()

         */
    }
    // function here
}