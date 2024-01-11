package com.example.fundamentosandroid

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
import kotlinx.coroutines.launch

interface Callback {
    fun heroClicked(hero: Hero)
}

class HeroesListFragment: Fragment(), Callback {

    private lateinit var binding: FragmentHeroesListBinding
    private val viewModel: HeroesListViewModel by activityViewModels()
    private val adapter = HeroesListAdapter(this)

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
        viewModel.loadHeroes()
    }

    override fun heroClicked(hero: Hero) {
        (activity as MainActivity).showHeroDetail(hero)
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
                    is HeroesListViewModel.HeroesListState.Loading -> { }
                    is HeroesListViewModel.HeroesListState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    is HeroesListViewModel.HeroesListState.Loaded -> {
                        adapter.update(state.heroes)
                    }
                }
            }
        }
    }
}