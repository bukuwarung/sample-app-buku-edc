package com.bukuwarung.edc.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bukuwarung.edc.domain.settings.AccountSettings
import com.bukuwarung.edc.domain.settings.BankAccount
import com.bukuwarung.edc.domain.settings.DeviceInfo
import com.bukuwarung.edc.domain.settings.StoreInfo
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.theme.Colors
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenuScreen(
    onBack: () -> Unit,
    onAccountClick: () -> Unit,
    onBankAccountsClick: () -> Unit,
    viewModel: SettingsMenuViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is SettingsMenuUiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        context.getString(event.messageResId),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    SettingsMenuContent(
        onBack = onBack,
        onAccountClick = onAccountClick,
        onBankAccountsClick = onBankAccountsClick,
        onPrintReceiptTestClick = viewModel::onPrintReceiptTestClick,
        onSoundEffectSettingsClick = viewModel::onSoundEffectSettingsClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsMenuContent(
    onBack: () -> Unit,
    onAccountClick: () -> Unit,
    onBankAccountsClick: () -> Unit,
    onPrintReceiptTestClick: () -> Unit,
    onSoundEffectSettingsClick: () -> Unit,
) {
    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back),
                        )
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            item {
                SettingsSectionHeader(
                    text = stringResource(R.string.settings_section_profile),
                    modifier = Modifier.padding(top = 12.dp),
                )
            }

            item {
                SettingsNavRow(
                    title = stringResource(R.string.settings_menu_account),
                    onClick = onAccountClick,
                )
            }
            item { HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp) }
            item {
                SettingsNavRow(
                    title = stringResource(R.string.settings_menu_bank_accounts),
                    onClick = onBankAccountsClick,
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp) }

            item {
                SettingsSectionHeader(
                    text = stringResource(R.string.settings_section_receipt),
                    modifier = Modifier.padding(top = 12.dp),
                )
            }

            item {
                SettingsNavRow(
                    title = stringResource(R.string.settings_menu_print_test),
                    onClick = onPrintReceiptTestClick,
                )
            }
            item { HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp) }
            item {
                SettingsNavRow(
                    title = stringResource(R.string.settings_menu_sound_effects),
                    onClick = onSoundEffectSettingsClick,
                )
            }
            item { HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAccountScreen(
    onBack: () -> Unit,
    onEditStoreNameClick: () -> Unit,
    viewModel: SettingsAccountViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateCompat()
    val accountSettings = state.accountSettings

    SettingsAccountContent(
        onBack = onBack,
        accountSettings = accountSettings,
        onEditStoreNameClick = onEditStoreNameClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsAccountContent(
    onBack: () -> Unit,
    accountSettings: AccountSettings?,
    onEditStoreNameClick: () -> Unit,
) {
    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_account_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            SettingsInfoItem(
                label = stringResource(R.string.settings_account_registered_phone),
                value = accountSettings?.deviceInfo?.registeredPhoneNumber.orEmpty(),
            )
            HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp)
            SettingsInfoItem(
                label = stringResource(R.string.settings_account_device_serial),
                value = accountSettings?.deviceInfo?.deviceSerialNumber.orEmpty(),
            )
            HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp)
            SettingsInfoItem(
                label = stringResource(R.string.settings_account_terminal_id),
                value = accountSettings?.deviceInfo?.terminalId.orEmpty(),
            )
            HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp)
            SettingsInfoItem(
                label = stringResource(R.string.settings_account_store_name),
                value = accountSettings?.storeInfo?.name.orEmpty(),
                trailing = {
                    OutlinedButton(
                        onClick = onEditStoreNameClick,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                    ) {
                        Text(stringResource(R.string.settings_action_edit))
                    }
                },
            )
            HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp)
            SettingsInfoItem(
                label = stringResource(R.string.settings_account_store_address),
                value = accountSettings?.storeInfo?.address.orEmpty(),
            )
            HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsEditStoreNameScreen(
    onBack: () -> Unit,
    viewModel: SettingsEditStoreNameViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateCompat()

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is SettingsEditStoreNameUiEvent.ShowToastAndClose -> {
                    Toast.makeText(
                        context,
                        context.getString(event.messageResId),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBack()
                }
            }
        }
    }

    SettingsEditStoreNameContent(
        onBack = onBack,
        state = state,
        onStoreNameChange = viewModel::onStoreNameChange,
        onSaveClick = viewModel::onSaveClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsEditStoreNameContent(
    onBack: () -> Unit,
    state: SettingsEditStoreNameUiState,
    onStoreNameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_edit_store_name_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back),
                        )
                    }
                },
            )
        },
        bottomBar = {
            Surface(color = Colors.White) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                        .navigationBarsPadding(),
                ) {
                    Button(
                        onClick = onSaveClick,
                        enabled = state.isSaveEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(stringResource(R.string.settings_action_save))
                    }
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.settings_edit_store_name_label),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            )
            OutlinedTextField(
                value = state.storeNameInput,
                onValueChange = onStoreNameChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Text(
                text = stringResource(R.string.settings_edit_store_name_help),
                style = MaterialTheme.typography.bodySmall,
                color = Colors.TextGray,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.settings_edit_store_address_label),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            )
            OutlinedTextField(
                value = state.storeAddress,
                onValueChange = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBankAccountsScreen(
    onBack: () -> Unit,
    viewModel: SettingsBankAccountsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateCompat()

    SettingsBankAccountsContent(
        onBack = onBack,
        accounts = state.accounts,
        onEditAccountClick = {
            Toast.makeText(
                context,
                context.getString(R.string.settings_toast_feature_unavailable),
                Toast.LENGTH_SHORT
            ).show()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsBankAccountsContent(
    onBack: () -> Unit,
    accounts: List<BankAccount>,
    onEditAccountClick: () -> Unit,
) {
    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_bank_accounts_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.settings_bank_accounts_description),
                style = MaterialTheme.typography.bodySmall,
                color = Colors.TextGray,
            )
            Spacer(modifier = Modifier.height(12.dp))

            accounts.forEach { account ->
                BankAccountCard(
                    account = account,
                    onEditClick = onEditAccountClick,
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.labelSmall,
        color = Colors.TextGray,
    )
}

@Composable
private fun SettingsNavRow(
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Colors.Black,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Colors.TextGray,
        )
    }
}

@Composable
private fun SettingsInfoItem(
    label: String,
    value: String,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = Colors.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = Colors.TextGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (trailing != null) {
            Spacer(modifier = Modifier.width(12.dp))
            trailing()
        }
    }
}

@Composable
private fun BankAccountCard(
    account: BankAccount,
    onEditClick: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Colors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Colors.SurfaceBorder, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = account.bankName.take(3),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Colors.Black,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${account.bankName} - ${account.accountHolderName}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Colors.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = account.accountNumber,
                    style = MaterialTheme.typography.bodySmall,
                    color = Colors.TextGray,
                )
                if (account.isPrimaryPayoutAccount) {
                    Text(
                        text = stringResource(R.string.settings_bank_accounts_primary_label),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Colors.IconBlue,
                    )
                }
            }
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.settings_bank_accounts_edit_action)) },
                        onClick = {
                            menuExpanded = false
                            onEditClick()
                        },
                    )
                }
            }
        }
    }
}

/**
 * Minimal compat wrapper to avoid pulling in `collectAsStateWithLifecycle` dependency.
 */
@Composable
private fun <T> StateFlow<T>.collectAsStateCompat(): State<T> {
    return collectAsState(initial = value)
}

@Preview(showBackground = true)
@Composable
private fun SettingsMenuPreview() {
    SettingsMenuContent(
        onBack = {},
        onAccountClick = {},
        onBankAccountsClick = {},
        onPrintReceiptTestClick = {},
        onSoundEffectSettingsClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsAccountPreview() {
    val sample = AccountSettings(
        deviceInfo = DeviceInfo(
            registeredPhoneNumber = "0813 1010 1111",
            deviceSerialNumber = "C38753489",
            terminalId = "T73648723",
        ),
        storeInfo = StoreInfo(
            name = "Toko Cahaya Abadi",
            address = "Jl. Pahlawan Revolusi No.24, Jakarta Timur",
        ),
    )
    SettingsAccountContent(
        onBack = {},
        accountSettings = sample,
        onEditStoreNameClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsEditStoreNamePreview() {
    SettingsEditStoreNameContent(
        onBack = {},
        state = SettingsEditStoreNameUiState(
            initialStoreName = "Toko Cahaya Abadi",
            storeNameInput = "Toko Cahaya Abadi - Cabang GG",
            storeAddress = "Jl. Pahlawan Revolusi No.24, Jakarta Timur",
        ),
        onStoreNameChange = {},
        onSaveClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsBankAccountsPreview() {
    SettingsBankAccountsContent(
        onBack = {},
        accounts = listOf(
            BankAccount(
                bankName = "BSI",
                accountHolderName = "Febriana",
                accountNumber = "013249322434",
                isPrimaryPayoutAccount = true,
            )
        ),
        onEditAccountClick = {},
    )
}
