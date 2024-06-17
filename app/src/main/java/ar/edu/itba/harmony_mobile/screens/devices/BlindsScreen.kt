package ar.edu.itba.harmony_mobile.screens.devices

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import ar.edu.itba.harmony_mobile.ui.theme.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.window.core.layout.WindowHeightSizeClass
import ar.edu.itba.harmony_mobile.R

@Preview(device = Devices.PHONE, showBackground = true)
@Composable
fun BlindsPreview() {
    BlindsScreen()
}

enum class MoveState {
    STILL, OPENING, CLOSING
}

@Composable
fun BlindsScreen() {
    val blindsName by rememberSaveable { mutableStateOf("Blinds Name") }
    var blindsLimit by rememberSaveable { mutableFloatStateOf(75f) }
    var blindsStatus by rememberSaveable { mutableFloatStateOf(0f) }

    var isMoving by rememberSaveable { mutableStateOf(MoveState.STILL) }

    val adaptiveInfo = currentWindowAdaptiveInfo()

    @Composable
    fun blindsTitle() {
        Text(
            text = blindsName, color = primary, fontSize = 30.sp, fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun blindsStatusText() {
        Text(
            text = "${stringResource(id = R.string.status)} ${blindsStatus.toInt()}% ${
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
            onClick = { /*SEND TO API*/
                blindsStatus = 0f
//                isMoving = moveState.OPENING //Lo comento xq todavia la API no devuelve cuando termine de moverse
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = isMoving == MoveState.STILL && blindsStatus > 0f,
            border = BorderStroke(2.dp, primary)
        ) {
            Text(text = stringResource(id = R.string.open))
        }
    }

    @Composable
    fun closeButton() {
        Button(
            onClick = { /*SEND TO API*/
                blindsStatus = blindsLimit
//                isMoving = moveState.CLOSING  //Lo comento xq todavia la API no devuelve cuando termine de moverse

            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                tertiary,
                secondary,
                tertiary.desaturate(0f),
                secondary.desaturate(0f)
            ),
            enabled = isMoving == MoveState.STILL && blindsStatus < blindsLimit,
            border = BorderStroke(2.dp, primary)
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
