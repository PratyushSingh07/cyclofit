package com.example.cyclofit.ui.fragment


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.cyclofit.R
import androidx.navigation.Navigation.findNavController
import com.androchef.happytimer.countdowntimer.CircularCountDownView
import com.androchef.happytimer.countdowntimer.HappyTimer
import com.example.cyclofit.databinding.FragmentHomeBinding
import com.example.cyclofit.ui.activities.SettingsActivity
import com.example.cyclofit.ui.fragment.LeaderboardFragment.Companion.list
import com.example.cyclofit.ui.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_set_time.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.lang.Math.abs
import java.net.URI.create


class HomeFragment : BaseFragment() {

    lateinit var binding : FragmentHomeBinding
    companion object{
         var timer:String=""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

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
                        dialog, id-> timer=view.setTimeText.text.toString()
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
}