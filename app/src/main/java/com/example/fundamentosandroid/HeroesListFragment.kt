package com.example.fundamentosandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentosandroid.databinding.FragmentHeroesListBinding
import kotlinx.coroutines.launch

interface Callback {
    var selectedHero: Hero?
    fun updateHero(newHealth: Int)
    fun heroClicked(hero: Hero)
}

class HeroesListFragment: Fragment(), Callback {

    private lateinit var binding: FragmentHeroesListBinding
    private val viewModel: HeroesListViewModel by activityViewModels()
    private val adapter = HeroesListAdapter(this)

    override var selectedHero: Hero? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadHeroes()
        setAdapter()
        setObservers()
        setListeners()
    }

    override fun heroClicked(hero: Hero) {
        selectedHero = hero
        Log.i("AWUUU", "HERO CLICKED!!!!! ${selectedHero?.name}")

        (activity as MainActivity).showHeroDetail(selectedHero!!, this)
    }

    override fun updateHero(newHealth: Int) {
        Log.i("AWUUU", "UPDATING HERO HEALTH..... ${selectedHero?.name} - $newHealth")
        adapter.updateHero(selectedHero!!, newHealth)
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
                    is HeroesListViewModel.State.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    is HeroesListViewModel.State.Loaded -> {
                        adapter.update(state.heroes, state.forceUpdateList)
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.btHealAll.setOnClickListener {
            viewModel.healAllHeroes()
        }
    }
}