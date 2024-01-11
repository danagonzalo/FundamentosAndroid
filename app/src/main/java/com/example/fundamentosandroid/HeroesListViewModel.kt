package com.example.fundamentosandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class HeroesListViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<State>(State.Normal)
    val uiState: StateFlow<State> = _uiState
    private var heroesList = mutableListOf<Hero>()

    sealed class State {
        data object Normal: State()
        data class Loaded(val heroes: MutableList<Hero>, val forceUpdateList: Boolean = false): State()
        data class Error(val message: String): State()
    }

    fun downloadHeroesFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            val url = Constants.heroesUrl

            val formBody = FormBody.Builder()
                .add("name", "")
                .build()

            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("Authorization", "Bearer ${Constants.token}")
                .build()

            val client = OkHttpClient()
            val call = client.newCall(request)
            val response = call.execute()

            if (response.isSuccessful) {
                val heroesDtoList =  Gson().fromJson(response.body?.string(), Array<HeroDto>::class.java)
                val list = heroesDtoList.map { hero ->
                    Hero(hero.id, hero.name, hero.photo)
                }.toMutableList()
                loadHeroesIntoList(list)
            }
            else _uiState.value = State.Error(response.message)
        }
    }

    fun loadHeroesIntoList(heroes: MutableList<Hero>) {
        heroesList = heroes
        _uiState.value = State.Loaded(heroesList)
    }

    fun healAllHeroes() {
        heroesList.forEach {
            it.fullHeal()
        }

        _uiState.value = State.Loaded(heroesList, true)
    }
}