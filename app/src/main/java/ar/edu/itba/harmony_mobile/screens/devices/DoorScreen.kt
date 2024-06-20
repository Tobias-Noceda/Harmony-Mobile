package ar.edu.itba.harmony_mobile.screens.devices

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowHeightSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Door
import ar.edu.itba.harmony_mobile.model.Status
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


@Composable
fun DoorScreen(device: Door, onBackCalled: () -> Unit) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    BackHandler(onBack = onBackCalled)

    @Composable
    fun doorTitle() {
        Text(
            text = device.name, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun openText() {
        Text(
            text = when (device.status) {
                Status.OPEN -> stringResource(id = R.string.close)
                else -> stringResource(id = R.string.open)
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun openSwitch() {
        Switch(
            checked = device.status != Status.OPEN,
            onCheckedChange = {
                /* TODO ACTUALIZAR CON API
                device.status = when (device.status) {
                    Status.OPEN -> Status.CLOSED
                    else -> Status.OPEN
                }*/
            },
            enabled = !device.lock,
            colors = SwitchDefaults.colors(
                checkedThumbColor = tertiary,
                checkedTrackColor = tertiary.copy(0.5f),
                uncheckedThumbColor = primary,
                uncheckedTrackColor = primary.copy(0.5f),
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun lockButton() {
        Button(
            onClick = {
                /* TODO ACTUALIZAR CON API
                    device.lock = !device.lock
                */
            },
            enabled = device.status != Status.OPEN,
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when (device.lock) {
                    true -> stringResource(id = R.string.unlock)
                    else -> stringResource(id = R.string.lock)
                }
            )
        }
    }

    @Composable
    fun stateText() {
        Text(
            text = "${stringResource(id = R.string.status)} ${
                if(device.lock){
                    stringResource(id = R.string.locked)
                } else{
                    when (device.status) {
                        Status.OPEN -> stringResource(id = R.string.opened)
                        Status.CLOSED -> stringResource(id = R.string.closed)
                        else -> "WTF"
                    }
                }
            }"
        )
    }

    @Composable
    fun stateIcons() {
        Box(
        ) {
            Icon(
                painter =
                when (device.status) {
                    Status.OPEN -> painterResource(id = R.drawable.door_open)
                    else -> painterResource(id = R.drawable.door)
                },
                contentDescription = "",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            )
        }
        Box(
        ) {
            Icon(
                painter =
                when (device.lock) {
                    true -> painterResource(id = R.drawable.lock)
                    else -> painterResource(id = R.drawable.lock_open)
                },
                contentDescription = "",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            )
        }
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
            doorTitle()
            Row(
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(with(adaptiveInfo) {
                        if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
                            10.dp
                        } else {
                            30.dp
                        }
                    })
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    with(adaptiveInfo) {
                        if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                openText()
                                openSwitch()
                            }
                        } else {
                            Column (modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally){
                                openText()
                                openSwitch()
                            }
                        }
                    }
                    lockButton()
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    stateText()
                    with(adaptiveInfo) {
                        if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
                            Row {
                                stateIcons()
                            }
                        } else {
                            stateIcons()
                        }
                    }
                }
            }
        }
    }
}