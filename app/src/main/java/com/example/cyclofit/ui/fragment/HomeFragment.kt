package com.example.cyclofit.ui.fragment


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androchef.happytimer.countdowntimer.CircularCountDownView
import com.androchef.happytimer.countdowntimer.HappyTimer
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentHomeBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.activities.SettingsActivity
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.fragment.LeaderboardFragment.Companion.list
import com.example.cyclofit.ui.utils.Constants
import com.example.cyclofit.ui.utils.GlideLoader
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnSuccessListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.dialog_set_time.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Math.abs
import java.lang.reflect.Type


class HomeFragment : BaseFragment() {

    lateinit var binding : FragmentHomeBinding
    lateinit var mUserDetails : User
    var sp = ArrayList<Shared>()
    private val client: FusedLocationProviderClient? = null

    companion object{
         var timer:String=""
        var timeList=ArrayList<String>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        val sharedPreferences1 = requireContext().getSharedPreferences(
            Constants.CYCLOFIT_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPreferences1.getString("timer",null)
        val type: Type = object : TypeToken<ArrayList<Shared?>?>() {}.type

        if(json!=null){
            sp = gson.fromJson(json, type)
        }

        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)

        val sharedPreferences = requireContext().getSharedPreferences(Constants.CYCLOFIT_PREFERENCES, Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")!!

        binding.tvHomeUserName.text = "Hi, ${userName}"

        binding.userImage.setOnClickListener {
            startActivity(Intent(context,SettingsActivity::class.java))
        }

        binding.circularCountDownView.isTimerTextShown= true
        binding.circularCountDownView.timerType = HappyTimer.Type.COUNT_DOWN
        binding.circularCountDownView.timerTextFormat = CircularCountDownView.TextFormat.HOUR_MINUTE_SECOND
        binding.circularCountDownView.strokeThicknessForeground = 10f
        binding.circularCountDownView.strokeThicknessBackground = 10f
        binding.circularCountDownView.strokeColorBackground = ContextCompat.getColor(requireActivity(), R.color.dark_green)
        binding.circularCountDownView.strokeColorForeground = ContextCompat.getColor(requireActivity(), R.color.dark_green)
        binding.circularCountDownView.timerTextColor = ContextCompat.getColor(requireActivity(), R.color.dark_green)
        binding.circularCountDownView.timerTextIsBold = true
        binding.circularCountDownView.timerTextSize = 13f //this will automatically converted to sp value.



        //set OnTickListener for getting updates on time. [Optional]
        binding.circularCountDownView.setOnTickListener(object : HappyTimer.OnTickListener {
//
//            //OnTick
            override fun onTick(completedSeconds: Int, remainingSeconds: Int) {
//
            }
//
//            //OnTimeUp
            override fun onTimeUp() {
                fetchCalsBurnt()
            }
        })

//        set OnStateChangeListener [RUNNING, FINISHED, PAUSED, RESUMED, UNKNOWN, RESET, STOPPED] [Optional]
        binding.circularCountDownView.setStateChangeListener(object : HappyTimer.OnStateChangeListener {
            override fun onStateChange(
                state: HappyTimer.State,
                completedSeconds: Int,
                remainingSeconds: Int
            ) {

            }
        })


//
//        //Stop Timer
//        circularCountDownView.stopTimer()
//
//        //Reset Timer
//        circularCountDownView.resetTimer()
//
//        //get Total Seconds
//        val totalSeconds = circularCountDownView.getTotalSeconds()
        binding.setTimer.setOnClickListener {
            val builder=AlertDialog.Builder(activity)
            val inflater=requireActivity().layoutInflater
            val view=inflater.inflate(R.layout.dialog_set_time,null)
            builder.setView(view).setTitle("Set the Time")
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
                .setPositiveButton("Proceed", DialogInterface.OnClickListener{
                        dialog, id-> timer = view.setTimeText.text.toString()
//                        timeList.add(timer)

                        saveData(timer)

                    if (ActivityCompat.checkSelfPermission(
                             requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    client?.lastLocation?.addOnSuccessListener(requireActivity(),
                        OnSuccessListener<Any?> { location ->
                            if (location != null) {
                                Toast.makeText(activity,"${location.toString()}",Toast.LENGTH_SHORT).show()
                            }
                        })

                        binding.circularCountDownView.initTimer(timer.toInt())
                        binding.circularCountDownView.setOnClickListener {
                            binding.circularCountDownView.startTimer()
                        }
                })
            if (binding.circularCountDownView.isEnabled){
                binding.pauseTimer.setOnClickListener {
                    binding.circularCountDownView.pauseTimer()
                }
            }
            binding.resumeTimer.setOnClickListener {
                binding.circularCountDownView.resumeTimer()
            }
            val alert=builder.create()
            alert.show()

        }
        var id=0;
        // search ur name in the list
        for (i in list){
            if (i.name.equals(userName)){
                id=i.id.toInt()
                break
            }
        }
        // if the name is found
        if(id!=0){
            println(id)
            binding.distanceToBeCovered.setText(abs(list[id-2].distance.toDouble()-list[id-1].distance.toDouble()).toString())
        }
        return binding.root
    }
    private fun fetchCalsBurnt(){
        // Duration of physical activity in minutes × (MET × 3.5 × your weight in kg) / 200 =calories burned per minute
        var durationOfActivity=0;
        if (!timer.isEmpty())
            durationOfActivity= timer.toInt()//in minutes
        val MET=12 // for bicycles
        val wt=60 // in kg
        var totalCalsBurnt=0.0;
        totalCalsBurnt=durationOfActivity*(MET*3.5*wt)
        totalCalsBurnt/=200*1000;

        binding.cvOfKcal.setText(totalCalsBurnt.toString())
    }
    override fun onResume() {
        super.onResume()
        showProgressDialog()
        FirestoreClass().getUserDetails(this)
//        FirestoreClass().allDistance(this)
    }

    fun userDetailsSuccess(user: User) {
        hideProgressDialog()

        GlideLoader(requireContext()).loadUserPicture(user.image,binding.userImage)
    }

    fun getDistanceSuccess(list: ArrayList<String>) {
        tv_current_distance.text = list[list.size-2]
    }

    private fun saveData(timer: String) {
        val sharedPreferences = requireContext().getSharedPreferences(Constants.CYCLOFIT_PREFERENCES,Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()

        val gson = Gson()
        sp.add(Shared(timer))
        val json = gson.toJson(sp)
        editor.putString("timer",json)
        editor.apply()
    }

}