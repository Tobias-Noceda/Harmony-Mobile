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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.window.core.layout.WindowHeightSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


enum class FridgeMode(@StringRes val textId: Int) {
    DEFAULT(R.string.default_mode),
    VACATION(R.string.vacation_mode),
    PARTY(R.string.party_mode)
}

@Composable
fun FridgeScreen(deviceName: String, onBackCalled: () -> Unit) {

    val dropDownOptions = FridgeMode.entries.toList()

    var mode by rememberSaveable { mutableStateOf(FridgeMode.DEFAULT) }
    var fridgeTemp by rememberSaveable { mutableIntStateOf(5) }
    var freezerTemp by rememberSaveable { mutableIntStateOf(-10) }

    val adaptiveInfo = currentWindowAdaptiveInfo()
    BackHandler(onBack = onBackCalled)

    @Composable
    fun fridgeTitle() {
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
                dropDownOptions.forEach { item ->
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
            text = "${fridgeTemp.toString()} °C",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun freezerTempText() {
        Text(
            text = "${freezerTemp.toString()} °C",
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
            modifier = Modifier.fillMaxWidth()
        ) {
            fridgeTitle()
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
                                            (fridgeTemp--)
                                        },
                                        icon = painterResource(id = R.drawable.remove),
                                        enabled = fridgeTemp > 2 && mode == FridgeMode.DEFAULT
                                    )
                                    fridgeTempText()
                                    tempButton(
                                        onClick = {
                                            (fridgeTemp++)
                                        },
                                        icon = painterResource(id = R.drawable.add),
                                        enabled = fridgeTemp < 8 && mode == FridgeMode.DEFAULT
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
                                            (freezerTemp--)
                                        },
                                        icon = painterResource(id = R.drawable.remove),
                                        enabled = freezerTemp > -20 && mode == FridgeMode.DEFAULT
                                    )
                                    freezerTempText()
                                    tempButton(
                                        onClick = {
                                            (freezerTemp++)
                                        },
                                        icon = painterResource(id = R.drawable.add),
                                        enabled = freezerTemp < -8 && mode == FridgeMode.DEFAULT
                                    )
                                }
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
                                            (fridgeTemp--)
                                        },
                                        icon = painterResource(id = R.drawable.remove),
                                        enabled = fridgeTemp > 2 && mode == FridgeMode.DEFAULT
                                    )
                                    fridgeTempText()
                                    tempButton(
                                        onClick = {
                                            (fridgeTemp++)
                                        },
                                        icon = painterResource(id = R.drawable.add),
                                        enabled = fridgeTemp < 8 && mode == FridgeMode.DEFAULT
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
                                            (freezerTemp--)
                                        },
                                        icon = painterResource(id = R.drawable.remove),
                                        enabled = freezerTemp > -20 && mode == FridgeMode.DEFAULT
                                    )
                                    freezerTempText()
                                    tempButton(
                                        onClick = {
                                            (freezerTemp++)
                                        },
                                        icon = painterResource(id = R.drawable.add),
                                        enabled = freezerTemp < -8 && mode == FridgeMode.DEFAULT
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
