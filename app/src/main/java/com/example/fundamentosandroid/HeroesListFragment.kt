package com.example.fundamentosandroid

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentosandroid.databinding.FragmentHeroesListBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch

interface Callback {
    var selectedHero: Hero
    fun updateHero(newHealth: Int)
    fun heroClicked(hero: Hero)
}

class HeroesListFragment: Fragment(), Callback {

    private lateinit var binding: FragmentHeroesListBinding
    private val viewModel: HeroesListViewModel by activityViewModels()
    private val adapter = HeroesListAdapter(this)

    override var selectedHero = Hero()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        setListeners()
        loadHeroesList()
    }

    override fun heroClicked(hero: Hero) {
        selectedHero = hero
        (activity as MainActivity).showHeroDetail(this)
    }

    override fun updateHero(newHealth: Int) {
        adapter.updateHero(selectedHero.id, newHealth)
    }

    // Configuramos recycler view
    private fun setAdapter() {
        binding.rvHeroesList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHeroesList.adapter = adapter
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is HeroesListViewModel.State.Normal -> { }
                    is HeroesListViewModel.State.Error ->
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    is HeroesListViewModel.State.Loaded -> {
                        adapter.update(state.heroes)
                        saveHeroesList(state.heroes)
                    }
                }
            }
        }
    }

    private fun setListeners() {
        // devuelve a todos los héroes al máximo de vida
        binding.btHealAll.setOnClickListener {
            viewModel.healAllHeroes()
        }
    }

    private fun loadHeroesList() {
        // buscamos datos en shared preferences
        val heroesListStored = getHeroesFromPreferences()

        // si no hay nada, descarga los heroes de la api
        if (heroesListStored.isEmpty()) viewModel.downloadHeroesFromApi()
        else viewModel.loadHeroesIntoList(heroesListStored)
    }

    private fun saveHeroesList(heroesList: MutableList<Hero>) {
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

        with(sharedPreferences?.edit()) {
            this?.putString(Constants.TAG_HEROES_LIST, Gson().toJson(heroesList))
            this?.commit()
        }
    }

    private fun getHeroesFromPreferences(): MutableList<Hero> {
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        val heroesListJson = sharedPreferences?.getString(Constants.TAG_HEROES_LIST, "")

        return Gson().fromJson(heroesListJson, Array<Hero>::class.java).toMutableList()
    }
}