package com.example.commutual.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.model.User
import com.example.commutual.ui.screens.item.TaskItem
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {


    val upcomingUserTasks = viewModel.upcomingUserTasks.collectAsStateWithLifecycle(emptyList())
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState
    val user by viewModel.user.collectAsState()


    Scaffold(
        floatingActionButton = {

            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(R.string.add_post),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.displayMedium
                    )
                },
                icon = {
                    Icon(
                        Icons.Filled.Add, "Add",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                },
                onClick = { viewModel.onAddClick(openScreen) },
                modifier = modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.tertiary,
            )
        }
    ) { padding ->


        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            BasicToolbar(
                title = stringResource(R.string.home)
            )

            if (upcomingUserTasks.value.isNotEmpty()) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    userScrollEnabled = false,
                    contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.upcoming_tasks_sessions)
                        )
                    }
                    items(upcomingUserTasks.value) { task ->
                        TaskItem(task = task, creator = User())
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.upcoming_tasks_sessions)
                    )

                    Text(
                        text = stringResource(R.string.no_upcoming_tasks_sessions)
                    )
                }

            }


            // Pie Chart for Tasks Completed and Tasks Scheduled
            Box(
                modifier =
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                user?.let {
                    PieChart(
                        it.tasksScheduled,
                        it.tasksCompleted
                    )
                }
            }
        }
    }
}


@Composable
fun PieChart(
    totalTasksScheduled: Long,
    totalTasksCompleted: Long
) {
    val tasksCompletedColor = MaterialTheme.colorScheme.primary
    val tasksScheduledColor = MaterialTheme.colorScheme.secondary
    val onTasksCompletedColor = MaterialTheme.colorScheme.tertiary
    val onTasksScheduledColor = MaterialTheme.colorScheme.tertiary


    val entries = listOf(
        PieEntry(totalTasksScheduled.toFloat(), "Scheduled"),
        PieEntry(totalTasksCompleted.toFloat(), "Completed")
    )


    Log.d("user.tasksCompleted", totalTasksCompleted.toFloat().toString())
    Log.d("user.tasksScheduled", totalTasksScheduled.toFloat().toString())


    val dataSet = PieDataSet(entries, "Tasks")
    dataSet.colors = listOf(tasksCompletedColor.toArgb(), tasksScheduledColor.toArgb())
    dataSet.valueTextSize = 32f

    dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
    dataSet.valueLinePart1Length = 0.4f
    dataSet.valueLinePart2Length = 0.7f
    dataSet.valueLineWidth = 4f
    dataSet.valueLineColor = MaterialTheme.colorScheme.primaryContainer.toArgb()

//    dataSet.valueFormatter = PercentFormatter()
    dataSet.valueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return (DecimalFormat("##0.0%")).format(value.toDouble() / 100)
        }
    }

    dataSet.setValueTextColors(
        listOf(
            onTasksCompletedColor.toArgb(),
            onTasksScheduledColor.toArgb()
        )
    )

    val pieData = PieData(dataSet)

    AndroidView(
        modifier = Modifier
            .padding(18.dp)
            .size(300.dp),
        factory = { context ->
            PieChart(context).apply {
                setUsePercentValues(true)
                description.isEnabled = false
                legend.isEnabled = false
                data = pieData
                animateY(500)
                isDrawHoleEnabled = false
                setEntryLabelTextSize(18f)
            }

        }
    )

}
