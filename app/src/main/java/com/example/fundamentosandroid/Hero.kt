package com.example.fundamentosandroid

import kotlin.random.Random

typealias Heroes = List<Hero>

data class Hero(
    val id: String,
    val name: String,
    val photo: String,
    var hp: Int = 100
) {
    fun damage() {
        hp -= Random.nextInt(10, 60)
    }

    fun heal() {
        hp += 20
    }

    fun fullHeal() {
        hp = 100
    }

    fun isAlive() = hp > 0
}