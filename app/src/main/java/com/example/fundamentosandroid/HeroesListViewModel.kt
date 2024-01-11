package com.example.fundamentosandroid

import android.util.Log
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

    private val _uiState = MutableStateFlow<HeroesListState>(HeroesListState.Loading)
    val uiState: StateFlow<HeroesListState> = _uiState

    sealed class HeroesListState {
        data object Loading: HeroesListState()
        data class Loaded(val heroes: Heroes): HeroesListState()
        data class Error(val message: String): HeroesListState()
    }

    fun loadHeroes() {
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
                val heroesList = heroesDtoList.map { hero ->
                    Hero(hero.id, hero.name, hero.photo)
                }
                _uiState.value = HeroesListState.Loaded(heroesList)
            }
            else _uiState.value = HeroesListState.Error(response.message)
        }
    }

}