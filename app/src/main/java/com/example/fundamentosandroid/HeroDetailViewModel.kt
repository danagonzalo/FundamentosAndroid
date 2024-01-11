package com.example.fundamentosandroid

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HeroDetailViewModel(): ViewModel() {

    private val _uiState = MutableStateFlow<HeroDetailViewModel.State>(HeroDetailViewModel.State.Normal)
    val uiState: StateFlow<HeroDetailViewModel.State> = _uiState

    sealed class State {
        data object Normal: State()
        data class Error(val message: String): State()
        data class HeroChanged(val newHealth: Int): State()
        data class HeroDied(val newHealth: Int = 0): State()
    }

    fun heal(hero: Hero) {
        hero.heal()
        _uiState.value = State.HeroChanged(hero.hp)
        _uiState.value = State.Normal
    }

    fun damage(hero: Hero) {
        hero.damage()
        _uiState.value = if (hero.isDead) State.HeroDied() else State.HeroChanged(hero.hp)
        _uiState.value = State.Normal
    }

    fun fullHeal(hero: Hero) {
        hero.fullHeal()
        _uiState.value = State.HeroChanged(hero.hp)
        _uiState.value = State.Normal
    }
}