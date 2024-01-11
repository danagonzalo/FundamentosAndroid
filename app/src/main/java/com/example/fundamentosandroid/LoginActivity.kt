package com.example.fundamentosandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fundamentosandroid.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect() { state ->
                when(state) {
                    is LoginViewModel.LoginState.NotLoggedIn -> showLoading(false)
                    is LoginViewModel.LoginState.Loading -> showLoading(true)
                    is LoginViewModel.LoginState.Success -> {
                        showLoading(true)
                        Constants.token = state.token
                        goToHeroesList()
                    }
                    is LoginViewModel.LoginState.Error -> {
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            btLogin.setOnClickListener {
                viewModel.login(etUser.text.toString(), etPassword.text.toString())
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun goToHeroesList() {
        MainActivity.start(this)
        finish()
    }
}