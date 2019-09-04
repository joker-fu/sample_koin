package com.joker.koin.model

import com.joker.koin.R

class Girl {

    var type: String = ""

    fun getGirl(): Int {
        return when (type) {
            "可爱" -> R.drawable.keai
            "性感" -> R.drawable.xinggan
            else -> R.drawable.girl
        }
    }

}