package ar.edu.itba.harmony_mobile.screens.devices

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.window.core.layout.WindowHeightSizeClass
import ar.edu.itba.harmony_mobile.R

enum class UnitToDispense(val text: String) {
    MILLILITRES("Millilitres"),
    DECILITRES("Decilitres"),
    LITRES("Litres"),
    HECTOLITRES("Hectolitres"),
    KILOLITRES("Kilolitres");
}

@Composable
fun SprinklerScreen(onBackCalled: () -> Unit) {

    val sprinklerName by rememberSaveable { mutableStateOf("Sprinkler Name") }

    val dropDownOptions = UnitToDispense.entries.toList()
    var selectedUnitToDispense by rememberSaveable { mutableStateOf(UnitToDispense.LITRES) }

    var selectedAmountToDispense by rememberSaveable { mutableIntStateOf(1) }

    var open by rememberSaveable { mutableStateOf(false) }
    var isDispensing by rememberSaveable { mutableStateOf(false) }

    val adaptiveInfo = currentWindowAdaptiveInfo()
    BackHandler(onBack = onBackCalled)

    @Composable
    fun blindsTitle() {
        Text(
            text = sprinklerName, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun blindsStatusText() {
        Text(
            text = "${stringResource(id = R.string.status)} ${
                if (open) {
                    stringResource(id = R.string.on)
                } else {
                    stringResource(id = R.string.off)
                }
            }",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun openButton() {
        Button(
            onClick = { /*SEND TO API*/
                open = true
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = !open,
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
            onClick = { /*SEND TO API*/
                open = false
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = open && !isDispensing,
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
                                Text(selectedUnitToDispense.text)
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
                                            Text(item.text, color = secondary)
                                        },
                                    )
                                }
                            }
                        }
                        Button(
                            onClick = { /*SEND TO API*/
                                //DISPENSE
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
                        Text(selectedUnitToDispense.text)
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
                                    Text(item.text, color = secondary)
                                },
                            )
                        }
                    }
                }
                Button(
                    onClick = { /*SEND TO API*/
                        //DISPENSE
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
