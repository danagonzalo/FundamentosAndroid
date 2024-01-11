package com.example.fundamentosandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<State>(State.NotLoggedIn)
    val uiState: StateFlow<State> = _uiState

    sealed class State {
        data object NotLoggedIn: State()
        data object Loading: State()
        data class Success(val token: String): State()
        data class Error(val message: String): State()
    }

    fun login(user: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = State.Loading

            val url = Constants.loginUrl

            val credentials = Credentials.basic(user, password)
            //val credentials = Credentials.basic("damdgonzalo@gmail.com", "123456")
            val formBody = FormBody.Builder().build()

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", credentials)
                .post(formBody)
                .build()

            val client = OkHttpClient()
            val call = client.newCall(request)
            val response = call.execute()

            if (response.isSuccessful) {
                _uiState.value = response.body?.let {
                    State.Success(it.string())
                } ?: State.Error("Token vacío")
            } else _uiState.value = State.Error(response.message)
        }

    }

    /*private fun userIsValid(user: String): Boolean {
        return if (user.isBlank()) {
            _uiState.value = LoginState.InvalidCredentials
            false
        } else true
    }

    private fun passwordIsValid(password: String): Boolean {
        return if (password.isBlank()) {
            _uiState.value = LoginState.InvalidCredentials
            false
        } else true
    }*/
}