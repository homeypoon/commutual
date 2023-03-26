package com.example.commutual.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.model.CategoryEnum
import com.example.commutual.model.User
import com.example.commutual.ui.screens.item.TaskItem
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
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
                        style = MaterialTheme.typography.displaySmall
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


        LazyColumn(
            modifier = modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                BasicToolbar(
                    title = stringResource(R.string.home)
                )
            }


            if (upcomingUserTasks.value.isNotEmpty()) {

                item {
                    Text(
                        text = stringResource(R.string.upcoming_tasks_sessions),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(upcomingUserTasks.value) { task ->
                    TaskItem(task = task, creator = User())
                }


            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(R.string.upcoming_tasks_sessions),
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .padding(vertical = 12.dp, horizontal = 18.dp),
                        )

                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.no_upcoming_tasks_sessions),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 24.dp)
                                    .fillMaxWidth()
                            )
                        }

                    }
                }

            }

            // Pie Chart for Tasks Completed and Tasks Scheduled

            item {

                Box(
                    modifier =
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    user?.let {
                        BarGraph(
                            it.categoryCount
                        )
                    }

                }

                if (user?.tasksScheduled != 0L || user?.tasksCompleted != 0L) {
                    Box(
                        modifier =
                        Modifier.fillMaxWidth(),
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

                Spacer(modifier = Modifier.height(72.dp))
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
                legend.isEnabled = true
                legend.textSize = 18f
                data = pieData

                setDrawEntryLabels(false)

                animateY(500)
                isDrawHoleEnabled = false
                setEntryLabelTextSize(18f)
            }

        }
    )

}

@Composable
fun BarGraph(
    categoryCount: Map<String, Int>
) {

    val entries = categoryCount.entries.mapIndexed { index, (category, count) ->
        BarEntry(index.toFloat(), count.toFloat(), category)
    }


    val dataSet = BarDataSet(entries, "Task counts")

    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS, 255)

    val barChartData = BarData(dataSet)

    AndroidView(
        modifier = Modifier
            .padding(18.dp)
            .size(300.dp),
        factory = { context ->
            BarChart(context).apply {
                data = barChartData
                description.isEnabled = false
                legend.isEnabled = false
                dataSet.setDrawValues(false)
                setDrawValueAboveBar(false)
                setDrawBarShadow(false)
                setPinchZoom(false)
                setTouchEnabled(false)
//                setFitBars(true)

                xAxis.isEnabled = true
                xAxis.setDrawGridLines(false)
                axisLeft.isEnabled = true
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.labelRotationAngle = -90f
//
//                axisLeft.valueFormatter = object : ValueFormatter() {
//                    override fun getFormattedValue(value: Float): String {
//                        return floor(value.toDouble()).toInt().toString()
//                    }
//                }

                val categoryNames = categoryCount.keys.map {
                    context.getString(CategoryEnum.valueOf(it).categoryStringRes)
                }

                xAxis.valueFormatter = IndexAxisValueFormatter(
                    categoryNames.toList()
                )

                axisLeft.labelCount = barChartData.yMax.toInt()


            }
        }
    )
}
