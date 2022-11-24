package com.example.cyclofit.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentCommunityBinding
import com.example.cyclofit.model.Post
import com.example.cyclofit.ui.adapter.CommunityListAdapter
import com.example.cyclofit.ui.adapter.AllPostAdapter
import com.example.cyclofit.ui.firestore.FirestoreClass


class CommunityFragment : BaseFragment() {

    lateinit var binding : FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater,container,false)

        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)

        binding.fabNewCommunity.setOnClickListener {
            val createFragment = CreateCommunityFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_dashboard,createFragment)
                .commit()
        }

        getAllPost()

        getCommunityList()

        binding.fab.setOnClickListener{
            val postFragment=PostFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_dashboard,postFragment)
                .commit()
        }
        binding.toolbarDashboard.inflateMenu(R.menu.commuity_top)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.create->{
                Toast.makeText(activity,"Community Clicked",Toast.LENGTH_SHORT).show()
            }
            R.id.join->{
                Toast.makeText(activity,"Join Clicked",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getAllPost() {
        showProgressDialog()
        FirestoreClass().getAllPost(this)
    }

    fun getAllPostSuccess(list: ArrayList<Post>) {

        hideProgressDialog()

        binding.rvCommunityMember.adapter = AllPostAdapter(requireContext(),list)
    }

    private fun getCommunityList(){

        FirestoreClass().getCommunityList(this)
    }

    fun getCommunityListSuccess(list: ArrayList<String>){

        binding.rvCommunityName.adapter = CommunityListAdapter(list)
        binding.rvCommunityName.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

    }
}