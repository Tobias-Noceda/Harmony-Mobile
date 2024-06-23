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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowHeightSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Blinds
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.model.Status
import ar.edu.itba.harmony_mobile.ui.devices.BlindsViewModel
import ar.edu.itba.harmony_mobile.ui.devices.DevicesViewModel
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory
import ar.edu.itba.harmony_mobile.ui.theme.darken
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.disabled
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


@Composable
fun BlindsScreen(deviceRef: Blinds, onBackCalled: (() -> Unit)? = null) {


    val viewModel: BlindsViewModel = viewModel(factory = getViewModelFactory())
    val dViewModel: DevicesViewModel = viewModel(factory = getViewModelFactory())
    val deviceState by dViewModel.uiState.collectAsState()
    fun getValidDevice(): Blinds {
        if (deviceState.currentDevice != null && deviceState.currentDevice is Blinds) {
            return deviceState.currentDevice as Blinds
        }
        val aux: Device? = deviceState.devices.find { it.id == deviceRef.id }
        if (aux != null && aux is Blinds) {
            return aux
        }
        return deviceRef
    }

    dViewModel.setCurrentDeviceId(deviceRef.id!!)
    var blindsLimit by rememberSaveable { mutableFloatStateOf(getValidDevice().level.toFloat()) }


    val adaptiveInfo = currentWindowAdaptiveInfo()
    if(onBackCalled != null) {
        BackHandler(onBack = onBackCalled)
    }
    @Composable
    fun blindsTitle() {
        Text(
            text = getValidDevice().name,
            color = primary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun blindsStatusText() {
        Text(
            text = "${stringResource(id = R.string.status)} ${getValidDevice().currentLevel}% ${
                when (getValidDevice().status) {
                    Status.OPENING -> "- " + stringResource(id = R.string.opening)
                    Status.CLOSING -> "- " + stringResource(id = R.string.closing)
                    else -> ""
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
                viewModel.open(getValidDevice())
            },
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = (getValidDevice().status == Status.CLOSED)
                    && getValidDevice().currentLevel > 0,
        ) {
            Text(text = stringResource(id = R.string.open))
        }
    }

    @Composable
    fun closeButton() {
        Button(
            onClick = {
                viewModel.close(getValidDevice())
            },
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = (getValidDevice().status == Status.OPENED)
                    && getValidDevice().currentLevel < getValidDevice().level,
        ) {
            Text(text = stringResource(id = R.string.close))
        }
    }

    @Composable
    fun limitMenu() {
        Box(contentAlignment = Alignment.Center) {
            Column {
                Text(
                    text = stringResource(R.string.limit) + " " + "${getValidDevice().level}",
                    color = primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                Slider(
                    value = getValidDevice().level.toFloat(),
                    onValueChange = { blindsLimit = it },
                    onValueChangeFinished = {
                        viewModel.setLevel(getValidDevice(), blindsLimit.toInt())
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
                        limitMenu()
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                        limitMenu()
                    }
                }
            }
        }
    }
}
