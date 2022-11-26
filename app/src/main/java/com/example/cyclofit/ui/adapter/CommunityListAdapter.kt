package com.example.cyclofit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cyclofit.R
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.fragment.CommunityFragment
import kotlinx.android.synthetic.main.item_community_list.view.*

class CommunityListAdapter(
    val context: CommunityFragment,
    private val list: ArrayList<String>
) : RecyclerView.Adapter<CommunityListAdapter.CommunityViewHolder>() {

    class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_community_list, parent, false)
        return CommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.communityName.text = model
        holder.itemView.ll_community.setOnClickListener {
            FirestoreClass().addCurrentCommunity(context,model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}