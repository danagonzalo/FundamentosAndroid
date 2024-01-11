package com.example.fundamentosandroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fundamentosandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showHeroesList()
    }

    private fun showHeroesList() {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, HeroesListFragment())
            .commit()
    }

    fun showHeroDetail(hero: Hero, callback: Callback) {
        supportFragmentManager.beginTransaction()
        .replace(binding.fragmentContainer.id, HeroDetailFragment(callback))
            .addToBackStack(null)
            .commit()
    }
}