package com.example.commutual.model

import androidx.annotation.StringRes
import com.example.commutual.R


enum class CategoryEnum(@StringRes val categoryStringRes: Int) {
    ANY(R.string.any),
    NONE(R.string.empty_string),
    ACADEMICS(R.string.academics),
    ART(R.string.art),
    CODING(R.string.coding),
    HEALTH_AND_WELLNESS(R.string.health_and_wellness),
    MUSIC(R.string.music),
    MISCELLANEOUS(R.string.miscellaneous),
    ATHLETICS(R.string.athletics),
    WORK(R.string.work)
}

enum class MessageTabEnum(@StringRes val tabStringRes: Int) {
    CHAT(R.string.chat),
    TASKS(R.string.tasks)
}

