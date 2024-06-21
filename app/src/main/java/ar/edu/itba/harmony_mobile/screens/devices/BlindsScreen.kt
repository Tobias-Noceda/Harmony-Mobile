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
import ar.edu.itba.harmony_mobile.ui.devices.BlindsViewModel
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory
import ar.edu.itba.harmony_mobile.ui.theme.darken
import ar.edu.itba.harmony_mobile.ui.theme.desaturate
import ar.edu.itba.harmony_mobile.ui.theme.disabled
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


enum class MoveState {
    STILL, OPENING, CLOSING
}

@Composable
fun BlindsScreen(device: Blinds, onBackCalled: () -> Unit) {

    var isMoving by rememberSaveable { mutableStateOf(MoveState.STILL) }

    var blindsLimit by rememberSaveable { mutableFloatStateOf(0f) }

    val viewModel: BlindsViewModel = viewModel(factory = getViewModelFactory())

    val adaptiveInfo = currentWindowAdaptiveInfo()
    BackHandler(onBack = onBackCalled)
    @Composable
    fun blindsTitle() {
        Text(
            text = device.name, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun blindsStatusText() {
        Text(
            text = "${stringResource(id = R.string.status)} ${device.currentLevel}% ${
                when (isMoving) {
                    MoveState.STILL -> ""
                    MoveState.OPENING -> "- " + stringResource(id = R.string.opening)
                    MoveState.CLOSING -> "- " + stringResource(id = R.string.closing)
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
                viewModel.open(device)
                isMoving = MoveState.OPENING
            },
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = isMoving == MoveState.STILL && device.currentLevel > 0,
        ) {
            Text(text = stringResource(id = R.string.open))
        }
    }

    @Composable
    fun closeButton() {
        Button(
            onClick = {
                viewModel.close(device)
                isMoving = MoveState.CLOSING

            },
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = isMoving == MoveState.STILL && device.currentLevel < device.level,
        ) {
            Text(text = stringResource(id = R.string.close))
        }
    }

    @Composable
    fun limitMenu() {
        Box(contentAlignment = Alignment.Center) {
            Column {
                Text(
                    text = stringResource(R.string.limit) + " " + "${blindsLimit.toInt()}",
                    color = primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                Slider(
                    value = blindsLimit,
                    onValueChange = { blindsLimit = it },
                    onValueChangeFinished = {
                        viewModel.setLevel(device, blindsLimit.toInt())
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
