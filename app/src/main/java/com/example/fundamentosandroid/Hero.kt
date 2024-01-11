package com.example.fundamentosandroid

import android.util.Log
import kotlin.random.Random

typealias Heroes = List<Hero>

data class Hero(
    val id: String,
    val name: String,
    val photo: String,
    var hp: Int = 100,
    var isDead: Boolean = false
) {
    fun damage() {
        Log.i("AWUUU","HERO DAMAGED ${name}")

        hp -= Random.nextInt(10, 60)
    }

    fun heal() {
        Log.i("AWUUU","HERO HEAL ${name}")

        hp += 20
    }

    fun fullHeal() {
        hp = 100
    }
}