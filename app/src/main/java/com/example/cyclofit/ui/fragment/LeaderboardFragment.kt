package com.example.cyclofit.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentLeaderboardBinding
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.adapter.LeaderboardAdapter
import com.example.cyclofit.ui.firestore.FirestoreClass

class LeaderboardFragment : BaseFragment() {

    private lateinit var list: ArrayList<User>
    private lateinit var binding: FragmentLeaderboardBinding
    private lateinit var firestoreClass: FirestoreClass

    companion object {
        var list = ArrayList<User>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        // binding.toolbarDashboard.inflateMenu(R.menu.leaderboard_top)
        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)

        firestoreClass = FirestoreClass()


        list = ArrayList<User>()
        list.add(User("1", "Pratyush", "aries.@gmail.com", "8.4"))
        list.add(User("2", "Ayush", "aries.@gmail.com", "7.2"))
        list.add(User("3", "Abhiram", "aries.@gmail.com", "6.8"))
        list.add(User("4", "Aditya", "aries.@gmail.com", "5.3"))
        list.add(User("5", "Samyak", "aries.@gmail.com", "5.1"))
        list.add(User("6", "Rahul", "aries.@gmail.com", "4.9"))
        list.add(User("7", "Ankur", "aries.@gmail.com", "3.0"))
        list.add(User("8", "Adiii", "aries.@gmail.com", "2.5"))
        list.add(User("9", "Prince", "aries.@gmail.com", "1.7"))
        list.add(User("10", "Ritik", "aries.@gmail.com", "0.8"))


        inflateMenuItem()

        return binding.root
    }

    fun getLeaderBoard(userList: ArrayList<User>) {

        hideProgressDialog()

        binding.rvLeaderboard.adapter = LeaderboardAdapter(requireContext(), userList)
        binding.rvLeaderboard.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        firestoreClass.getLeaderboardFragment(this)
    }

    private fun inflateMenuItem() {
        // use menu provider api to implement menu
        binding.toolbarDashboard.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Inflate the menu into the toolbar
                menuInflater.inflate(R.menu.leaderboard_top, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.refresh -> {
                        firestoreClass.leaderBoarManager { userList->
                            getLeaderBoard(userList)
                        }
                        true
                    }
                    R.id.id_menu_name -> {
                        firestoreClass.leaderBoarManager { userList->
                            val sortedArrList = ArrayList<User>()
                            userList.sortedBy { it.name.lowercase() }.forEach {
                                sortedArrList.add(it)
                            }
                            getLeaderBoard(sortedArrList)
                        }
                        true
                    }
                    R.id.id_menu_distance -> {
                        firestoreClass.leaderBoarManager { userList->
                            val sortedArrList = ArrayList<User>()
                            userList.sortedByDescending { it.distance.toDouble() }.forEach {
                                sortedArrList.add(it)
                            }
                            getLeaderBoard(sortedArrList)
                        }
                        true
                    }
                    else -> false
                }
            }
        })
    }
}