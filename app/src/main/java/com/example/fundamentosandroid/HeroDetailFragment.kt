package com.example.fundamentosandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.fundamentosandroid.databinding.FragmentHeroDetailBinding
import kotlinx.coroutines.launch

class HeroDetailFragment(private var heroSelected: Hero) : Fragment() {

    private lateinit var binding: FragmentHeroDetailBinding
    private val viewModel: HeroDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showHero(heroSelected)
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state) {
                    is HeroDetailViewModel.HeroDetailState.Loading -> {}
                    is HeroDetailViewModel.HeroDetailState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    is HeroDetailViewModel.HeroDetailState.HeroChanged -> {
                        heroSelected = state.hero
                        updateHealth(state.newHealth)
                    }
                    is HeroDetailViewModel.HeroDetailState.HeroDied -> {
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }
    }

    private fun showHero(hero: Hero) {
        with(binding) {
            tvHeroName.text = hero.name
            tvHeroHealthText.text = "${hero.hp}/100 HP"
            pbHeroHealth.progress = hero.hp

            Glide
                .with(root)
                .load(hero.photo)
                .centerCrop()
                .into(ivHeroPhoto)
        }
    }

    private fun updateHealth(newHealth: Int) {
        binding.tvHeroHealthText.text = "${newHealth}/100 HP"
        binding.pbHeroHealth.progress = newHealth
    }

    private fun setListeners() {
        binding.btHeal.setOnClickListener {
            viewModel.heal(heroSelected)
        }

        binding.btDamage.setOnClickListener {
            viewModel.damage(heroSelected)
        }
    }
}