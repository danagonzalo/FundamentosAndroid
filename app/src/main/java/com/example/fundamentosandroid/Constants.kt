package com.example.fundamentosandroid

class Constants {
    companion object {
        var token: String = ""

        val baseUrl = "https://dragonball.keepcoding.education/api"
        val loginUrl = "${baseUrl}/auth/login"
        val heroesUrl = "${baseUrl}/heros/all"

        val TAG_HEROES_LIST = "TAG_HEROES_LIST"
    }
}