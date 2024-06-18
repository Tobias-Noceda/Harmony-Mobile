package ar.edu.itba.harmony_mobile.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.screens.devices.BlindsScreen
import ar.edu.itba.harmony_mobile.screens.devices.LightScreen
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.BlindsCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.DoorCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.LightCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.RefrigeratorCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.SprinklerCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.VacuumCard
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

@Composable
fun DevicesByType(type: DeviceTypes, currentHouse: String, onBackCalled: () -> Unit) {
    var currentName by rememberSaveable { mutableStateOf("") }

    BackHandler(onBack = onBackCalled)

    if (currentName == "") {
        TypeList(
            type,
            onDeviceClick = { deviceName ->
                currentName = deviceName
            }
        )
    } else {
        when(type) {
            DeviceTypes.BLINDS -> BlindsScreen(currentName) { currentName = "" }
            else -> LightScreen(currentName) { currentName = "" }
        }
    }
}

@Composable
fun TypeList(type: DeviceTypes, onDeviceClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = type.type),
            modifier = Modifier.padding(
                start = 12.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
            style = MaterialTheme.typography.titleLarge
        )

        val scState = rememberScrollState(0)
        val widthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
        val isCompact = widthClass == WindowWidthSizeClass.COMPACT
        val isExpanded = widthClass == WindowWidthSizeClass.EXPANDED

        val deviceIds = listOf(0, 1, 2, 3, 4)
        val deviceNames = listOf("CeilingLamp", "BedsideLamp", "FootLamp", "CeilingLamp2", "BarLight")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 6.dp,
                )
                .verticalScroll(scState)
        ) {
            if(isCompact) {
                for (id in deviceIds) {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { onDeviceClick(deviceNames[id]) },
                        colors = ButtonColors(secondary, primary, tertiary.copy(alpha = .5f), Color.White),
                        elevation = ButtonDefaults.buttonElevation(8.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = 0.3f)
                        )
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(id = type.icon),
                                contentDescription = null,
                                modifier = Modifier.height(60.dp)
                            )
                            Text(
                                text = deviceNames[id],
                                textAlign = TextAlign.Left,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            } else if (isExpanded) {
                val chunkedDevices = deviceIds.chunked(2)

                for (chunk in chunkedDevices) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (id in chunk) {
                            when(type) {
                                DeviceTypes.LIGHTS -> LightCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                                DeviceTypes.DOORS -> DoorCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                                DeviceTypes.REFRIS -> RefrigeratorCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                                DeviceTypes.VACUUMS -> VacuumCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                                DeviceTypes.SPRINKLERS -> SprinklerCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                                else -> BlindsCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                            }
                        }
                    }
                }
            } else {
                for (id in deviceIds) {
                    when(type) {
                        DeviceTypes.LIGHTS -> LightCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                        DeviceTypes.DOORS -> DoorCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                        DeviceTypes.REFRIS -> RefrigeratorCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                        DeviceTypes.VACUUMS -> VacuumCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                        DeviceTypes.SPRINKLERS -> SprinklerCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                        else -> BlindsCard(name = deviceNames[id]) { onDeviceClick(deviceNames[id]) }
                    }
                }
            }
        }
    }
}
