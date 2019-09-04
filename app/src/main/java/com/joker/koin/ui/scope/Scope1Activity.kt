package com.joker.koin.ui.scope

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joker.koin.R
import com.joker.koin.model.ScopeEntity
import kotlinx.android.synthetic.main.activity_scope1.*
import org.koin.android.ext.android.getKoin
import org.koin.androidx.scope.bindScope
import org.koin.core.qualifier.named

class Scope1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scope1)
        
        //创建 scope 需要指定 id 和 qualifier，getScope 需要id
        val scope1 = getKoin().createScope("scope1", named("scope"))
        //默认绑定onDestory
        bindScope(scope1)

        scope1.get<ScopeEntity>().text = "Scope1Activity"

        tvText.text = scope1.get<ScopeEntity>().text

        btn.setOnClickListener {
            startActivity(Intent(this, Scope2Activity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        tvText.text = getKoin().getScope("scope1").get<ScopeEntity>().text
    }
}