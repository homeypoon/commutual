package com.example.commutual.ui.screens.edit_task

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusManager
import com.example.commutual.FormatterClass.Companion.formatTime
import com.example.commutual.R
import com.example.commutual.TASK_DEFAULT_ID
import com.example.commutual.common.ext.idFromParameter
import com.example.commutual.common.snackbar.SnackbarManager
import com.example.commutual.model.CategoryEnum
import com.example.commutual.model.Task
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EditTaskViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService

) : CommutualViewModel(logService = logService) {
    val task = mutableStateOf(Task())

    private var uiState = mutableStateOf(EditTaskUiState())
        private set

    val startTime
        get() = uiState.value.startTime

    val endTime
        get() = uiState.value.endTime

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

    fun onDoneClick(chatId: String, popUpScreen: () -> Unit, focusManager: FocusManager) {

        focusManager.clearFocus()

        if (task.value.title.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_title_error)
            return
        }


        if (task.value.category == CategoryEnum.ANY) {
            SnackbarManager.showMessage(R.string.empty_category_error)
            return
        }

        if (task.value.category == CategoryEnum.ANY) {
            SnackbarManager.showMessage(R.string.empty_category_error)
            return
        }


        launchCatching {
            val editedTask = task.value
            if (editedTask.taskId.isBlank()) {
                storageService.saveTask(editedTask, chatId)
            } else {
                storageService.updateTask(editedTask, chatId)
            }
            popUpScreen()
        }
    }

    fun onDateChange(newDate: Long) {
//        val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
//        calendar.timeInMillis = newValue

//        val date = FormatterClass.formatDate(newDate)
        task.value = task.value.copy(date = newDate)
    }

    fun onStartTimeChange(hour: Int, minute: Int) {

        uiState.value = uiState.value.copy(startTime = formatTime(hour, minute, false))
        task.value = task.value.copy(startTime = formatTime(hour, minute, true))

        Log.d("24 time", formatTime(hour, minute, true))
    }

    fun onEndTimeChange(hour: Int, minute: Int) {

        uiState.value = uiState.value.copy(endTime = formatTime(hour, minute, false))
        task.value = task.value.copy(endTime = formatTime(hour, minute, true))
    }

    fun showDatePicker(activity: AppCompatActivity?, onDateChange: (Long) -> Unit) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setSelection(task.value.date).build()

        activity?.let {
            picker.show(it.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                    timeInMillis -> onDateChange(timeInMillis)
            }
        }
    }

    fun showTimePicker(activity: AppCompatActivity?, onTimeChange: (Int, Int) -> Unit) {
        val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).build()

        activity?.let {
            picker.show(it.supportFragmentManager, picker.toString())
            Log.d("picker", picker.toString())
            picker.addOnPositiveButtonClickListener { onTimeChange(picker.hour, picker.minute) }
        }
    }

    companion object {
        private const val UTC = "UTC"
        private const val DATE_FORMAT = "EEE, d MMM yyyy"
    }

}