package com.richardsmith.crisys


import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_alarm_list.*


/**
 * A simple [Fragment] subclass.
 */
class AlarmListFragment : LifecycleFragment() {
    companion object {
        val TAG = "AlarmListFragment"
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_alarm_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmList.adapter = AlarmAdapter()
        alarmList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val viewModel = ViewModelProviders.of(activity).get(AlarmViewModel::class.java)
        viewModel.allAlarms.observe(this, Observer {
            (alarmList.adapter as AlarmAdapter).alarms = it ?: emptyList()
            alarmList.adapter.notifyDataSetChanged()
        })

        newAlarm.setOnClickListener {
            val newAlarmFragment = NewAlarmFragment()
            val arguments = Bundle()
            arguments.putString("contactKey", "")
            newAlarmFragment.arguments = arguments

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, newAlarmFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    class AlarmAdapter : RecyclerView.Adapter<AlarmViewHolder>() {
        var alarms: List<Alarm> = emptyList()
        override fun onBindViewHolder(holder: AlarmViewHolder?, position: Int) {
            val alarm = alarms[position]
            holder?.alarmKey = alarm.key
            holder?.alarmName?.text = alarm.name + "  :   " + alarm.time

        }

        override fun getItemCount(): Int = alarms.size


        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AlarmViewHolder {
            val itemView =  LayoutInflater.from(parent?.context).inflate(R.layout.alarm_item, parent, false)
            return AlarmViewHolder(itemView)
        }

    }

    class AlarmViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val alarmName: TextView = itemView.findViewById(R.id.alarmName)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            val alarmDetailsFragment = AlarmDetailsFragment()
            val arguments = Bundle()
            arguments.putString("alarmKey", alarmKey)
            Log.e(TAG, alarmKey)
            alarmDetailsFragment.arguments = arguments

            (view?.context as FragmentActivity)
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, alarmDetailsFragment)
                    .addToBackStack(null)
                    .commit()
        }

        var alarmKey: String = ""
    }
}// Required empty public constructor
