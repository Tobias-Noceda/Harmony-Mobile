package ar.edu.itba.harmony_mobile.screens.devices

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ar.edu.itba.harmony_mobile.ui.theme.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowHeightSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.model.Status
import ar.edu.itba.harmony_mobile.ui.devices.DevicesViewModel
import ar.edu.itba.harmony_mobile.ui.devices.SprinklerViewModel
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory

enum class UnitToDispense(@StringRes val textId: Int, val apiText: String) {
    MILLILITRES(R.string.mL, "ml"),
    DECILITRES(R.string.dL, "dl"),
    LITRES(R.string.L, "l"),
    HECTOLITRES(R.string.hL, "hl"),
    KILOLITRES(R.string.kL, "kl");
}

@Composable
fun SprinklerScreen(deviceRef: Sprinkler, onBackCalled: () -> Unit) {

    val dropDownOptions = UnitToDispense.entries.toList()
    var selectedUnitToDispense by rememberSaveable { mutableStateOf(UnitToDispense.LITRES) }
    var selectedAmountToDispense by rememberSaveable { mutableIntStateOf(1) }

    val adaptiveInfo = currentWindowAdaptiveInfo()
    BackHandler(onBack = onBackCalled)
    val viewModel: SprinklerViewModel = viewModel(factory = getViewModelFactory())

    var isDispensing by rememberSaveable { mutableStateOf(false) }

    val dViewModel: DevicesViewModel = viewModel(factory = getViewModelFactory())
    val deviceState by dViewModel.uiState.collectAsState()

    dViewModel.getDevice(deviceRef.id!!) // updates the current device

    fun getValidDevice(): Sprinkler {
        if (deviceState.currentDevice != null && deviceState.currentDevice is Sprinkler) {
            return deviceState.currentDevice as Sprinkler
        }
        return deviceRef
    }

    @Composable
    fun blindsTitle() {
        Text(
            text = getValidDevice().name, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun blindsStatusText() {
        Text(
            text = "${stringResource(id = R.string.status)} ${
                if (getValidDevice().status == Status.OPEN || getValidDevice().status == Status.OPENED) {
                    stringResource(id = R.string.opened)
                } else {
                    stringResource(id = R.string.closed)
                }
            }",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun openButton() {
        Button(
            onClick = {
                viewModel.start(getValidDevice())
                dViewModel.getDevice(deviceRef.id)
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = getValidDevice().status != Status.OPEN,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,
                disabledElevation = (-5).dp
            )
        ) {
            Text(text = stringResource(id = R.string.open))
        }
    }

    @Composable
    fun closeButton() {
        Button(
            onClick = {
                viewModel.pause(getValidDevice())
                dViewModel.getDevice(deviceRef.id)
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = getValidDevice().status != Status.OPEN && !isDispensing,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,
                disabledElevation = (-5).dp
            )
        ) {
            Text(text = stringResource(id = R.string.close))
        }
    }

    @Composable
    fun DispenseMenu() {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current
        var amountBuff by rememberSaveable { mutableStateOf(selectedAmountToDispense.toString()) }

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${stringResource(id = R.string.dispense)}:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth()
            )
            if (adaptiveInfo.windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
                Column {
                    Row {
                        TextField(
                            value = amountBuff,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                                    amountBuff = newValue
                                } else if (newValue.isEmpty()) {
                                    amountBuff = ""
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.NumberPassword,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                                selectedAmountToDispense =
                                    amountBuff.toInt().coerceAtLeast(1).coerceAtMost(100)
                                amountBuff = selectedAmountToDispense.toString()
                            }),
                            label = { Text(stringResource(id = R.string.amount)) },
                            modifier = Modifier
                                .fillMaxWidth(0.33f)
                                .fillMaxHeight(0.75f)
                        )
                        Box {
                            Button(
                                onClick = { isExpanded = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primary,
                                    contentColor = secondary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .fillMaxHeight(0.75f)
                            ) {
                                Text(stringResource(selectedUnitToDispense.textId))
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
                                            selectedUnitToDispense = item
                                            isExpanded = false
                                        },
                                        text = {
                                            Text(stringResource(item.textId), color = secondary)
                                        },
                                    )
                                }
                            }
                        }
                        Button(
                            onClick = {
                                viewModel.dispense(
                                    getValidDevice(),
                                    selectedUnitToDispense.apiText,
                                    selectedAmountToDispense
                                )
                                dViewModel.getDevice(deviceRef.id)
                                isDispensing = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonColors(
                                tertiary,
                                secondary,
                                tertiary.desaturate(0f),
                                secondary.desaturate(0f)
                            ),
                            enabled = !isDispensing,
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 5.dp,
                                disabledElevation = (-5).dp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.75f)
                        ) {
                            Text(text = stringResource(id = R.string.dispense))
                        }
                    }
                }
            } else {
                TextField(
                    value = amountBuff,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                            amountBuff = newValue
                        } else if (newValue.isEmpty()) {
                            amountBuff = ""
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        selectedAmountToDispense =
                            amountBuff.toInt().coerceAtLeast(1).coerceAtMost(100)
                        amountBuff = selectedAmountToDispense.toString()
                    }),
                    label = { Text(stringResource(id = R.string.amount)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Box {
                    Button(
                        onClick = { isExpanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primary,
                            contentColor = secondary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(selectedUnitToDispense.textId))
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
                                    selectedUnitToDispense = item
                                    isExpanded = false
                                },
                                text = {
                                    Text(stringResource(item.textId), color = secondary)
                                },
                            )
                        }
                    }
                }
                Button(
                    onClick = {
                        viewModel.dispense(
                            getValidDevice(),
                            selectedUnitToDispense.apiText,
                            selectedAmountToDispense
                        )
                        dViewModel.getDevice(deviceRef.id)
                        isDispensing = true
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        tertiary,
                        secondary,
                        tertiary.desaturate(0f),
                        secondary.desaturate(0f)
                    ),
                    enabled = !isDispensing,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 5.dp,
                        disabledElevation = (-5).dp
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.dispense))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {

            blindsTitle()
            with(adaptiveInfo) {
                if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(25.dp)
                    ) {
                        blindsStatusText()
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            openButton()
                            closeButton()
                        }
                        DispenseMenu()
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            blindsStatusText()
                            Row(
                                Modifier.fillMaxWidth(0.5f),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                openButton()
                                closeButton()
                            }

                        }
                        DispenseMenu()
                    }
                }
            }
        }
    }
}
