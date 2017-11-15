package com.richardsmith.crisys


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_new_alarm.*
import org.jetbrains.anko.support.v4.toast


/**
 * A simple [Fragment] subclass.
 */
class NewAlarmFragment : Fragment() {

    private var alarmKey: String = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_new_alarm, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(activity).get(AlarmViewModel::class.java)

        newAlarm.setOnClickListener {
            if (alarmLocation.text.toString() != "" && alarmName.text.toString() != "" && alarmTime.text.toString() != "") {
                val aLocation = alarmLocation.text.toString()
                val aName = alarmName.text.toString()
                val aTime = alarmTime.text.toString()
                val newAlarm = Alarm(alarmKey, aName, aTime, aLocation)
                viewModel.saveAlarm(newAlarm)
                fragmentManager.popBackStack()

            } else {
                toast("Please fill out all fields.")
            }
        }

    }
}// Required empty public constructor
