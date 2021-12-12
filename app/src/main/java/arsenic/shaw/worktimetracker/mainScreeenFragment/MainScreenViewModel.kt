package arsenic.shaw.worktimetracker.mainScreeenFragment

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import arsenic.shaw.worktimetracker.database.Work
import arsenic.shaw.worktimetracker.database.WorkDatabase
import arsenic.shaw.worktimetracker.utils.Constant
import arsenic.shaw.worktimetracker.utils.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class MainScreenViewModel(application: Application) : AndroidViewModel(application) {


    private val database = WorkDatabase.getInstance(application).dao

    private val preferenceManager = PreferenceManager(application)

    private val _startAtText = MutableLiveData<String>()
    val startAtText: LiveData<String>
        get() = _startAtText

    private val _liveTimer = MutableLiveData<String>()
    val liveTimer: LiveData<String>
        get() = _liveTimer

    val allWorks = database.getAllWork()

    //utils
    private val _isWorkStarted = MutableLiveData<Boolean>()
    val isWorkStarted: LiveData<Boolean>
    get() = _isWorkStarted

    private var timer = Timer()
    private var startTime: Calendar = Calendar.getInstance()

    init {
        _startAtText.value = ""
        _liveTimer.value = "00:00:00"
        _isWorkStarted.value = preferenceManager.getBoolean(Constant.IS_WORK_STARTED)

        if(preferenceManager.getBoolean(Constant.IS_WORK_STARTED)){
            _startAtText.value = "Started at " + startTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + startTime.get(Calendar.MINUTE)

            val c = Calendar.getInstance()
            c.timeInMillis = preferenceManager.getLong(Constant.STARTED_AT)
            startTime = c

            timer.schedule(object: TimerTask(){
                override fun run() {
                    viewModelScope.launch {
                        _liveTimer.value = getTimerText()
                    }
                }
            }, 0L, 1000L)
        }
    }

    fun startClicked(){
        startTime = Calendar.getInstance()
        preferenceManager.putLong(Constant.STARTED_AT, startTime.timeInMillis)
        _startAtText.value = "Started at " + startTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + startTime.get(Calendar.MINUTE)
        _isWorkStarted.value = true
        preferenceManager.putBoolean(Constant.IS_WORK_STARTED, true)

        // change time every second

        timer.schedule(object: TimerTask(){
            override fun run() {
                viewModelScope.launch {
                    _liveTimer.value = getTimerText()
                }
            }
        }, 0L, 1000L)

    }

    fun endClicked() = viewModelScope.launch {

        _isWorkStarted.value = false
        preferenceManager.putBoolean(Constant.IS_WORK_STARTED, false)
        timer.cancel()
        timer = Timer()
        _liveTimer.value = "00:00:00"
        _startAtText.value = ""

        withContext(Dispatchers.IO){
            database.insert(Work(
                startTime = startTime.timeInMillis,
                endTime = Calendar.getInstance().timeInMillis
            ))
        }
    }


    private fun getTimerText(): String{
        val timeText = ""
        val milli = Calendar.getInstance().timeInMillis - startTime.timeInMillis

        return milliToHMS(milli)
    }

    private fun milliToHMS(millis: Long): String{

        val s = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        val m = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
        val h = TimeUnit.MILLISECONDS.toHours(millis)

        var output: String = ""

        if(h < 10){
            output = output + "0" + h + ":"
        }else{
            output = output + h + ":"
        }

        if(m < 10){
            output = output + "0" + m + ":"
        }else{
            output = output + m + ":"
        }

        if(s < 10){
            output = output + "0" + s
        }else{
            output = output + s
        }

        return output
    }

    fun deleteWork(id: Long) = viewModelScope.launch{
        withContext(Dispatchers.IO){
            database.delete(id)
        }
    }

    fun deleteAllPlant() = viewModelScope.launch {
        withContext(Dispatchers.IO){
            database.deleteAll()
        }
    }
}