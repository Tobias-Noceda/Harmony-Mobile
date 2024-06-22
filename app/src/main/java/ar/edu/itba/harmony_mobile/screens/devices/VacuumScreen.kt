package ar.edu.itba.harmony_mobile.screens.devices

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.model.Status
import ar.edu.itba.harmony_mobile.model.Vacuum
import ar.edu.itba.harmony_mobile.ui.devices.DevicesViewModel
import ar.edu.itba.harmony_mobile.ui.devices.VacuumViewModel
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


enum class VacuumMode(@StringRes val textId: Int, val apiText: String) {
    VACUUM(R.string.vacuum, "vacuum"),
    MOP(R.string.mop, "mop"),
}

@Composable
fun VacuumScreen(deviceRef: Vacuum, rooms: List<Room>, onBackCalled: () -> Unit) {

    val modeDropDownOptions = VacuumMode.entries.toList()

    val adaptiveInfo = currentWindowAdaptiveInfo()
    BackHandler(onBack = onBackCalled)
    val viewModel: VacuumViewModel = viewModel(factory = getViewModelFactory())


    val dViewModel: DevicesViewModel = viewModel(factory = getViewModelFactory())
    val deviceState by dViewModel.uiState.collectAsState()

    dViewModel.getDevice(deviceRef.id!!) // updates the current device

    fun getValidDevice(): Vacuum {
        if (deviceState.currentDevice != null) {
            return deviceState.currentDevice as Vacuum
        }
        return deviceRef
    }

    var mode by rememberSaveable {
        mutableStateOf(
            if (getValidDevice().mode == "mop") {
                VacuumMode.MOP
            } else {
                VacuumMode.VACUUM
            }
        )
    }

    fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
        return if (condition) {
            then(modifier(Modifier))
        } else {
            this
        }
    }

    @Composable
    fun vacuumTitle() {
        Text(
            text = getValidDevice().name,
            color = primary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun modeSelector() {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        Text(
            text = stringResource(id = R.string.mode) + ":",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
        Box {
            Button(
                onClick = { isExpanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                    contentColor = secondary
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = getValidDevice().targetRoom != null
            ) {
                Text(stringResource(mode.textId))
                Icon(
                    imageVector = when (isExpanded) {
                        false -> Icons.Default.KeyboardArrowDown
                        true -> Icons.Default.KeyboardArrowUp
                    },
                    contentDescription = ""
                )
            }
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                containerColor = primary,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                modeDropDownOptions.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            mode = item
                            isExpanded = false
                            viewModel.setMode(getValidDevice(), item.apiText)
                        },
                        text = {
                            Text(stringResource(item.textId), color = secondary)
                        },
                    )
                }
            }
        }

    }

    @Composable
    fun roomSelector() {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        Text(
            text = stringResource(id = R.string.target_room),
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
        Box {
            Button(
                onClick = { isExpanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                    contentColor = secondary
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                getValidDevice().targetRoom?.let { Text(it.name) }
                Icon(
                    imageVector = when (isExpanded) {
                        false -> Icons.Default.KeyboardArrowDown
                        true -> Icons.Default.KeyboardArrowUp
                    },
                    contentDescription = ""
                )
            }
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                containerColor = primary,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd),
            ) {
                rooms.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            item.id?.let { viewModel.setLocation(getValidDevice(), it) }
                            isExpanded = false
                        },
                        text = {
                            Text(item.name, color = secondary)
                        },
                    )
                }
            }
        }

    }

    @Composable
    fun onButton() {
        IconButton(
            onClick = {
                if (getValidDevice().status == Status.ACTIVE || getValidDevice().status == Status.ON) {
                    viewModel.pause(getValidDevice())
                    dViewModel.getDevice(deviceRef.id)
                } else {
                    viewModel.start(getValidDevice())
                    dViewModel.getDevice(deviceRef.id)
                }
            },
            colors = IconButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = getValidDevice().battery > 5
        ) {
            Icon(
                painter = when (getValidDevice().status == Status.ACTIVE || getValidDevice().status == Status.ON) {
                    true -> painterResource(id = R.drawable.pause)
                    false -> painterResource(id = R.drawable.play_arrow)
                }, contentDescription = ""
            )
        }
    }

    @Composable
    fun sendToBaseButton() {
        Button(
            onClick = {
                viewModel.dock(getValidDevice())
                dViewModel.getDevice(deviceRef.id)
            },
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = getValidDevice().status != Status.DOCKED
        ) {
            Text(text = stringResource(id = R.string.send_to_base))
        }
    }

    @Composable
    fun statusText() {
        Text(
            text = "${stringResource(id = R.string.status)} ${
                stringResource(
                    id =
                    if (getValidDevice().status == Status.DOCKED && getValidDevice().status != Status.INACTIVE) {
                        R.string.charging
                    } else if (getValidDevice().status == Status.ON || getValidDevice().status == Status.ACTIVE) {
                        R.string.on
                    } else {
                        R.string.off
                    }
                )
            }",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun batteryIndicator() {
        Icon(
            painter = painterResource(
                id =
                if (getValidDevice().status == Status.DOCKED) {
                    R.drawable.battery_charging
                } else {
                    when (getValidDevice().battery) {
                        in 1..15 -> R.drawable.battery_1_bar
                        in 15..30 -> R.drawable.battery_2_bar
                        in 30..45 -> R.drawable.battery_3_bar
                        in 45..60 -> R.drawable.battery_4_bar
                        in 60..75 -> R.drawable.battery_5_bar
                        in 75..90 -> R.drawable.battery_6_bar
                        in 90..100 -> R.drawable.battery_full
                        else -> R.drawable.battery_empty
                    }
                }
            ),
            modifier = Modifier
                .conditional(
                    adaptiveInfo.windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.EXPANDED
                            || adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
                ) {
                    fillMaxSize(0.5f)
                },
            contentDescription = ""
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(25.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            vacuumTitle()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {

                with(adaptiveInfo) {
                    if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                onButton()
                                sendToBaseButton()
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(0.5f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                statusText()
                                batteryIndicator()
                            }
                        }
                    } else if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.EXPANDED) {
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(
                                        30.dp,
                                        Alignment.CenterHorizontally
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    onButton()
                                    sendToBaseButton()
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        modeSelector()
                                    }
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        roomSelector()
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                statusText()
                                batteryIndicator()
                            }
                        }
                    } else if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(25.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(
                                        30.dp,
                                        Alignment.CenterHorizontally
                                    ),
                                ) {
                                    onButton()
                                    sendToBaseButton()
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        modeSelector()
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        roomSelector()
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                statusText()
                                batteryIndicator()
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                Row {
                                    onButton()
                                    sendToBaseButton()
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(
                                        25.dp,
                                        Alignment.CenterHorizontally
                                    )
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        modeSelector()
                                        roomSelector()
                                    }
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                statusText()
                                batteryIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
