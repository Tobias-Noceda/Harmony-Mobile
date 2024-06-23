package ar.edu.itba.harmony_mobile.screens.devices

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.model.Lamp
import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.model.Status
import ar.edu.itba.harmony_mobile.tools.HsvColorPicker
import ar.edu.itba.harmony_mobile.tools.rememberColorPickerController
import ar.edu.itba.harmony_mobile.ui.devices.DevicesViewModel
import ar.edu.itba.harmony_mobile.ui.devices.LampViewModel
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory
import ar.edu.itba.harmony_mobile.ui.theme.darken
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.disabled
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

@Composable
fun LightScreen(deviceRef: Lamp, onBackCalled: (() -> Unit)? = null) {
    val colorController = rememberColorPickerController()
    val scState = rememberScrollState(0)
    val adaptiveInfo = currentWindowAdaptiveInfo()

    val viewModel: LampViewModel = viewModel(factory = getViewModelFactory())
    val dViewModel: DevicesViewModel = viewModel(factory = getViewModelFactory())
    val deviceState by dViewModel.uiState.collectAsState()

    var first = true

    dViewModel.setCurrentDeviceId(deviceRef.id!!)

    fun getValidDevice(): Lamp {
        if (deviceState.currentDevice != null && deviceState.currentDevice is Lamp) {
            return deviceState.currentDevice as Lamp
        }
        val aux: Device? = deviceState.devices.find { it.id == deviceRef.id }
        if (aux != null && aux is Lamp) {
            return aux
        }
        return deviceRef    }

    var colorString by rememberSaveable { mutableStateOf(Lamp.colorToString(getValidDevice().color)) }

    var lightBrightness by rememberSaveable { mutableFloatStateOf(getValidDevice().brightness.toFloat()) }

    if (onBackCalled != null) {
        BackHandler(onBack = onBackCalled)
    }

    @Composable
    fun lightTitle() {
        Text(
            text = getValidDevice().name,
            color = primary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
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
                text = stringResource(id = R.string.status) + " " + if (getValidDevice().status == Status.ON) {
                    stringResource(id = R.string.on)
                } else {
                    stringResource(id = R.string.off)

                }, color = primary, fontSize = 20.sp, fontWeight = FontWeight.Normal
            )
            Switch(
                checked = getValidDevice().status == Status.ON, onCheckedChange = {
                    if (getValidDevice().status == Status.ON) {
                        viewModel.turnOff(getValidDevice())
                    } else {
                        viewModel.turnOn(getValidDevice())
                    }
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
                horizontalArrangement = Arrangement.spacedBy(20.dp),
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
                        Color("#ff${colorString}".toColorInt()),
                        Color("#ff${colorString}".toColorInt()),
                        Color("#ff${colorString}".toColorInt()).desaturate(0f),
                        Color("#ff${colorString}".toColorInt()).desaturate(0f)
                    ), border = BorderStroke(2.dp, primary)
                ) {}
            }
            with(adaptiveInfo) {
                if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT
                    && windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        HsvColorPicker(
                            modifier = Modifier.padding(0.dp),
                            controller = colorController,
                            onColorChanged = {
                                if (!first && it.color != Color.Black) {
                                    Log.i("Color picker", it.color.toString())
                                    viewModel.setColor(getValidDevice(), it.color)
                                    colorString = Lamp.colorToString(it.color)
                                }
                                first = false
                            },
                        )
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
                                onClick = {
                                    viewModel.setColor(getValidDevice(), Color.Red)
                                    colorString = Lamp.colorToString(Color.Red)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonColors(
                                    Color.Red,
                                    Color.Red,
                                    getValidDevice().color.desaturate(0f),
                                    getValidDevice().color.desaturate(0f)
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    if (getValidDevice().color == Color.Red) {
                                        tertiary
                                    } else {
                                        primary
                                    }
                                )
                            ) {}
                            Button(
                                onClick = {
                                    viewModel.setColor(getValidDevice(), Color.Blue)
                                    colorString = Lamp.colorToString(Color.Blue)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonColors(
                                    Color.Blue,
                                    Color.Blue,
                                    getValidDevice().color.desaturate(0f),
                                    getValidDevice().color.desaturate(0f)
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    if (getValidDevice().color == Color.Blue) {
                                        tertiary
                                    } else {
                                        primary
                                    }
                                )
                            ) {}
                            Button(
                                onClick = {
                                    viewModel.setColor(getValidDevice(), Color.Green)
                                    colorString = Lamp.colorToString(Color.Green)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonColors(
                                    Color.Green,
                                    Color.Green,
                                    getValidDevice().color.desaturate(0f),
                                    getValidDevice().color.desaturate(0f)
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    if (getValidDevice().color == Color.Green) {
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
                                onClick = {
                                    viewModel.setColor(getValidDevice(), Color.Yellow)
                                    colorString = Lamp.colorToString(Color.Yellow)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonColors(
                                    Color.Yellow,
                                    Color.Yellow,
                                    getValidDevice().color.desaturate(0f),
                                    getValidDevice().color.desaturate(0f)
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    if (getValidDevice().color == Color.Yellow) {
                                        tertiary
                                    } else {
                                        primary
                                    }
                                )
                            ) {}
                            Button(
                                onClick = {
                                    viewModel.setColor(getValidDevice(), Color.Cyan)
                                    colorString = Lamp.colorToString(Color.Cyan)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonColors(
                                    Color.Cyan,
                                    Color.Cyan,
                                    getValidDevice().color.desaturate(0f),
                                    getValidDevice().color.desaturate(0f)
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    if (getValidDevice().color == Color.Cyan) {
                                        tertiary
                                    } else {
                                        primary
                                    }
                                )
                            ) {}
                            Button(
                                onClick = {
                                    viewModel.setColor(getValidDevice(), Color.Magenta)
                                    colorString = Lamp.colorToString(Color.Magenta)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonColors(
                                    Color.Magenta,
                                    Color.Magenta,
                                    getValidDevice().color.desaturate(0f),
                                    getValidDevice().color.desaturate(0f)
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    if (getValidDevice().color == Color.Magenta) {
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

    @Composable
    fun brightnessMenu() {
        Box(contentAlignment = Alignment.Center) {
            Column {
                Text(
                    text = stringResource(R.string.brightness) + " " + "${getValidDevice().brightness}",
                    color = primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                Slider(
                    value = getValidDevice().brightness.toFloat(),
                    onValueChange = { lightBrightness = it },
                    onValueChangeFinished = {
                        viewModel.setBrightness(
                            getValidDevice(),
                            lightBrightness.toInt()
                        )
                        dViewModel.getDevice(deviceRef.id)
                    },
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
                    enabled = getValidDevice().status == Status.ON
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
                            brightnessMenu()
                        }
                        colorMenu()
                    }
                }
            }
        }
    }

}