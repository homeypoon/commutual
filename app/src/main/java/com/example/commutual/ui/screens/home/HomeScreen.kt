package com.example.commutual.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.model.CategoryEnum
import com.example.commutual.model.Task
import com.example.commutual.ui.screens.item.TaskItem
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val tasks by viewModel.currentUserTasks.collectAsStateWithLifecycle(emptyList())
    val upcomingTasks = tasks.filter { !it.showAttendance && !it.showCompletion }
    val inProgressTasks =
        tasks.filter { it.showAttendance && !it.showCompletion && (it.attendance[viewModel.currentUserId] != Task.ATTENDANCE_NO) }

    val uiState by viewModel.uiState
    val user by viewModel.user.collectAsState()

    LaunchedEffect(Unit) { viewModel.initialize() }


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

                Spacer(modifier = Modifier.padding(bottom = 12.dp))

            }

            if (inProgressTasks.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.in_progress_tasks_sessions),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    )
                }

                items(inProgressTasks) { inProgressTask ->
                    TaskItem(task = inProgressTask)
                }

            }

            if (inProgressTasks.isNotEmpty()) {
                item {
                    Divider(
                        modifier = Modifier
                            .padding(top = 32.dp, bottom = 20.dp)
                    )
                }
            }

            if (upcomingTasks.isNotEmpty()) {

                item {
                    Text(
                        text = stringResource(R.string.upcoming_tasks_sessions),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    )
                }
                items(upcomingTasks) { upcomingTask ->
                    TaskItem(task = upcomingTask)
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
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
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
            item {
                Divider(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 4.dp)
                )
            }

            // Pie Chart for Tasks Completed and Tasks Scheduled
            if (user?.tasksMissed != 0L || user?.tasksCompleted != 0L) {

                item {

                    Box(
                        modifier =
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .padding(start = 24.dp, end = 24.dp, top = 28.dp)
                                .fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = stringResource(R.string.completion_graph_title),
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(top = 16.dp, bottom = 12.dp)
                                        .fillMaxWidth()
                                )



                                user?.let {
                                    PieChart(
                                        totalTasksMissed = it.tasksMissed,
                                        totalTasksCompleted = it.tasksCompleted
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .padding(
                                                top = 8.dp,
                                                bottom = 8.dp,
                                                start = 24.dp,
                                                end = 8.dp
                                            )
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(
                                                    MaterialTheme.colorScheme.primaryContainer
                                                )
                                        )
                                        Text(
                                            text = stringResource(
                                                R.string.missed_sessions,
                                                uiState.missedTasksPercentage
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier
                                                .padding(start = 8.dp)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .padding(
                                                top = 8.dp,
                                                bottom = 20.dp,
                                                start = 24.dp,
                                                end = 8.dp
                                            )
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(
                                                    MaterialTheme.colorScheme.primary,
                                                )
                                                .padding(end = 8.dp)
                                        )
                                        Text(
                                            text = stringResource(
                                                R.string.completed_sessions,
                                                uiState.completedTasksPercentage
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier
                                                .padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }

            item {

                Box(
                    modifier =
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp, top = 32.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = stringResource(R.string.tasks_per_category_graph_title),
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 16.dp, bottom = 4.dp)
                                    .fillMaxWidth()
                            )

                            user?.let {
                                BarGraph(
                                    it.categoryCount
                                )
                            }
                        }


                    }
                }



                Spacer(modifier = Modifier.height(84.dp))
            }

        }
    }
}


@Composable
fun PieChart(
    totalTasksMissed: Long,
    totalTasksCompleted: Long
) {
    val tasksMissedColor = MaterialTheme.colorScheme.primaryContainer
    val tasksCompletedColor = MaterialTheme.colorScheme.primary


    val entries = listOf(
        PieEntry(totalTasksMissed.toFloat(), "Missed Tasks"),
        PieEntry(totalTasksCompleted.toFloat(), "Completed Tasks")
    )


    val dataSet = PieDataSet(entries, "Tasks")
    dataSet.setDrawValues(false)
    dataSet.colors = listOf(tasksMissedColor.toArgb(), tasksCompletedColor.toArgb())


    val pieData = PieData(dataSet)

    AndroidView(
        modifier = Modifier
            .padding(start = 18.dp, end = 12.dp, bottom = 18.dp)
            .size(240.dp),
        factory = { context ->
            PieChart(context).apply {
                data = pieData
                setUsePercentValues(false)
                setDrawEntryLabels(false)
                description.isEnabled = false
                legend.isEnabled = false
                isDrawHoleEnabled = true
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

    val colorTemplate = intArrayOf(
        Color(0xFFB8ECB4).toArgb(),
        Color(0xFFA3E7D0).toArgb(),
        Color(0xFF99C8E7).toArgb(),
        Color(0xFFB1B1EE).toArgb(),
    )

    dataSet.setColors(colorTemplate, 255)

    val barChartData = BarData(dataSet)

    AndroidView(
        modifier = Modifier
            .padding(top = 18.dp, start = 18.dp, end = 18.dp, bottom = 24.dp)
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

                xAxis.isEnabled = true
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(true)
                xAxis.textSize = 14f
                xAxis.typeface = ResourcesCompat.getFont(context, R.font.open_sans_regular)

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.labelRotationAngle = -90f

                axisLeft.isEnabled = true
                axisLeft.granularity = 1.0f
                axisLeft.axisMinimum = 0f
                axisLeft.setDrawAxisLine(true)
                axisLeft.setDrawGridLines(true)
                axisLeft.axisMaximum = barChartData.yMax
                axisRight.isEnabled = false

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
