package com.bukuwarung.edc.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.annotation.StringRes
import com.bukuwarung.edc.ui.theme.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is HomeUiEvent.ShowToast -> {
                    val label = context.getString(event.action.labelResId)
                    Toast.makeText(context, "$label clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Colors.PrimaryGreen)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = stringResource(R.string.home_title),
            color = Colors.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Colors.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.home_mini_atm),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Colors.Black
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bluetooth,
                            contentDescription = stringResource(R.string.content_description_bluetooth),
                            modifier = Modifier.size(24.dp),
                            tint = Colors.Black
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = stringResource(R.string.content_description_print),
                            modifier = Modifier.size(24.dp),
                            tint = Colors.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Grid
                val items = listOf(
                    ActionItemData(HomeAction.Transfer, Icons.Default.CreditCard),
                    ActionItemData(HomeAction.CekSaldo, Icons.Default.CreditCard),
                    ActionItemData(HomeAction.TarikTunai, Icons.Default.FileDownload),
                    ActionItemData(HomeAction.Riwayat, Icons.AutoMirrored.Filled.Assignment),
                    ActionItemData(HomeAction.Pengaturan, Icons.Default.Settings)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.height(280.dp),
                    userScrollEnabled = false
                ) {
                    items(items) { item ->
                        ActionItem(
                            item = item,
                            onClick = { clickedItem ->
                                viewModel.onActionClick(clickedItem.action)
                            }
                        )
                    }
                }
            }
        }
    }
}

data class ActionItemData(
    val action: HomeAction,
    val icon: ImageVector
)

enum class HomeAction(@StringRes val labelResId: Int) {
    Transfer(R.string.home_action_transfer),
    CekSaldo(R.string.home_action_cek_saldo),
    TarikTunai(R.string.home_action_tarik_tunai),
    Riwayat(R.string.home_action_riwayat),
    Pengaturan(R.string.home_action_pengaturan)
}

@Composable
fun ActionItem(
    item: ActionItemData,
    onClick: (ActionItemData) -> Unit
) {
    val label = stringResource(item.action.labelResId)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .size(72.dp)
                .clickable { onClick(item) },
            shape = RoundedCornerShape(16.dp),
            color = Colors.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Colors.SurfaceBorder),
            shadowElevation = 1.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = label,
                    modifier = Modifier.size(36.dp),
                    tint = Colors.IconBlue
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = Colors.TextGray
        )
    }
}

@Preview(showBackground = true, backgroundColor = Colors.PrimaryGreenColor)
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel = HomeViewModel())
}
