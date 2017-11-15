package com.richardsmith.crisys

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations.map
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by richardsmith on 2017-11-13.
 */
data class FirebaseAlarm(val name: String = "",
                         val time: String = "",
                         val location: String = "")

data class Alarm(val key: String,
                 val name: String,
                 val time: String,
                 val location: String)

class AlarmRepository {
    companion object {
        val TAG = "AlarmRepository"
    }

    private val dataBase = FirebaseDatabase.getInstance()
    private val alarmRef = dataBase.getReference("alarms")

    private val valueEventListener = object : ValueEventListener {
        override fun onCancelled(databaseError: DatabaseError?) {
            Log.e(TAG, "Error in firebase.", databaseError?.toException())
        }

        override fun onDataChange(snapshot: DataSnapshot?) {
            val latestAlarm = snapshot?.children
                    ?.map {
                        val firebaseAlarm = it.getValue(FirebaseAlarm::class.java)
                        return@map Alarm(it.key, firebaseAlarm?.name ?: "", firebaseAlarm?.time ?: "", firebaseAlarm?.location ?: "")
                    } ?: emptyList()
            allAlarms.postValue(latestAlarm)
        }
    }


    val allAlarms: MediatorLiveData<List<Alarm>> = MediatorLiveData()

    fun startListening() {
        alarmRef.addValueEventListener(valueEventListener)
    }

    fun stopListening() {
        alarmRef.removeEventListener(valueEventListener)
    }

    fun loadAlarm(key: String): LiveData<Alarm> {
        val mediatorLiveData = MediatorLiveData<Alarm>()
        if (key != "") {
            alarmRef.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError?) {
                    Log.e(TAG, "Databas error", error?.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot?) {
                    val firebaseAlarm = snapshot?.getValue(FirebaseAlarm::class.java)
                    val alarm = Alarm(snapshot?.key ?: "",
                            firebaseAlarm?.name ?: "",
                            firebaseAlarm?.time ?: "",
                            firebaseAlarm?.location ?: "")
                    mediatorLiveData.postValue(alarm)
                }

            })
        } else {
            mediatorLiveData.postValue(Alarm("","", "", ""))
        }
        return mediatorLiveData
    }

    fun saveAlarm(alarm: Alarm) {
        val fireBaseAlarm = FirebaseAlarm(alarm.name, alarm.time, alarm.location)
        when(alarm.key) {
            "" -> alarmRef.push().setValue(fireBaseAlarm)
            else -> alarmRef.child(alarm.key).setValue(fireBaseAlarm)
        }
    }

    fun removeAlarm(alarm: Alarm) {
        if (alarm.key != "") {
            alarmRef.child(alarm.key).removeValue()
        }
    }
}