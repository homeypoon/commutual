
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.commutual.*
import com.example.commutual.R
import com.example.commutual.model.Task
import com.example.commutual.model.Task.Companion.ATTENDANCE_NO
import com.example.commutual.model.Task.Companion.ATTENDANCE_YES
import com.example.commutual.model.Task.Companion.COMPLETION_YES
import com.example.commutual.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationResultItem(
    resultType: Int,
    task: Task,
    user: User,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(20.dp, 8.dp)
    ) {

        Text(
            text = when (resultType) {
                ATTENDANCE_YES, ATTENDANCE_NO -> {
                    FormatterClass.formatTimestamp(task.showAttendanceTimestamp, true)
                }
                else -> {
                    FormatterClass.formatTimestamp(task.showCompletionTimestamp, true)
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        androidx.compose.material3.Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
        ) {
            Column(
                Modifier
                    .padding(12.dp, 12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = when (resultType) {
                        ATTENDANCE_YES, ATTENDANCE_NO -> {
                            stringResource(
                                R.string.task_attendance
                            )
                        }
                        else -> {
                            stringResource(
                                R.string.task_completion
                            )
                        }
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                )

                Text(
                    text = when (resultType) {
                        ATTENDANCE_YES -> {
                            stringResource(
                                R.string.confirmed_attendance, user.username
                            )
                        }
                        ATTENDANCE_NO -> {
                            stringResource(
                                R.string.no_attendance, user.username
                            )
                        }
                        COMPLETION_YES -> {
                            stringResource(
                                R.string.confirmed_completion, user.username
                            )
                        }
                        else -> {
                            // COMPLETION_NO
                            stringResource(
                                R.string.no_completion, user.username
                            )
                        }
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                )

                Divider()

                Text(
                    stringResource(
                        R.string.formatted_task_title, task.title
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                )
                Text(
                    stringResource(
                        R.string.formatted_task_time_nd,
                        task.startTime,
                        task.endTime
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .fillMaxWidth()
                )

                Text(
                    text = when (resultType) {
                        ATTENDANCE_YES -> {
                            stringResource(
                                R.string.earned_attendance_points,
                                ATTENDANCE_YES_POINTS.toString()
                            )
                        }
                        ATTENDANCE_NO -> {
                            stringResource(
                                R.string.lost_attendance_points,
                                ATTENDANCE_NO_POINTS.toString()
                            )
                        }
                        COMPLETION_YES -> {
                            stringResource(
                                R.string.earned_completion_points,
                                COMPLETION_YES_POINTS.toString()
                            )
                        }
                        else -> {
                            // COMPLETION_NO
                            stringResource(
                                R.string.lost_completion_points,
                                COMPLETION_NO_POINTS.toString()
                            )
                        }
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = when (resultType) {
                        ATTENDANCE_YES, COMPLETION_YES -> {
                            MaterialTheme.colorScheme.primary
                        }
                        else -> {
                            MaterialTheme.colorScheme.error
                        }
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth()
                )


            }

        }
    }
}
