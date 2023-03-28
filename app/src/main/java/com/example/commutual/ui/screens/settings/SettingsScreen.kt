
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.*
import com.example.commutual.ui.screens.settings.SettingsViewModel
import com.example.commutual.R.drawable as AppIcon
import com.example.commutual.R.string as AppText


@Composable
@ExperimentalMaterialApi
fun SettingsScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(stringResource(R.string.settings))

        Column(modifier = Modifier.fillMaxWidth().padding(24.dp, 0.dp)) {
            SignOutCard { viewModel.onSignOutClick(restartApp) }
            DeleteMyAccountCard { viewModel.onDeleteMyAccountClick(restartApp) }
        }

    }
}

@ExperimentalMaterialApi
@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(AppText.log_out, AppIcon.ic_log_out, "") {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = {
                Text(
                    text = stringResource(AppText.log_out),
                    style = MaterialTheme.typography.headlineMedium,
                )
            },
            text = { Text(
                stringResource(AppText.log_out_message),
                style = MaterialTheme.typography.bodyMedium
            ) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.log_out) {
                    signOut()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    WarningCardEditor(
        AppText.delete_account,
        AppIcon.ic_delete_account,
        "",
    ) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = {
                Text(
                    stringResource(AppText.delete_account),
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            text = { Text(stringResource(AppText.delete_account_message)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.delete_account) {
                    deleteMyAccount()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}