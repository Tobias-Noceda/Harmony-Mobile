package ar.edu.itba.harmony_mobile.screens.devices

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.window.core.layout.WindowHeightSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


enum class DoorState {
    OPEN, CLOSED, LOCKED
}

@Composable
fun DoorScreen(deviceName: String, onBackCalled: () -> Unit) {
    var state by rememberSaveable { mutableStateOf(DoorState.CLOSED) }

    val adaptiveInfo = currentWindowAdaptiveInfo()
    BackHandler(onBack = onBackCalled)

    @Composable
    fun doorTitle() {
        Text(
            text = deviceName, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun openText() {
        Text(
            text = when (state) {
                DoorState.OPEN -> stringResource(id = R.string.close)
                else -> stringResource(id = R.string.open)
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun openSwitch() {
        Switch(
            checked = state != DoorState.OPEN,
            onCheckedChange = {
                state = when (state) {
                    DoorState.OPEN -> DoorState.CLOSED
                    else -> DoorState.OPEN
                }
            },
            enabled = state != DoorState.LOCKED,
            colors = SwitchDefaults.colors(
                checkedThumbColor = tertiary,
                checkedTrackColor = tertiary.copy(0.5f),
                uncheckedThumbColor = primary,
                uncheckedTrackColor = primary.copy(0.5f),
            )
        )
    }

    @Composable
    fun lockButton() {
        Button(
            onClick = {
                state = when (state) {
                    DoorState.LOCKED -> DoorState.CLOSED
                    else -> DoorState.LOCKED
                }
            },
            enabled = state != DoorState.OPEN,
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
        ) {
            Text(
                text = when (state) {
                    DoorState.LOCKED -> stringResource(id = R.string.unlock)
                    else -> stringResource(id = R.string.lock)
                }
            )
        }
    }

    @Composable
    fun stateText() {
        Text(
            text = "${stringResource(id = R.string.status)} ${
                when (state) {
                    DoorState.OPEN -> stringResource(id = R.string.opened)
                    DoorState.CLOSED -> stringResource(id = R.string.closed)
                    DoorState.LOCKED -> stringResource(id = R.string.locked)
                }
            }"
        )
    }

    @Composable
    fun stateIcons(modifier: Modifier) {
        Row(modifier = modifier) {
            Box(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Icon(
                    painter =
                    when (state) {
                        DoorState.OPEN -> painterResource(id = R.drawable.door_open)
                        else -> painterResource(id = R.drawable.door)
                    },
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Icon(
                    painter =
                    when (state) {
                        DoorState.LOCKED -> painterResource(id = R.drawable.lock)
                        else -> painterResource(id = R.drawable.lock_open)
                    },
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
                            Column {
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
                    Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                        stateIcons(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}