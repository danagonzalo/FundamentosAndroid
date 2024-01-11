package com.example.fundamentosandroid

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
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

class HeroDetailFragment(private val callback: Callback) : Fragment() {

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
        Log.i("AWUUU", "HERO  SELECTED IN DETAIL ${callback.selectedHero.name}")
        showHero(callback.selectedHero)
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state) {
                    is HeroDetailViewModel.State.Normal -> { }
                    is HeroDetailViewModel.State.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    is HeroDetailViewModel.State.HeroChanged -> {
                        callback.updateHero(state.newHealth)
                        updateHealth(state.newHealth)
                    }
                    is HeroDetailViewModel.State.HeroDied -> {
                        callback.updateHero(state.newHealth)
                        // vuelve a la lista de héroes
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
                .placeholder(R.drawable.baseline_person_24)
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
            if (callback.selectedHero.hp == 100)
                Toast.makeText(context, "¡${callback.selectedHero.name} está a tope!", Toast.LENGTH_LONG).show()
            else viewModel.heal(callback.selectedHero)
        }

        binding.btDamage.setOnClickListener {
            viewModel.damage(callback.selectedHero)
        }
    }
}