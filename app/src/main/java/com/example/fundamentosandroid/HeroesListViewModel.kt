package com.example.fundamentosandroid

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HeroesListViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<HeroesListState>(HeroesListState.Loading)
    val uiState: StateFlow<HeroesListState> = _uiState

    sealed class HeroesListState {
        data object Loading: HeroesListState()
        data class Loaded(val heroes: Heroes): HeroesListState()
    }

}