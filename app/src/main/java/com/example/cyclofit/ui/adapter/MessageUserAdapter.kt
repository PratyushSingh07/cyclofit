package com.example.cyclofit.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cyclofit.databinding.UserCommunityMemberListBinding
import com.example.cyclofit.ui.User

class MessageUserAdapter(
    val context: Context, private val list: ArrayList<User>
) : RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {


    inner class MessageUserViewHolder(val binding: UserCommunityMemberListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(UserCommunityMemberListBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvCommunityMemberName.text = model.name
    }

    override fun getItemCount(): Int {
        return list.size
    }


}