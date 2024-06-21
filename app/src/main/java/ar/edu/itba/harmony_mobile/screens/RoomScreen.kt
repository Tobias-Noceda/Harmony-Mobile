package ar.edu.itba.harmony_mobile.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.screens.devices.BlindsScreen
import ar.edu.itba.harmony_mobile.screens.devices.DoorScreen
import ar.edu.itba.harmony_mobile.screens.devices.FridgeScreen
import ar.edu.itba.harmony_mobile.screens.devices.LightScreen
import ar.edu.itba.harmony_mobile.screens.devices.SprinklerScreen
import ar.edu.itba.harmony_mobile.screens.devices.VacuumScreen
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.BlindsCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.DoorCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.LightCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.RefrigeratorCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.SprinklerCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.VacuumCard
import ar.edu.itba.harmony_mobile.ui.devices.DevicesUiState
import ar.edu.itba.harmony_mobile.ui.rooms.RoomsUiState
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary


@Composable
fun RoomScreen(room: Room, roomsState: RoomsUiState, devicesState: DevicesUiState, onBackCalled: () -> Unit) {
    var currentDevice by rememberSaveable { mutableStateOf("") }
    var currentDeviceType by rememberSaveable { mutableStateOf(DeviceTypes.DOORS) }

    BackHandler(onBack = onBackCalled)

    if(currentDevice == "") {
        RoomDevices(
            roomName = room.name,
            deviceList = devicesState.getRoomDevices(room),
            onDeviceClick = { device ->
                    currentDevice = device.id!!
                    currentDeviceType = device.type
            }
        )
    } else {
        when(currentDeviceType) {
            DeviceTypes.LIGHTS -> LightScreen(devicesState.getDevice(currentDevice)) { currentDevice = "" }
            DeviceTypes.DOORS -> DoorScreen(devicesState.getDevice(currentDevice)) { currentDevice = "" }
            DeviceTypes.REFRIS -> FridgeScreen(devicesState.getDevice(currentDevice)) { currentDevice = "" }
            DeviceTypes.VACUUMS -> VacuumScreen(devicesState.getDevice(currentDevice)) { currentDevice = "" }
            DeviceTypes.SPRINKLERS -> SprinklerScreen(devicesState.getDevice(currentDevice)) { currentDevice = "" }
            else -> BlindsScreen(devicesState.getDevice(currentDevice)) { currentDevice = "" }
        }
        BackHandler(onBack = { currentDevice = "" })
    }
}

@Composable
fun RoomDevices(
    roomName: String,
    deviceList: List<Device>,
    onDeviceClick: (Device) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = roomName,
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
        val modifier = Modifier
            .weight(1f)
            .padding(4.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 6.dp,
                )
                .verticalScroll(scState)
        ) {
            if(isCompact) {
                for (id in deviceList.indices) {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { onDeviceClick(deviceList[id]) },
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
                                painterResource(id = deviceList[id].type.icon),
                                contentDescription = null,
                                modifier = Modifier.height(60.dp)
                            )
                            Text(
                                text = deviceList[id].name,
                                textAlign = TextAlign.Left,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            } else if (isExpanded) {
                val chunkedDevices = deviceList.indices.chunked(2)

                for (chunk in chunkedDevices) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (id in chunk) {
                            when(deviceList[id].type) {
                                DeviceTypes.LIGHTS -> LightCard(lamp = deviceList[id], modifier = modifier) { onDeviceClick(deviceList[id]) }
                                DeviceTypes.DOORS -> DoorCard(door = deviceList[id], modifier = modifier) { onDeviceClick(deviceList[id]) }
                                DeviceTypes.REFRIS -> RefrigeratorCard(refrigerator = deviceList[id], modifier = modifier) { onDeviceClick(deviceList[id]) }
                                DeviceTypes.VACUUMS -> VacuumCard(vacuum = deviceList[id], modifier = modifier) { onDeviceClick(deviceList[id]) }
                                DeviceTypes.SPRINKLERS -> SprinklerCard(sprinkler = deviceList[id], modifier = modifier) { onDeviceClick(deviceList[id]) }
                                else -> BlindsCard(blinds = deviceList[id], modifier = modifier) { onDeviceClick(deviceList[id]) }
                            }
                        }
                        if(chunk.size != 2) {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .weight(1f)
                            ) {}
                        }
                    }
                }
            } else {
                for (id in deviceList.indices) {
                    when(deviceList[id].type) {
                        DeviceTypes.LIGHTS -> LightCard(lamp = deviceList[id]) { onDeviceClick(deviceList[id]) }
                        DeviceTypes.DOORS -> DoorCard(door = deviceList[id]) { onDeviceClick(deviceList[id]) }
                        DeviceTypes.REFRIS -> RefrigeratorCard(refrigerator = deviceList[id]) { onDeviceClick(deviceList[id]) }
                        DeviceTypes.VACUUMS -> VacuumCard(vacuum = deviceList[id]) { onDeviceClick(deviceList[id]) }
                        DeviceTypes.SPRINKLERS -> SprinklerCard(sprinkler = deviceList[id]) { onDeviceClick(deviceList[id]) }
                        else -> BlindsCard(blinds = deviceList[id]) { onDeviceClick(deviceList[id]) }
                    }
                }
            }
        }
    }
}
