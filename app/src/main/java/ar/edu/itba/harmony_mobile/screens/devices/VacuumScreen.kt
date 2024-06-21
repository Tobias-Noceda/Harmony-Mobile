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
import ar.edu.itba.harmony_mobile.model.Status
import ar.edu.itba.harmony_mobile.model.Vacuum
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
fun VacuumScreen(device: Vacuum, onBackCalled: () -> Unit) {

    val modeDropDownOptions = VacuumMode.entries.toList()
    val roomDropDownOptions = arrayOf("asdasd", "jhsgghjkgkhjf") //TODO REEMPLAZAR CON FETCH A LA API

    val adaptiveInfo = currentWindowAdaptiveInfo()
    BackHandler(onBack = onBackCalled)
    val viewModel: VacuumViewModel = viewModel(factory = getViewModelFactory())

    var mode by rememberSaveable { mutableStateOf(VacuumMode.VACUUM) }
    if (device.mode == "mop") {
        mode = VacuumMode.MOP
    }

    @Composable
    fun vacuumTitle() {
        Text(
            text = device.name, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
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
                            viewModel.setMode(device, item.apiText)
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
                device.targetRoom?.let { Text(it.name) }     //REEMPLAZAR CON EL NOMBRE
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
                roomDropDownOptions.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            //TODO device.targetRoom = item   //REEMPLAZAR CON OBTENER ID
                            isExpanded = false
                        },
                        text = {
                            Text(item, color = secondary)
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
                if (device.status == Status.ON) {
                    viewModel.pause(device)
                } else {
                    viewModel.start(device)
                }
            },
            colors = IconButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = device.battery > 5
        ) {
            Icon(
                painter = when (device.status == Status.ON) {
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
                viewModel.dock(device)
            },
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            //enabled = device.isCharging
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
                    if (device.status == Status.DOCKED) {
                        R.string.charging
                    } else if (device.status == Status.ON) {
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
                if (device.status == Status.DOCKED) {
                    R.drawable.battery_charging
                } else {
                    when (device.battery) {
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
                                    modifier = Modifier.fillMaxWidth(0.5f)
                                ) {
                                    modeSelector()
                                }
                                Column(
                                    modifier = Modifier.fillMaxWidth(0.5f)
                                ) {
                                    roomSelector()
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
