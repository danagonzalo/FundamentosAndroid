package com.example.fundamentosandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
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
        setViews()
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect() { state ->
                when(state) {
                    is LoginViewModel.State.NotLoggedIn -> showLoading(false)
                    is LoginViewModel.State.Loading -> showLoading(true)
                    is LoginViewModel.State.Success -> {
                        Constants.token = state.token // guardamos el token
                        goToHeroesList()
                    }
                    is LoginViewModel.State.Error -> {
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun setViews() {
        with(binding) {
            btLogin.isEnabled = false

            etUser.doAfterTextChanged {
                btLogin.isEnabled = etUser.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()
            }

            etPassword.doAfterTextChanged {
                btLogin.isEnabled = etUser.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()
            }
        }
    }
    private fun setListeners() {
        with(binding) {
            btLogin.setOnClickListener {
                showLoading(true)
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