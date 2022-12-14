package com.example.cyclofit.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cyclofit.databinding.ItemLeaderboardBinding
import android.content.Context
import com.example.cyclofit.model.User

class FilteredLeaderBoardAdapter(val context: Context, private var list: ArrayList<User>) :
    RecyclerView.Adapter<FilteredLeaderBoardAdapter.LeaderViewHolder>() {
    inner class LeaderViewHolder(val binding: ItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderViewHolder {
        return LeaderViewHolder(
            ItemLeaderboardBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LeaderViewHolder, position: Int) {
        val model = list[position]
        holder.binding.positionOfUser.text = (position+1).toString()
        holder.binding.nameOfUser.text = model.name
        holder.binding.emailOfUser.text = model.email
        holder.binding.distanceCovered.text = model.distance
    }

    override fun getItemCount(): Int {
        return list.size
    }

//    fun setLeaderBoardQuery(filteredList:ArrayList<User>){
//        this.list = filteredList
//        notifyDataSetChanged()
//    }

}