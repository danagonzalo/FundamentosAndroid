package com.example.fundamentosandroid

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HeroDetailViewModel(): ViewModel() {

    private val _uiState = MutableStateFlow<HeroDetailViewModel.HeroDetailState>(HeroDetailViewModel.HeroDetailState.Loading)
    val uiState: StateFlow<HeroDetailViewModel.HeroDetailState> = _uiState

    sealed class HeroDetailState {
        data object Loading: HeroDetailState()
        data class Error(val message: String): HeroDetailState()
        data class HeroChanged(var hero: Hero, val newHealth: Int): HeroDetailState()

        data object HeroDied: HeroDetailState()
    }

    fun heal(hero: Hero) {
        hero.heal()
        if (hero.hp > 100) hero.hp = 100

        _uiState.value = HeroDetailState.HeroChanged(hero, hero.hp)
    }

    fun damage(hero: Hero) {
        hero.damage()
        if (hero.hp < 0) {
            hero.hp = 0
            hero.isDead = true

            _uiState.value = HeroDetailState.HeroDied
        }
        else _uiState.value = HeroDetailState.HeroChanged(hero, hero.hp)
    }

    fun fullHeal(hero: Hero) {
        hero.fullHeal()
        _uiState.value = HeroDetailState.HeroChanged(hero, hero.hp)
    }
}