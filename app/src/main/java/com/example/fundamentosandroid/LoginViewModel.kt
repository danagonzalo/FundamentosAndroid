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

    private val _uiState = MutableStateFlow<LoginState>(LoginState.NotLoggedIn)
    val uiState: StateFlow<LoginState> = _uiState

    sealed class LoginState {
        data object NotLoggedIn: LoginState()
        data object Loading: LoginState()
        data class Success(val token: String): LoginState()
        data class Error(val message: String): LoginState()
    }

    fun login(user: String, password: String) {
        if (userIsValid(user) && passwordIsValid(password)) {
            viewModelScope.launch(Dispatchers.IO) {
                val url = Constants.loginUrl

                //val credentials = Credentials.basic(user, password)
                val credentials = Credentials.basic("damdgonzalo@gmail.com", "123456")
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
                        LoginState.Success(it.string())
                    } ?: LoginState.Error("Token vacío")
                } else _uiState.value = LoginState.Error(response.message)
            }
        }
    }

    private fun userIsValid(user: String): Boolean {
        return if (user.isBlank()) {
            _uiState.value = LoginState.Error("Introduce un usuario")
            false
        } else true
    }

    private fun passwordIsValid(password: String): Boolean {
        return if (password.isBlank()) {
            _uiState.value = LoginState.Error("Introduce una contraseña")
            false
        } else true
    }
}