package com.example.cyclofit.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentLeaderboardBinding
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.adapter.LeaderboardAdapter
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.utils.Tools
import kotlin.collections.ArrayList

class LeaderboardFragment : BaseFragment() {

    private lateinit var list: ArrayList<User>
    private lateinit var binding: FragmentLeaderboardBinding
    private lateinit var filteredLeaderBoardList: ArrayList<User>

    //    private lateinit var leaderBoardUserList:kotlin.collections.List<User>
    private lateinit var leaderBoardUserList: ArrayList<User>
    private lateinit var firestoreClass: FirestoreClass

    companion object {
        var list = ArrayList<User>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        binding.toolbarDashboard.inflateMenu(R.menu.leaderboard_top)
        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)
        leaderBoardUserList = kotlin.collections.ArrayList<User>()
        //LeaderBoardSearchFunctionality
        binding.searchBox.clearFocus()
        binding.searchBox.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterLeaderBoardList(newText)
                return true
            }

        })
        firestoreClass = FirestoreClass()

        inflateMenuItem()

//         list=ArrayList<User>()
//        list.add(User("1","Pratyush","aries.@gmail.com","8.4"))
//        list.add(User("2","Ayush","aries.@gmail.com","7.2"))
//        list.add(User("3","Abhiram","aries.@gmail.com","6.8"))
//        list.add(User("4","Aditya","aries.@gmail.com","5.3"))
//        list.add(User("5","Samyak","aries.@gmail.com","5.1"))
//        list.add(User("6","Rahul","aries.@gmail.com","4.9"))
//        list.add(User("7","Ankur","aries.@gmail.com","3.0"))
//        list.add(User("8","Adiii","aries.@gmail.com","2.5"))
//        list.add(User("9","Prince","aries.@gmail.com","1.7"))
//        list.add(User("10","Ritik","aries.@gmail.com","0.8"))


        return binding.root
    }

    private fun filterLeaderBoardList(newText: String?) {
        filteredLeaderBoardList = ArrayList<User>()
        for (item in leaderBoardUserList) {
            if (item.name.lowercase().contains(newText!!.lowercase())) {
                filteredLeaderBoardList.add(item)
            }
        }
        if (filteredLeaderBoardList.isEmpty()) {
            showErrorSnackBar("No User Found", true)
        } else {
            binding.rvLeaderboard.adapter =
                LeaderboardAdapter(requireContext(), filteredLeaderBoardList)
        }
    }

    fun getLeaderBoard(userList: kotlin.collections.ArrayList<User>) {

//        leaderBoardUserList = userList.sortedByDescending {
//            it.distance.toDouble() }.mapIndexed { index, item -> item.copy(rank = index + 1) }

        leaderBoardUserList = Tools.convertListToArrayList(
            userList.sortedByDescending { it.distance.toDouble() }
                .mapIndexed { index, item -> item.copy(rank = index + 1) }
        )


        Log.d("sorteddata", "$leaderBoardUserList")
        hideProgressDialog()
        binding.rvLeaderboard.adapter = LeaderboardAdapter(requireContext(), leaderBoardUserList)
        binding.rvLeaderboard.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    fun getUserList(userList: ArrayList<User>) {

        //filter list issue pratyush singh is at bottom with 7.9km should at pos 2
        getTopUser(userList)
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        firestoreClass.getLeaderboardFragment(this)
    }

    private fun getTopUser(userList: ArrayList<User>) {
        binding.nameOfUser.text = leaderBoardUserList.first().name
        binding.emailOfUser.text = leaderBoardUserList.first().email
        binding.distanceCovered.text = leaderBoardUserList.first().distance + " km"
    }

    private fun inflateMenuItem() {
        // use menu provider api to implement menu
        binding.toolbarDashboard.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Inflate the menu into the toolbar
                // menuInflater.inflate(R.menu.leaderboard_top, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.refresh -> {
                        // refresh leaderboard. this rearrange board to the default way
                        showProgressDialog()
                        firestoreClass.leaderBoardManager { userList ->
                            getLeaderBoard(userList)
                            hideProgressDialog()
                        }
                        true
                    }
                    R.id.sort -> {
                        Tools.popUpWindow(
                            context = requireContext(),
                            fragmentView = binding.root,
                            R.layout.sort_leader_layout
                        ) { view, alertDialog ->
                            var boolean = true

                            val sortName = view.findViewById<CheckBox>(R.id.id_tv_sort_by_name)
                            val sortDistance =
                                view.findViewById<CheckBox>(R.id.id_tv_sort_by_distance)
                            val checkAscending =
                                view.findViewById<CheckBox>(R.id.id_tv_sort_ascending)
                            val checkDescending =
                                view.findViewById<CheckBox>(R.id.id_tv_sort_descending)
                            val btnSortList =
                                view.findViewById<Button>(R.id.id_btn_sort_list)

                            // check soring method ascending
                            sortName.setOnClickListener {
                                if (sortName.isChecked) {
                                    sortDistance.isChecked = false
                                    checkAscending.text = "from A to Z"
                                    checkDescending.text = "from Z to A"
                                }
                            }
                            // check sorting method descending
                            sortDistance.setOnClickListener {
                                if (sortDistance.isChecked) {
                                    sortName.isChecked = false
                                    checkAscending.text = "from highest"
                                    checkDescending.text = "from lowest"
                                }
                            }

                            // check soring method ascending
                            checkAscending.setOnClickListener {
                                if (checkAscending.isChecked) {
                                    checkDescending.isChecked = false
                                    boolean = true
                                }
                            }

                            // check sorting method descending
                            checkDescending.setOnClickListener {
                                if (checkDescending.isChecked) {
                                    checkAscending.isChecked = false
                                    boolean = false
                                }
                            }

                            // sort button
                            btnSortList.setOnClickListener {
                                alertDialog.dismiss()
                                showProgressDialog()
                                if (sortName.isChecked) {
                                    sortByName(boolean)
                                } else if (sortDistance.isChecked) {
                                    sortByDistance(boolean)
                                }
                            }

                        }
                        true
                    }
                    else -> false
                }
            }
        })
    }

    private fun sortByDistance(ascending: Boolean = true) {
        // rearrange the board by distance (highest to lowest)
        // and then notify the adapter of data changed
        firestoreClass.leaderBoardManager { userList ->
            leaderBoardUserList.clear()
            // choose ways to sort user list highest or lowest
            val ul = when (ascending) {
                true -> userList.sortedByDescending { it.distance.toDouble() }
                false -> userList.sortedBy { it.distance.toDouble() }
            }
            ul.mapIndexed { index, item -> item.copy(rank = index + 1) }
                .forEach {
                    leaderBoardUserList.add(it)
                }
            hideProgressDialog()
            binding.rvLeaderboard.adapter?.notifyDataSetChanged()
        }
    }

    private fun sortByName(ascending: Boolean = true) {
        // rearrange the board alphabetically (A-Z)
        // and then notify the adapter of data changed
        firestoreClass.leaderBoardManager { userList ->
            leaderBoardUserList.clear()
            // choose ways to sort user list ascending or descending
            val ul = when (ascending) {
                true -> userList.sortedBy { it.name.lowercase() }
                false -> userList.sortedByDescending { it.name.lowercase() }
            }

            // map user to  their index
            ul.mapIndexed { index, item -> item.copy(rank = index + 1) }
                .forEach {
                    leaderBoardUserList.add(it)
                }
            hideProgressDialog()
            binding.rvLeaderboard.adapter?.notifyDataSetChanged()
        }
    }
}