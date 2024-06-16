package ar.edu.itba.harmony_mobile.screens.devices_screen.devices

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import ar.edu.itba.harmony_mobile.tools.HsvColorPicker
import ar.edu.itba.harmony_mobile.tools.rememberColorPickerController
import ar.edu.itba.harmony_mobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LightScreen() {
    var lightName by rememberSaveable { mutableStateOf("Light Name") }
    var lightBrightness by rememberSaveable { mutableFloatStateOf(75f) }
    var lightStatus by rememberSaveable { mutableStateOf(true) }
    var colorMode by rememberSaveable { mutableStateOf(true) }
    var lightColor by remember { mutableStateOf(Color.Red) }

    val colorController = rememberColorPickerController()

    Column(
        modifier = Modifier.padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(text = lightName, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold)
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
                },
                color = primary, fontSize = 20.sp, fontWeight = FontWeight.Normal
            )
            Switch(
                checked = lightStatus,
                onCheckedChange = {
                    lightStatus = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = tertiary,
                    checkedTrackColor = tertiary.copy(0.5f),
                    uncheckedThumbColor = primary,
                    uncheckedTrackColor = primary.copy(0.5f),
                )
            )
        }
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
                },
                color = primary, fontSize = 20.sp, fontWeight = FontWeight.Normal
            )
            Switch(
                checked = colorMode,
                onCheckedChange = {
                    colorMode = it
                    colorController.setEnabled(colorMode)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = tertiary,
                    checkedTrackColor = tertiary.copy(0.5f),
                    uncheckedThumbColor = primary,
                    uncheckedTrackColor = primary.copy(0.5f),
                )
            )
        }
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Color:",
                    color = primary, fontSize = 20.sp, fontWeight = FontWeight.Normal
                )
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(lightColor, lightColor, disabled, disabled),
                    enabled = colorMode,
                    border = BorderStroke(2.dp, primary)
                ) {}
            }
            Box(modifier = Modifier.padding(2.dp)) {
                HsvColorPicker(
                    modifier = Modifier.fillMaxHeight(0.7f),
                    controller = colorController,
                    onColorChanged = {
                        lightColor = it.color
                        /*MANDAR A LA API*/
                    })
            }
        }
        Box(contentAlignment = Alignment.Center) {
            Column {
                Text(
                    text = "Brightness: ${lightBrightness.toInt()}",
                    color = primary, fontSize = 20.sp, fontWeight = FontWeight.Normal
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
}