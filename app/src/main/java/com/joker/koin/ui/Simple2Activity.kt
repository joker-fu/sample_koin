package com.joker.koin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joker.koin.R
import com.joker.koin.model.Girl
import kotlinx.android.synthetic.main.activity_sample1.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class Simple2Activity : AppCompatActivity() {

    private val girl1 by inject<Girl>(named("girl1")) { parametersOf("可爱") }
    private val girl2 by inject<Girl>(named("girl2")) { parametersOf("性感") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample1)

        ivImage.setImageResource(girl1.getGirl())

        ivImage.postDelayed({
            ivImage.setImageResource(girl2.getGirl())
        }, 3000)
    }
}