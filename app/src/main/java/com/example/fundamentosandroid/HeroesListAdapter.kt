package com.example.fundamentosandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundamentosandroid.databinding.ItemHeroListBinding

class HeroesListAdapter: RecyclerView.Adapter<HeroesListAdapter.HeroesListViewHolder>() {

    private var heroesList: Heroes = emptyList()
    class HeroesListViewHolder(private val binding: ItemHeroListBinding): RecyclerView.ViewHolder(binding.root) {
        fun showItem(hero: Hero) {
            with(binding) {
                tvHeroName.text = hero.name
                tvHealthText.text = "${hero.hp}/100 HP"
                pbHealth.progress = hero.hp

                Glide
                    .with(root)
                    .load(hero.photo)
                    .centerCrop()
                    .into(ivPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroesListViewHolder {
        return HeroesListViewHolder(
            ItemHeroListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = heroesList.size

    override fun onBindViewHolder(holder: HeroesListViewHolder, position: Int) {
        holder.showItem(heroesList[position])
    }

    fun update(heroesList: Heroes) {
        this.heroesList = heroesList
        notifyDataSetChanged()
    }

}