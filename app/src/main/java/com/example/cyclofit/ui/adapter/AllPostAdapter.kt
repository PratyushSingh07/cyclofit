package com.example.cyclofit.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cyclofit.databinding.UserCommunityMemberListBinding
import com.example.cyclofit.model.Post
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.utils.GlideLoader

class AllPostAdapter(
    val context: Context,
    private val list: ArrayList<Post>
) : RecyclerView.Adapter<AllPostAdapter.MessageUserViewHolder>() {


    inner class MessageUserViewHolder(val binding: UserCommunityMemberListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(
            UserCommunityMemberListBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvPostText.text = model.details
        holder.binding.tvPostUser.text = model.name

        GlideLoader(context).loadUserPicture(model.image,holder.binding.ivPostImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }


}