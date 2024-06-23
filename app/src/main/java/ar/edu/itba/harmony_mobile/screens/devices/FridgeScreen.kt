package ar.edu.itba.harmony_mobile.screens.devices

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.ui.devices.DevicesViewModel
import ar.edu.itba.harmony_mobile.ui.devices.RefrigeratorViewModel
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


enum class FridgeMode(@StringRes val textId: Int, val apiText: String) {
    DEFAULT(R.string.default_mode, "default"),
    VACATION(R.string.vacation_mode, "vacation"),
    PARTY(R.string.party_mode, "party")
}

@Composable
fun FridgeScreen(deviceRef: Refrigerator, onBackCalled: (() -> Unit)? = null) {

    val dropDownOptions = FridgeMode.entries.toList()

    val adaptiveInfo = currentWindowAdaptiveInfo()
    if (onBackCalled != null) {
        BackHandler(onBack = onBackCalled)
    }
    val viewModel: RefrigeratorViewModel = viewModel(factory = getViewModelFactory())


    val dViewModel: DevicesViewModel = viewModel(factory = getViewModelFactory())
    val deviceState by dViewModel.uiState.collectAsState()

    dViewModel.setCurrentDeviceId(deviceRef.id!!)

    fun getValidDevice(): Refrigerator {
        if (deviceState.currentDevice != null && deviceState.currentDevice is Refrigerator) {
            return deviceState.currentDevice as Refrigerator
        }
        val aux: Device? = deviceState.devices.find { it.id == deviceRef.id }
        if (aux != null && aux is Refrigerator) {
            return aux
        }
        return deviceRef
    }

    @Composable
    fun fridgeTitle() {
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
                Text(
                    stringResource(
                        when (getValidDevice().mode) {
                            "vacation" -> FridgeMode.VACATION.textId
                            "party" -> FridgeMode.PARTY.textId
                            else -> FridgeMode.DEFAULT.textId
                        }
                    )
                )
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
                dropDownOptions.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            viewModel.setMode(getValidDevice(), item.apiText)
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
    fun tempButton(onClick: () -> Unit, icon: Painter, enabled: Boolean) {
        IconButton(
            onClick = {
                onClick.invoke()
            },
            colors = IconButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = enabled
        ) {
            Icon(painter = icon, contentDescription = "")
        }
    }

    @Composable
    fun fridgeTempText() {
        Text(
            text = "${getValidDevice().temperature} °C",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun freezerTempText() {
        Text(
            text = "${getValidDevice().freezerTemperature} °C",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(25.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            fridgeTitle()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {

                    with(adaptiveInfo) {
                        if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = stringResource(id = R.string.fridge),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        tempButton(
                                            onClick = {
                                                viewModel.setTemperature(
                                                    getValidDevice(),
                                                    getValidDevice().temperature - 1
                                                )
                                            },
                                            icon = painterResource(id = R.drawable.remove),
                                            enabled = getValidDevice().temperature > 2 && getValidDevice().mode == "default"
                                        )
                                        fridgeTempText()
                                        tempButton(
                                            onClick = {
                                                viewModel.setTemperature(
                                                    getValidDevice(),
                                                    getValidDevice().temperature + 1
                                                )
                                            },
                                            icon = painterResource(id = R.drawable.add),
                                            enabled = getValidDevice().temperature < 8 && getValidDevice().mode == "default"
                                        )
                                    }
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = stringResource(id = R.string.freezer),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        tempButton(
                                            onClick = {
                                                viewModel.setFreezerTemperature(
                                                    getValidDevice(),
                                                    getValidDevice().freezerTemperature - 1
                                                )
                                            },
                                            icon = painterResource(id = R.drawable.remove),
                                            enabled = getValidDevice().freezerTemperature > -20 && getValidDevice().mode == "default"
                                        )
                                        freezerTempText()
                                        tempButton(
                                            onClick = {
                                                viewModel.setFreezerTemperature(
                                                    getValidDevice(),
                                                    getValidDevice().freezerTemperature + 1
                                                )
                                            },
                                            icon = painterResource(id = R.drawable.add),
                                            enabled = getValidDevice().freezerTemperature < -8 && getValidDevice().mode == "default"
                                        )
                                    }
                                }
                            }
                        } else if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(20.dp)
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.fridge),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Normal
                                        )
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            tempButton(
                                                onClick = {
                                                    viewModel.setTemperature(
                                                        getValidDevice(),
                                                        getValidDevice().temperature - 1
                                                    )
                                                },
                                                icon = painterResource(id = R.drawable.remove),
                                                enabled = getValidDevice().temperature > 2 && getValidDevice().mode == "default"
                                            )
                                            fridgeTempText()
                                            tempButton(
                                                onClick = {
                                                    viewModel.setTemperature(
                                                        getValidDevice(),
                                                        getValidDevice().temperature + 1
                                                    )
                                                },
                                                icon = painterResource(id = R.drawable.add),
                                                enabled = getValidDevice().temperature < 8 && getValidDevice().mode == "default"
                                            )
                                        }
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(20.dp)
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.freezer),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Normal
                                        )
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            tempButton(
                                                onClick = {
                                                    viewModel.setFreezerTemperature(
                                                        getValidDevice(),
                                                        getValidDevice().freezerTemperature - 1
                                                    )
                                                },
                                                icon = painterResource(id = R.drawable.remove),
                                                enabled = getValidDevice().freezerTemperature > -20 && getValidDevice().mode == "default"
                                            )
                                            freezerTempText()
                                            tempButton(
                                                onClick = {
                                                    viewModel.setFreezerTemperature(
                                                        getValidDevice(),
                                                        getValidDevice().freezerTemperature + 1
                                                    )
                                                },
                                                icon = painterResource(id = R.drawable.add),
                                                enabled = getValidDevice().freezerTemperature < -8 && getValidDevice().mode == "default"
                                            )
                                        }
                                    }
                                }
                                Column {
                                    modeSelector()
                                }
                            }
                        } else {
                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.fridge),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        tempButton(
                                            onClick = {
                                                viewModel.setTemperature(
                                                    getValidDevice(),
                                                    getValidDevice().temperature - 1
                                                )
                                            },
                                            icon = painterResource(id = R.drawable.remove),
                                            enabled = getValidDevice().temperature > 2 && getValidDevice().mode == "default"
                                        )
                                        fridgeTempText()
                                        tempButton(
                                            onClick = {
                                                viewModel.setTemperature(
                                                    getValidDevice(),
                                                    getValidDevice().temperature + 1
                                                )
                                            },
                                            icon = painterResource(id = R.drawable.add),
                                            enabled = getValidDevice().temperature < 8 && getValidDevice().mode == "default"
                                        )
                                    }
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.freezer),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        tempButton(
                                            onClick = {
                                                viewModel.setFreezerTemperature(
                                                    getValidDevice(),
                                                    getValidDevice().freezerTemperature - 1
                                                )
                                            },
                                            icon = painterResource(id = R.drawable.remove),
                                            enabled = getValidDevice().freezerTemperature > -20 && getValidDevice().mode == "default"
                                        )
                                        freezerTempText()
                                        tempButton(
                                            onClick = {
                                                viewModel.setFreezerTemperature(
                                                    getValidDevice(),
                                                    getValidDevice().freezerTemperature + 1
                                                )
                                            },
                                            icon = painterResource(id = R.drawable.add),
                                            enabled = getValidDevice().freezerTemperature < -8 && getValidDevice().mode == "default"
                                        )
                                    }
                                }
                            }
                            modeSelector()
                        }
                    }
                }
            }
        }
    }
}
