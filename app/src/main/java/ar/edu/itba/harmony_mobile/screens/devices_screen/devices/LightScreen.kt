package ar.edu.itba.harmony_mobile.screens.devices_screen.devices

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import ar.edu.itba.harmony_mobile.tools.HsvColorPicker
import ar.edu.itba.harmony_mobile.tools.rememberColorPickerController
import ar.edu.itba.harmony_mobile.ui.theme.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
@Preview
fun LightScreen() {
    var lightName by rememberSaveable { mutableStateOf("Light Name") }
    var lightBrightness by rememberSaveable { mutableFloatStateOf(75f) }
    var lightStatus by rememberSaveable { mutableStateOf(true) }
    var colorMode by rememberSaveable { mutableStateOf(true) }
    var lightColor by remember { mutableStateOf(Color.Red) }

    val colorController = rememberColorPickerController()

    val adaptiveInfo = currentWindowAdaptiveInfo()

    @Composable
    fun lightTitle() {
        Text(
            text = lightName, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
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
                text = if (lightStatus) {
                    "Status: On"
                } else {
                    "Status: Off"
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
                text = if (colorMode) {
                    "Color mode: On"
                } else {
                    "Color mode: Off"
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
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Color:",
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
                Column (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center){
                    HsvColorPicker(modifier = Modifier.padding(0.dp),
                        controller = colorController,
                        onColorChanged = {
                            lightColor = it.color
                            /*MANDAR A LA API*/
                        })
                }
            }
        }
    }

    @Composable
    fun brightnessMenu() {
        Box(contentAlignment = Alignment.Center) {
            Column {
                Text(
                    text = "Brightness: ${lightBrightness.toInt()}",
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
        Column(modifier = Modifier.fillMaxWidth()) {

            lightTitle()
            with(adaptiveInfo) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
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
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f),
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
