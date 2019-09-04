package com.joker.koin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joker.koin.R
import com.joker.koin.model.Girl
import kotlinx.android.synthetic.main.activity_sample1.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class Simple1Activity : AppCompatActivity() {

    private val girl by inject<Girl>()
    //private lateinit var girl: Girl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample1)

        //girl = get()

        println("---->$girl")
        ivImage.setImageResource(girl.getGirl())
    }
}