package com.richardsmith.crisys


import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_alarm_details.*


/**
 * A simple [Fragment] subclass.
 */
class AlarmDetailsFragment : LifecycleFragment() {

    companion object {
        val TAG = "AlarmDetailsFragment"
    }

    private var contactKey: String = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_alarm_details, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(activity).get(AlarmViewModel::class.java)

        val aKey = arguments.getString("alarmKey")
        viewModel.loadAlarm(aKey).observe(this, Observer {
            val alarm: Alarm = it ?: Alarm("", "", "", "")
            contactKey = alarm.key

            alarmStatus.text = alarm.name
            alarmLocation.text = alarm.location
            alarmPriority.text = alarm.time

            deleteAlarm.visibility = when (aKey) {
                "" -> View.GONE
                else -> View.VISIBLE
            }

            deleteAlarm.setOnClickListener {
                viewModel.removeAlarm(alarm)

                fragmentManager.popBackStack()
            }
        })



    }
}// Required empty public constructor
