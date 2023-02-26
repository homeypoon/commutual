package com.example.commutual.model

import androidx.annotation.StringRes
import com.example.commutual.R


enum class CategoryEnum(@StringRes val categoryResourceId: Int) {
    ACADEMICS(R.string.academics),
    ART(R.string.art),
    CODING(R.string.coding),
    DEFAULT(R.string.empty_string),
    HEALTH_AND_WELLNESS(R.string.health_and_wellness),
    MUSIC(R.string.music),
    ROUTINE(R.string.routine),
    SPORTS(R.string.sports),
    WORK(R.string.work)

}


