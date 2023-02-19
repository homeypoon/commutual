package com.example.commutual.model

import com.example.commutual.R

// Additional feature: Add icon for categories
sealed class Category(val name: String, val categoryResourceId: Int) {
    object Academics : Category("Academics", R.string.academics)
    object Art: Category("Art", R.string.art)
    object Coding : Category("Coding", R.string.coding)
    object HealthAndWellness : Category("Health And Wellness", R.string.health_and_wellness)
    object Music: Category("Music", R.string.music)
    object Routine : Category("Routine", R.string.routine)
    object Sports: Category("Sports", R.string.sports)
    object Work : Category("Work", R.string.work)
}