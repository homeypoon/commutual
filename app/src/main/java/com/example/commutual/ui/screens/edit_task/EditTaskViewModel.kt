package com.example.commutual.ui.screens.edit_task

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusManager
import com.example.commutual.FormatterClass.Companion.formatTime
import com.example.commutual.R
import com.example.commutual.TASK_DEFAULT_ID
import com.example.commutual.common.ext.idFromParameter
import com.example.commutual.common.snackbar.SnackbarManager
import com.example.commutual.model.CategoryEnum
import com.example.commutual.model.Chat
import com.example.commutual.model.Task
import com.example.commutual.model.Task.Companion.ATTENDANCE_NOT_DONE
import com.example.commutual.model.Task.Companion.COMPLETION_NOT_DONE
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.reflect.KFunction5
import kotlin.reflect.KFunction8


@HiltViewModel
class EditTaskViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService

) : CommutualViewModel(logService = logService) {
    val task = mutableStateOf(Task())
    val chat = mutableStateOf(Chat())


    private var uiState = mutableStateOf(EditTaskUiState())
        private set

    val startTime
        get() = uiState.value.startTime

    val endTime
        get() = uiState.value.endTime

    val year
        get() = uiState.value.selectedYear

    val month
        get() = uiState.value.selectedMonth

    val day
        get() = uiState.value.selectedDay

    val startHour
        get() = uiState.value.startHour

    val startMin
        get() = uiState.value.startMin

    val endHour
        get() = uiState.value.startHour

    val endMin
        get() = uiState.value.startMin

    val expandedDropDownMenu
        get() = uiState.value.expandedDropDownMenu


    fun setExpandedDropDownMenu(expandedDropDownMenu: Boolean) {
        uiState.value = uiState.value.copy(
            expandedDropDownMenu = expandedDropDownMenu
        )
    }

    fun initialize(taskId: String, chatId: String) {
        launchCatching {
            if (taskId != TASK_DEFAULT_ID) {
                task.value = storageService.getTask(taskId.idFromParameter(), chatId) ?: Task()
            }
            chat.value = storageService.getChatWithChatId(chatId) ?: Chat()
        }
    }

    fun onTitleChange(newValue: String) {
        task.value = task.value.copy(title = newValue)
    }

    fun onDetailsChange(newValue: String) {
        task.value = task.value.copy(details = newValue)
    }


    fun onCategoryButtonClick(selectedCategory: CategoryEnum) {
        task.value = task.value.copy(category = selectedCategory)
    }


    fun onDateChange(newDate: Long) {
        task.value = task.value.copy(date = newDate)
        uiState.value = uiState.value.copy(date = newDate)
    }

    fun onStartTimeChange(hour: Int, minute: Int) {

        uiState.value = uiState.value.copy(
            startTime = formatTime(hour, minute, false),
            startMin = minute,
            startHour = hour
        )
        Log.d("start hour", "${uiState.value.startHour}")
        task.value = task.value.copy(startTime = formatTime(hour, minute, true))

        Log.d("24 time", formatTime(hour, minute, true))
    }

    fun onEndTimeChange(hour: Int, minute: Int) {

        uiState.value = uiState.value.copy(
            endTime = formatTime(hour, minute, false),
            endHour = hour,
            endMin = minute
        )
        task.value = task.value.copy(endTime = formatTime(hour, minute, true))
    }

    fun showDatePicker(context: Context) {

        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

        val datePicker = DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                task.value = task.value.copy(
                    year = selectedYear,
                    month = selectedMonth,
                    day = selectedDayOfMonth
                )
                uiState.value = uiState.value.copy(
                    selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear",
                    selectedDay = selectedDayOfMonth,
                    selectedMonth = selectedMonth,
                    selectedYear = selectedYear
                )

            }, year, month, dayOfMonth
        )

        datePicker.datePicker.minDate = calendar.timeInMillis
        datePicker.show()


    }

    fun showTimePicker(activity: AppCompatActivity?, onTimeChange: (Int, Int) -> Unit) {
        val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).build()

        activity?.let {
            picker.show(it.supportFragmentManager, picker.toString())
            Log.d("picker", picker.toString())
            picker.addOnPositiveButtonClickListener { onTimeChange(picker.hour, picker.minute) }
        }
    }

    fun onDoneClick(
        chatId: String, popUpScreen: () -> Unit,
        focusManager: FocusManager,
        context: Context,
        showReminderNotification: KFunction5<Context, Int, String, String, Int, Unit>,
        setAlarmManager: KFunction8<Context, String, String, Calendar, Calendar, Array<String>, Task, String, Unit>
    ) {

        focusManager.clearFocus()

        if (task.value.title.isBlank()) {
            SnackbarManager.showMessage(R.string.task_empty_title_error)
            return
        }


        if (task.value.category == CategoryEnum.NONE) {
            SnackbarManager.showMessage(R.string.task_empty_category_error)
            return
        }

        if (uiState.value.selectedYear == 0 && uiState.value.selectedMonth == 0 && uiState.value.selectedDay == 0) {
            SnackbarManager.showMessage(R.string.task_empty_date_error)
            return
        }

        if (
            ((uiState.value.endHour - uiState.value.startHour) < 0)
            || ((uiState.value.endHour - uiState.value.startHour) == 0) &&
            (uiState.value.endMin - uiState.value.startMin) <= 0
        ) {
            SnackbarManager.showMessage(R.string.task_invalid_time)
            return
        }

        val attendanceMap = mutableMapOf<String, Int>()
        for (memberId in chat.value.membersId) {
            attendanceMap[memberId] = ATTENDANCE_NOT_DONE
        }

        val completionMap = mutableMapOf<String, Int>()
        for (memberId in chat.value.membersId) {
            completionMap[memberId] = COMPLETION_NOT_DONE
        }

        task.value = task.value.copy(
            attendance = attendanceMap,
            completion = completionMap,
            creatorId = accountService.currentUserId
        )

        launchCatching {
            val editedTask = task.value
            if (editedTask.taskId.isBlank()) {
                val taskId: String = storageService.saveTask(editedTask, chatId)
                Log.d("taskId", "taskId$taskId")

                storageService.updateCurrentUser(chatId, taskId)
                task.value = task.value.copy(
                    taskId = taskId
                )
            } else {
                storageService.updateTaskType(editedTask, chatId, null)
            }

            val attendanceCalendar: Calendar = Calendar.getInstance()

            attendanceCalendar.set(
                uiState.value.selectedYear,
                uiState.value.selectedMonth,
                uiState.value.selectedDay,
                uiState.value.startHour,
                uiState.value.startMin,
                0
            )

            Log.d("calendar", "${attendanceCalendar.time}")

            val completionCalendar: Calendar = Calendar.getInstance()
            completionCalendar.set(
                uiState.value.selectedYear,
                uiState.value.selectedMonth,
                uiState.value.selectedDay,
                uiState.value.endHour,
                uiState.value.endMin,
                0
            )

            setAlarmManager(
                context,
                "${task.value.title} is starting: Are you ready to work?",
                "${task.value.startTime} - ${task.value.endTime}",
                attendanceCalendar,
                completionCalendar,
                chat.value.membersId.toTypedArray(),
                task.value,
                chatId
            )

            popUpScreen()
        }
    }

    companion object {
        private const val DATE_FORMAT = "EEE, d MMM yyyy"
        private const val REMINDER_NOTIFICATION_ID = 0

    }

}