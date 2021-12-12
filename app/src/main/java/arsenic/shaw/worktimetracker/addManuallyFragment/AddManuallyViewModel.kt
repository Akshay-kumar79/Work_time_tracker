package arsenic.shaw.worktimetracker.addManuallyFragment

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View
import android.widget.DatePicker
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arsenic.shaw.worktimetracker.database.Work
import arsenic.shaw.worktimetracker.database.WorkDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*
import kotlin.math.min

class AddManuallyViewModel(application: Application) : AndroidViewModel(application) {


    private val database = WorkDatabase.getInstance(application).dao

    //properties
    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String>
    get() = _startDate

    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
    get() = _startTime

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String>
    get() = _endDate

    private val _endTime = MutableLiveData<String>()
    val endTime: LiveData<String>
    get() = _endTime


    //Utils
    private val _workAdded = MutableLiveData<Boolean>()
    val workAdded: LiveData<Boolean>
    get() = _workAdded

    private val _startDateDialogShowing = MutableLiveData<Boolean>()
    val startDateDialogShowing: LiveData<Boolean>
    get() = _startDateDialogShowing

    private val _endDateDialogShowing = MutableLiveData<Boolean>()
    val endDateDialogShowing: LiveData<Boolean>
    get() = _endDateDialogShowing

    private val _startTimeDialogShowing = MutableLiveData<Boolean>()
    val startTimeDialogShowing: LiveData<Boolean>
    get() = _startTimeDialogShowing

    private val _endTimeDialogShowing = MutableLiveData<Boolean>()
    val endTimeDialogShowing: LiveData<Boolean>
    get() = _endTimeDialogShowing


    val startDateChangeListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        val newMonth = month+1
        _startDate.value = getFormattedDate(dayOfMonth, newMonth, year)
        _startDateDialogShowing.value = false
    }
    val endDateChangeListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        val newMonth = month+1
        _endDate.value = getFormattedDate(dayOfMonth, newMonth, year)
        _endDateDialogShowing.value = false
    }
    val startTimeChangeListener = TimePickerDialog.OnTimeSetListener{view, hourOfDay, minute ->
        _startTime.value = getFormattedTime(hourOfDay, minute)
        _startTimeDialogShowing.value = false
    }
    val endTimeChangeListener = TimePickerDialog.OnTimeSetListener{view, hourOfDay, minute ->
        _endTime.value = getFormattedTime(hourOfDay, minute)
        _endTimeDialogShowing.value = false
    }


    init {
        _startDate.value = "00-00-00"
        _endDate.value = "00-00-00"
        _startTime.value = "00:00"
        _endTime.value = "00:00"
    }

    fun onStartDateChangeClick(){
        _startDateDialogShowing.value = true
    }

    fun onEndDateChangeClick(){
        _endDateDialogShowing.value = true
    }

    fun onStartTimeChangeClick(){
        _startTimeDialogShowing.value = true
    }

    fun onEndTimeChangeClick(){
        _endTimeDialogShowing.value = true
    }

    fun onDoneButtonClicked() = viewModelScope.launch {

        val dateFormatter = SimpleDateFormat("HH:mm, dd-MM-yyyy", Locale.getDefault())

        if (validateAddManualWork()){
            val work = Work(
                startTime = dateFormatter.parse("${startTime.value}, ${startDate.value}")!!.time,
                endTime = dateFormatter.parse("${endTime.value}, ${endDate.value}")!!.time
            )

            withContext(Dispatchers.IO){
                database.insert(work)
            }

            _workAdded.value = true
        }

    }

    private fun validateAddManualWork(): Boolean{
        when {
            startDate.value == "00-00-00" -> {
                Toast.makeText(getApplication(), "please select start date", Toast.LENGTH_SHORT).show()
                return false
            }
            endDate.value == "00-00-00" -> {
                Toast.makeText(getApplication(), "please select end date", Toast.LENGTH_SHORT).show()
                return false
            }
            startTime.value == "00:00" -> {
                Toast.makeText(getApplication(), "please start select date", Toast.LENGTH_SHORT).show()
                return false
            }
            endTime.value == "00:00" -> {
                Toast.makeText(getApplication(), "please start select date", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }

    }

    private fun getFormattedTime(hour: Int, minute: Int): String {

        var hourText = hour.toString()
        var minuteText = minute.toString()

        if (hour < 10)
            hourText = "0$hour"

        if (minute < 10) {
            minuteText = "0$minute"
        }

        return "$hourText:$minuteText"
    }

    private fun getFormattedDate(day: Int, month: Int, year: Int): String{
        var dayText = day.toString()
        var monthText = month.toString()

        if (day < 10)
            dayText = "0$day"

        if (month < 10){
            monthText = "0$month"
        }

        return "$dayText-$monthText-$year"
    }

    override fun onCleared() {
        super.onCleared()
        _workAdded.value = false
    }
}