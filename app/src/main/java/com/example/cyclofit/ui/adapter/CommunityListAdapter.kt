package com.example.cyclofit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cyclofit.R
import com.google.firebase.database.core.Context

class CommunityListAdapter(private val list:List<String>):RecyclerView.Adapter<CommunityListAdapter.CommunityViewHolder>(){

    class CommunityViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var communityImage=itemView.findViewById<ImageView>(R.id.communityPic)
        var communityName=itemView.findViewById<TextView>(R.id.communityName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.item_community_list,parent,false)
        return CommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        var title=list[position]
        holder.communityName.text=list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

}