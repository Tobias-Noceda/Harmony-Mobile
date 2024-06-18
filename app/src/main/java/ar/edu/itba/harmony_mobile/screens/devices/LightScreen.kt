package ar.edu.itba.harmony_mobile.screens.devices

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.tools.HsvColorPicker
import ar.edu.itba.harmony_mobile.tools.rememberColorPickerController
import ar.edu.itba.harmony_mobile.ui.theme.darken
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.disabled
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

// @Preview(device = Devices.PHONE, showBackground = true)
// @Composable
// fun LightPreview() {
//    LightScreen()
//}

@Composable
fun LightScreen(deviceName: String, onBackCalled: () -> Unit) {
    var lightBrightness by rememberSaveable { mutableFloatStateOf(75f) }
    var lightStatus by rememberSaveable { mutableStateOf(true) }
    var colorMode by rememberSaveable { mutableStateOf(true) }
    var lightColor by remember { mutableStateOf(Color.Red) }

    val colorController = rememberColorPickerController()
    val scState = rememberScrollState(0)
    val adaptiveInfo = currentWindowAdaptiveInfo()

    BackHandler(onBack = onBackCalled)

    @Composable
    fun lightTitle() {
        Text(
            text = deviceName, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
        )

    }

    @Composable
    fun lightSwitch() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.status) + " " + if (lightStatus) {
                    stringResource(id = R.string.on)
                } else {
                    stringResource(id = R.string.off)

                }, color = primary, fontSize = 20.sp, fontWeight = FontWeight.Normal
            )
            Switch(
                checked = lightStatus, onCheckedChange = {
                    lightStatus = it
                }, colors = SwitchDefaults.colors(
                    checkedThumbColor = tertiary,
                    checkedTrackColor = tertiary.copy(0.5f),
                    uncheckedThumbColor = primary,
                    uncheckedTrackColor = primary.copy(0.5f),
                )
            )
        }
    }

    @Composable
    fun colorSwitch() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.color_mode) + " " + if (colorMode) {
                    stringResource(id = R.string.on)
                } else {
                    stringResource(id = R.string.off)
                }, color = primary, fontSize = 20.sp, fontWeight = FontWeight.Normal
            )
            Switch(
                checked = colorMode, onCheckedChange = {
                    colorMode = it
                    colorController.setEnabled(colorMode)
                }, colors = SwitchDefaults.colors(
                    checkedThumbColor = tertiary,
                    checkedTrackColor = tertiary.copy(0.5f),
                    uncheckedThumbColor = primary,
                    uncheckedTrackColor = primary.copy(0.5f),
                )
            )
        }
    }

    @Composable
    fun colorMenu() {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.color),
                    color = primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                Button(
                    onClick = {}, shape = RoundedCornerShape(8.dp), colors = ButtonColors(
                        lightColor, lightColor, lightColor.desaturate(0f), lightColor.desaturate(0f)
                    ), enabled = colorMode, border = BorderStroke(2.dp, primary)
                ) {}
            }
            if (colorMode) {
                with(adaptiveInfo) {
                    if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT
                        && windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            HsvColorPicker(modifier = Modifier.padding(0.dp),
                                controller = colorController,
                                onColorChanged = {
                                    lightColor = it.color
                                    /*MANDAR A LA API*/
                                })
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Button(
                                    onClick = { lightColor = Color.Red },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonColors(
                                        Color.Red,
                                        Color.Red,
                                        lightColor.desaturate(0f),
                                        lightColor.desaturate(0f)
                                    ),
                                    enabled = colorMode,
                                    border = BorderStroke(
                                        2.dp,
                                        if (lightColor == Color.Red) {
                                            tertiary
                                        } else {
                                            primary
                                        }
                                    )
                                ) {}
                                Button(
                                    onClick = { lightColor = Color.Blue },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonColors(
                                        Color.Blue,
                                        Color.Blue,
                                        lightColor.desaturate(0f),
                                        lightColor.desaturate(0f)
                                    ),
                                    enabled = colorMode,
                                    border = BorderStroke(
                                        2.dp,
                                        if (lightColor == Color.Blue) {
                                            tertiary
                                        } else {
                                            primary
                                        }
                                    )
                                ) {}
                                Button(
                                    onClick = { lightColor = Color.Green },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonColors(
                                        Color.Green,
                                        Color.Green,
                                        lightColor.desaturate(0f),
                                        lightColor.desaturate(0f)
                                    ),
                                    enabled = colorMode,
                                    border = BorderStroke(
                                        2.dp,
                                        if (lightColor == Color.Green) {
                                            tertiary
                                        } else {
                                            primary
                                        }
                                    )
                                ) {}
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Button(
                                    onClick = { lightColor = Color.Yellow },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonColors(
                                        Color.Yellow,
                                        Color.Yellow,
                                        lightColor.desaturate(0f),
                                        lightColor.desaturate(0f)
                                    ),
                                    enabled = colorMode,
                                    border = BorderStroke(
                                        2.dp,
                                        if (lightColor == Color.Yellow) {
                                            tertiary
                                        } else {
                                            primary
                                        }
                                    )
                                ) {}
                                Button(
                                    onClick = { lightColor = Color.Cyan },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonColors(
                                        Color.Cyan,
                                        Color.Cyan,
                                        lightColor.desaturate(0f),
                                        lightColor.desaturate(0f)
                                    ),
                                    enabled = colorMode,
                                    border = BorderStroke(
                                        2.dp,
                                        if (lightColor == Color.Cyan) {
                                            tertiary
                                        } else {
                                            primary
                                        }
                                    )
                                ) {}
                                Button(
                                    onClick = { lightColor = Color.Magenta },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonColors(
                                        Color.Magenta,
                                        Color.Magenta,
                                        lightColor.desaturate(0f),
                                        lightColor.desaturate(0f)
                                    ),
                                    enabled = colorMode,
                                    border = BorderStroke(
                                        2.dp,
                                        if (lightColor == Color.Magenta) {
                                            tertiary
                                        } else {
                                            primary
                                        }
                                    )
                                ) {}
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun brightnessMenu() {
        Box(contentAlignment = Alignment.Center) {
            Column {
                Text(
                    text = stringResource(R.string.brightness) + " " + "${lightBrightness.toInt()}",
                    color = primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                Slider(
                    value = lightBrightness,
                    onValueChange = { lightBrightness = it },
                    onValueChangeFinished = {/*MANDAR A LA API*/ },
                    valueRange = 0f..100f,
                    colors = SliderColors(
                        tertiary.darken(0.9f),
                        tertiary,
                        Color.Transparent,
                        primary,
                        Color.Transparent,
                        disabled.darken(0.9f),
                        disabled,
                        Color.Transparent,
                        disabled.darken(0.75f),
                        Color.Transparent
                    ),
                    enabled = lightStatus
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            lightTitle()
            with(adaptiveInfo) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
                    || (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM && windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.EXPANDED)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(25.dp)
                    ) {
                        lightSwitch()
                        colorSwitch()
                        brightnessMenu()
                        colorMenu()
                    }
                } else {
                    Row(
                        modifier = if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
                            Modifier
                                .padding(10.dp)
                                .verticalScroll(scState)
                        } else {
                            Modifier.padding(10.dp)
                        },
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            lightSwitch()
                            colorSwitch()
                            brightnessMenu()
                        }
                        colorMenu()
                    }
                }
            }
        }
    }

}
