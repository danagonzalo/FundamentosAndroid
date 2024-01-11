package com.example.fundamentosandroid

import android.util.Log
import kotlin.random.Random

typealias Heroes = MutableList<Hero>

data class Hero(
    val id: String,
    val name: String,
    val photo: String,
    var hp: Int = 100,
    var isDead: Boolean = false
) {
    fun damage() {
        hp -= Random.nextInt(10, 60)
        if (hp < 0)  {
            hp = 0
            isDead = true
        }
    }

    fun heal() {
        hp += 20
        if (hp > 100) hp = 100

    }

    fun fullHeal() {
        hp = 100
        isDead = false
    }
}