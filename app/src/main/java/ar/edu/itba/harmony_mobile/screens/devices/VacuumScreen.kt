package ar.edu.itba.harmony_mobile.screens.devices

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.window.core.layout.WindowHeightSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


enum class VacuumMode(@StringRes val textId: Int) {
    VACUUM(R.string.vacuum),
    MOP(R.string.mop),
}

@Composable
fun VacuumScreen(deviceName: String, onBackCalled: () -> Unit) {

    val modeDropDownOptions = VacuumMode.entries.toList()
    val roomDropDownOptions = arrayOf("asdasd", "jhsgghjkgkhjf") //REEMPLAZAR CON FETCH A LA API

    var mode by rememberSaveable { mutableStateOf(VacuumMode.VACUUM) }
    var isOn by rememberSaveable { mutableStateOf(false) }
    var isCharging by rememberSaveable { mutableStateOf(false) }
    var battery by rememberSaveable { mutableFloatStateOf(50f) }
    var assignedRoomID by rememberSaveable { mutableStateOf("asdasd") }

    val adaptiveInfo = currentWindowAdaptiveInfo()
    BackHandler(onBack = onBackCalled)

    @Composable
    fun vacuumTitle() {
        Text(
            text = deviceName, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun modeSelector() {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        Text(
            text = stringResource(id = R.string.mode),
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
                Text(assignedRoomID)     //REEMPLAZAR CON EL NOMBRE
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
                            assignedRoomID = item   //REEMPLAZAR CON OBTENER ID
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
                isOn = !isOn
                isCharging = false
            },
            colors = IconButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = battery > 5
        ) {
            Icon(
                painter = when (isOn) {
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
                isOn = false
                isCharging = true
            },
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = !isCharging
        ) {
            Text(text = stringResource(id = R.string.send_to_base))
        }
    }

    @Composable
    fun statusText() {
        Text(
            text = "${stringResource(id = R.string.status)} ${
                stringResource(
                    id = if (isCharging) {
                        R.string.charging
                    } else if (isOn) {
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
                if (isCharging) {
                    R.drawable.battery_charging
                } else {
                    when (battery) {
                        in 1f..15f -> R.drawable.battery_1_bar
                        in 15f..30f -> R.drawable.battery_2_bar
                        in 30f..45f -> R.drawable.battery_3_bar
                        in 45f..60f -> R.drawable.battery_4_bar
                        in 60f..75f -> R.drawable.battery_5_bar
                        in 75f..90f -> R.drawable.battery_6_bar
                        in 90f..100f -> R.drawable.battery_full
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
                        Row {
                            Column {
                                onButton()
                                sendToBaseButton()
                            }
                            Column {
                                statusText()
                                batteryIndicator()
                            }
                        }
                    } else {
                        Row (horizontalArrangement = Arrangement.spacedBy(20.dp)){
                            Column {
                                onButton()
                                sendToBaseButton()
                            }
                            Column {
                                modeSelector()
                                roomSelector()
                            }
                        }
                        statusText()
                        batteryIndicator()
                    }
                }
            }
        }
    }
}
