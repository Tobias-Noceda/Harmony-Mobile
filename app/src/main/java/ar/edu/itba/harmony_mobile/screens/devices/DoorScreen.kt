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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowHeightSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.model.Door
import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.model.Status
import ar.edu.itba.harmony_mobile.ui.devices.DevicesViewModel
import ar.edu.itba.harmony_mobile.ui.devices.DoorViewModel
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


@Composable
fun DoorScreen(deviceRef: Door, onBackCalled: (() -> Unit)? = null) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    if (onBackCalled != null) {
        BackHandler(onBack = onBackCalled)
    }

    val viewModel: DoorViewModel = viewModel(factory = getViewModelFactory())

    val dViewModel: DevicesViewModel = viewModel(factory = getViewModelFactory())
    val deviceState by dViewModel.uiState.collectAsState()

    dViewModel.setCurrentDeviceId(deviceRef.id!!)

    fun getValidDevice(): Door {
        if (deviceState.currentDevice != null && deviceState.currentDevice is Door) {
            return deviceState.currentDevice as Door
        }
        val aux: Device? = deviceState.devices.find { it.id == deviceRef.id }
        if (aux != null && aux is Door) {
            return aux
        }
        return deviceRef
    }

    @Composable
    fun doorTitle() {
        Text(
            text = getValidDevice().name,
            color = primary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun openText() {
        Text(
            text = when (getValidDevice().status) {
                Status.OPEN, Status.OPENED -> stringResource(id = R.string.close)
                else -> stringResource(id = R.string.open)
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun openSwitch() {
        Switch(
            checked = getValidDevice().status == Status.OPEN || getValidDevice().status == Status.OPENED,
            onCheckedChange = {
                when (getValidDevice().status) {
                    Status.OPEN, Status.OPENED -> {
                        viewModel.close(getValidDevice())
                    }

                    else -> {
                        viewModel.open(getValidDevice())
                    }
                }
            },
            enabled = !getValidDevice().lock,
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
                when (getValidDevice().lock) {
                    true -> {
                        viewModel.unlock(getValidDevice())
                    }

                    false -> {
                        viewModel.lock(getValidDevice())
                    }
                }
            },
            enabled = getValidDevice().status != Status.OPEN && getValidDevice().status != Status.OPENED,
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when (getValidDevice().lock) {
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
                if (getValidDevice().lock) {
                    stringResource(id = R.string.locked)
                } else {
                    when (getValidDevice().status) {
                        Status.CLOSED -> stringResource(id = R.string.closed)
                        else -> stringResource(id = R.string.opened)
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
                when (getValidDevice().status) {
                    Status.OPEN, Status.OPENED -> painterResource(id = R.drawable.door_open)
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
                when (getValidDevice().lock) {
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
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
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