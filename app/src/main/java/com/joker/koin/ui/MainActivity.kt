package com.joker.koin.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joker.koin.R
import com.joker.koin.model.Girl
import com.joker.koin.model.ScopeEntity
import com.joker.koin.ui.scope.Scope1Activity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class MainActivity : AppCompatActivity() {

    private val girl1 by inject<Girl>(named("girl1")) { parametersOf("可爱") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sample1.setOnClickListener {
            startActivity(Intent(this, Simple1Activity::class.java))
        }
        sample2.setOnClickListener {
            startActivity(Intent(this, Simple2Activity::class.java))
        }
        scope.setOnClickListener {
            startActivityForResult(Intent(this, Scope1Activity::class.java), 120)
        }

        println("---->$girl1")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //验证 scope销毁 延时是为了保证 Scope1Activity 已经执行 onDestory
        scope.postDelayed({
            scope.text = try {
                getKoin().getScope("scope1").get<ScopeEntity>().text
            } catch (e: Exception) {
                "scope back"
            }
        }, 3000)
    }
}
