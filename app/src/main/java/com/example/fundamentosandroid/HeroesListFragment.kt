package com.example.fundamentosandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fundamentosandroid.databinding.FragmentHeroesListBinding

class HeroesListFragment : Fragment() {

    private lateinit var binding: FragmentHeroesListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroesListBinding.inflate(inflater)
        return binding.root
    }
}