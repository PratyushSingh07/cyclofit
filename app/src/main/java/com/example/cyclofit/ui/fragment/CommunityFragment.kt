package com.example.cyclofit.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentCommunityBinding
import com.example.cyclofit.model.Post
import com.example.cyclofit.ui.activities.CreateCommunityActivity
import com.example.cyclofit.ui.activities.PostActivity
import com.example.cyclofit.ui.adapter.CommunityListAdapter
import com.example.cyclofit.ui.adapter.AllPostAdapter
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.utils.Constants


class CommunityFragment : BaseFragment() {

    lateinit var binding : FragmentCommunityBinding
    private var filter : String = "abcd"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater,container,false)

        setHasOptionsMenu(true)

        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)


        binding.fab.setOnClickListener{
            val intent = Intent(requireContext(),PostActivity::class.java)
            intent.putExtra(Constants.PUT_EXTRA,filter)
            requireContext().startActivity(intent)
        }
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.create->{
                val intent  =Intent(requireContext(),CreateCommunityActivity::class.java)
                requireContext().startActivity(intent)
            }
            R.id.join->{
                Toast.makeText(activity,"Join Clicked",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getAllPost() {
        showProgressDialog()
        FirestoreClass().getAllPost(this,filter)
    }

    fun getAllPostSuccess(list: ArrayList<Post>) {

        hideProgressDialog()

        binding.rvCommunityMember.adapter = AllPostAdapter(requireContext(),list)
    }

    private fun getCommunityList(){

        FirestoreClass().getCommunityList(this)
    }

    fun getCommunityListSuccess(list: ArrayList<String>){

        binding.rvCommunityName.adapter = CommunityListAdapter(this,list)
        binding.rvCommunityName.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        binding.toolbarDashboard.inflateMenu(R.menu.commuity_top)
        binding.toolbarDashboard.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        getAllPost()
        getCommunityList()
        super.onResume()
    }

    fun postFilter(model: String) {
        filter = model
        onResume()
    }
}