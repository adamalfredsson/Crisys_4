package com.richardsmith.crisys

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData

/**
 * Created by richardsmith on 2017-11-13.
 */



class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        val TAG = "AlarmViewModel"
    }

    private val alarmRepository = AlarmRepository()
    val allAlarms = alarmRepository.allAlarms

    init {
        alarmRepository.startListening()
    }

    override fun onCleared() {
        super.onCleared()
        alarmRepository.stopListening()
    }

    fun loadAlarm(key: String): LiveData<Alarm> {
        return alarmRepository.loadAlarm(key)
    }

    fun saveAlarm(alarm: Alarm) {
        alarmRepository.saveAlarm(alarm)
    }

    fun removeAlarm(alarm: Alarm) {
        alarmRepository.removeAlarm(alarm)
    }

}