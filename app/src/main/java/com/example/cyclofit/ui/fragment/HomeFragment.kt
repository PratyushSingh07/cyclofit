package com.example.cyclofit.ui.fragment


import android.content.Context
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
import com.example.cyclofit.ui.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {

    lateinit var binding : FragmentHomeBinding

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
        binding.circularCountDownView.strokeColorBackground = ContextCompat.getColor(requireActivity(), R.color.purple_200)
        binding.circularCountDownView.strokeColorForeground = ContextCompat.getColor(requireActivity(), R.color.purple_200)
        binding.circularCountDownView.timerTextColor = ContextCompat.getColor(requireActivity(), R.color.purple_200)
        binding.circularCountDownView.timerTextIsBold = true
        binding.circularCountDownView.timerTextSize = 13f //this will automatically converted to sp value.

        //Initialize Your Timer with seconds
        binding.circularCountDownView.initTimer(60)

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

        //Call these functions to perform actions
        //Start Timer
        binding.circularCountDownView.setOnClickListener{

            binding.circularCountDownView.startTimer()
        }

        //Pause Timer
        binding.circularCountDownView.pauseTimer()

        //Resume Timer
//        circularCountDownView.resumeTimer()
//
//        //Stop Timer
//        circularCountDownView.stopTimer()
//
//        //Reset Timer
//        circularCountDownView.resetTimer()
//
//        //get Total Seconds
//        val totalSeconds = circularCountDownView.getTotalSeconds()

        return binding.root

    }
}