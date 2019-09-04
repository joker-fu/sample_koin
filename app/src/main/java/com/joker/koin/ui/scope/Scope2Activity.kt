package com.joker.koin.ui.scope

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joker.koin.R
import com.joker.koin.model.ScopeEntity
import kotlinx.android.synthetic.main.activity_scope1.*
import org.koin.android.ext.android.getKoin

class Scope2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scope1)

        val scope1 = getKoin().getScope("scope1")

        scope1.get<ScopeEntity>().text = "Scope2Activity"

        tvText.text = scope1.get<ScopeEntity>().text

    }
}