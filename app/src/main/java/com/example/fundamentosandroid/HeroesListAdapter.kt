package com.example.fundamentosandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundamentosandroid.databinding.ItemHeroListBinding

class HeroesListAdapter(private val callback: Callback): RecyclerView.Adapter<HeroesListAdapter.HeroesListViewHolder>() {

    private var heroesList: Heroes = emptyList<Hero>().toMutableList()
    class HeroesListViewHolder(private val binding: ItemHeroListBinding, val callback: Callback): RecyclerView.ViewHolder(binding.root) {

        fun showItem(hero: Hero) {
            with(binding) {
                tvHeroName.text = hero.name
                tvHealthText.text = "${hero.hp}/100 HP"
                pbHealth.progress = hero.hp

                Glide
                    .with(root)
                    .load(hero.photo)
                    .centerCrop()
                    .into(ivHeroPhoto)

                if(hero.isDead) {
                    root.isEnabled = false
                    constraintLayout.setBackgroundColor(ContextCompat.getColor(root.context, R.color.gray))
                    ivHeroPhoto.setColorFilter(R.color.gray)
                } else {
                    root.isEnabled = true
                    constraintLayout.setBackgroundColor(ContextCompat.getColor(root.context, R.color.white))
                    ivHeroPhoto.clearColorFilter()
                }

                root.setOnClickListener {
                    callback.heroClicked(hero)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroesListViewHolder {
        return HeroesListViewHolder(
            binding = ItemHeroListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            callback = callback
        )
    }

    override fun getItemCount(): Int = heroesList.size

    override fun onBindViewHolder(holder: HeroesListViewHolder, position: Int) {
        holder.showItem(heroesList[position])
    }

    fun update(heroesList: Heroes, isForceUpdate: Boolean) {
        if (this.heroesList.isEmpty() || isForceUpdate) {
            this.heroesList = heroesList
            notifyDataSetChanged()
        }
    }

    fun updateHero(hero: Hero, newHealth: Int) {
        heroesList.forEachIndexed { index, item ->
            if (item.id == hero.id) {
                item.hp = newHealth
                notifyItemChanged(index)
            }
        }
    }
}